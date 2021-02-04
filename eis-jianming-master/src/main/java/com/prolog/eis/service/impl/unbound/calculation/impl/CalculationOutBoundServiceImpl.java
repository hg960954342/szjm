package com.prolog.eis.service.impl.unbound.calculation.impl;

import com.prolog.eis.dao.QcSxStoreMapper;
import com.prolog.eis.dto.sxk.OutBoundSxStoreDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.service.impl.unbound.DivideAndRemainderToFloat;
import com.prolog.eis.service.impl.unbound.calculation.CalculationService;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import com.prolog.eis.service.impl.unbound.handler.OutBoundSxStoreHandler;
import com.prolog.eis.util.ListHelper;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationOutBoundServiceImpl implements CalculationService<OutBoundSxStoreDto> {

    @Autowired
    QcSxStoreMapper qcSxStoreMapper;

    public List<OutBoundSxStoreDto> buildOutBoundQty(DetailDataBean detailDataBeand, float miniPackage, boolean isPickStation) {

        List<OutBoundSxStoreDto> results = new ArrayList<>();
        //TODO 需要出库的量
        float last = DivideAndRemainderToFloat.subtract(detailDataBeand.getQty(),detailDataBeand.getFinishQty());
        //TODO 需要出库的总量
        float allLast = last;

        if (last == 0) {
            LogServices.logSysBusiness(String.format("订单:%s错误，出库数量为0!", detailDataBeand.getBillNo()));
            return results;
        }
        if (last < 0) {
            LogServices.logSysBusiness(String.format("订单:%s待出库库存:%s/1000计算出来错误!", detailDataBeand.getBillNo(), last));
            return results;
        }
        //TODO 判断库存是否满足
        float countQty = qcSxStoreMapper.getSxStoreCount(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId());
        if (countQty < last) {
            LogServices.logSysBusiness(String.format("库存:%s，不够订单:%s 出的量:%s/1000!", countQty, detailDataBeand.getBillNo(), last));
            return results;
        }

        BigDecimal[] b= DivideAndRemainderToFloat.divideAndRemainderToFloat(miniPackage,last);
        float lqty = b[1].floatValue();
        float zqty = b[0].floatValue();
        OutBoundSxStoreHandler handler = new OutBoundSxStoreHandler();
        qcSxStoreMapper.getSxStoreByOrderEntity(detailDataBeand.getItemId(), detailDataBeand.getLotId(), detailDataBeand.getOwnerId(), miniPackage, handler);
        List<OutBoundSxStoreDto> list = handler.getList();
        List<OutBoundSxStoreDto> zList = ListHelper.where(list, x -> x.getLqty() == 0);
        list = ListHelper.where(list, x -> x.getLqty() != 0);
        //TODO 获取零散的全部总量
        float sumLqty = (float) list.parallelStream().mapToDouble(OutBoundSxStoreDto::getLqty).sum();
        //TODO 零散能出全部
        if (last <= sumLqty) {
            computeL(list, last);
        } else {
            if (last <= miniPackage) {
                //TODO 零散出不了全部 则先出要出库的零散 lqty 在整出zqty
                last = computeL(list, lqty);
                //TODO 优先出半零散
                if (last == 0) {
                    List<OutBoundSxStoreDto> listx = ListHelper.where(list, x -> x.getZqty() != 0);
                    list.removeAll(listx);
                    zList = ListUtils.union(listx, zList);
                    computeZ(zList, zqty);
                } else {
                    last = computeZ(list, DivideAndRemainderToFloat.add(last,zqty) );
                    //TODO 出整
                    computeZ(zList, last);
                }
            } else {
                //TODO 零散可能出不完
                float y = computeL(list, lqty);
                float Lsum = (float) list.parallelStream().mapToDouble(OutBoundSxStoreDto::getOutQty).sum();
                List<OutBoundSxStoreDto> listx = ListHelper.where(list, x -> x.getZqty() != 0);
                list.removeAll(listx);
                zList = ListUtils.union(listx, zList);
                computeZ(zList, listx.isEmpty() ? DivideAndRemainderToFloat.add(zqty,y) : DivideAndRemainderToFloat.subtract(last,Lsum));

            }


        }

        //TODO 出库校验
        List<OutBoundSxStoreDto> resultList = new ArrayList<>();
        resultList.addAll(zList);
        resultList.addAll(list);

        float sumQty = (float) resultList.parallelStream().mapToDouble(OutBoundSxStoreDto::getOutQty).sum();
        if (sumQty <= allLast) {
            for (OutBoundSxStoreDto outBoundSxStoreDto : resultList) {
                if (outBoundSxStoreDto.getOutQty() != 0) {
                    results.add(outBoundSxStoreDto);
                }
            }
        }

        return results;
    }

    //TODO 计算零散
    public float computeL(List<OutBoundSxStoreDto> list, float last) {
        for (OutBoundSxStoreDto outBoundSxStoreDto : list) {
            if (outBoundSxStoreDto.getLqty() != 0 && outBoundSxStoreDto.getLqty() <= last) {
                outBoundSxStoreDto.setOutQty(DivideAndRemainderToFloat.add(outBoundSxStoreDto.getOutQty() , outBoundSxStoreDto.getLqty()));
                last=DivideAndRemainderToFloat.subtract(last,outBoundSxStoreDto.getOutQty());
                if (last <= 0) {
                    break;
                } else {
                    continue;
                }
            }
            if (outBoundSxStoreDto.getLqty() != 0 && outBoundSxStoreDto.getLqty() > last) {
                float outQty = last;
                outBoundSxStoreDto.setOutQty(DivideAndRemainderToFloat.add(outBoundSxStoreDto.getOutQty() , outQty));
                last=DivideAndRemainderToFloat.subtract(last,outBoundSxStoreDto.getOutQty());
                break;
            }
        }
        return last;
    }

    //TODO 计算整包
    private float computeZ(List<OutBoundSxStoreDto> list, float last) {
        for (OutBoundSxStoreDto outBoundSxStoreDto : list) {
            if (outBoundSxStoreDto.getZqty() != 0 && outBoundSxStoreDto.getZqty() <= last) {
                outBoundSxStoreDto.setOutQty(DivideAndRemainderToFloat.add(outBoundSxStoreDto.getOutQty() , outBoundSxStoreDto.getZqty()));
                last=DivideAndRemainderToFloat.subtract(last,outBoundSxStoreDto.getZqty());
                if (last <= 0) {
                    break;
                } else {
                    continue;
                }
            }
            if (outBoundSxStoreDto.getZqty() != 0 && outBoundSxStoreDto.getZqty() > last) {
                float outQty = last;
                outBoundSxStoreDto.setOutQty(DivideAndRemainderToFloat.add(outBoundSxStoreDto.getOutQty() , outQty));
                last=DivideAndRemainderToFloat.subtract(last,outQty);
                break;
            }
        }
        return last;
    }


}
