package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.utils.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 移库出库
 */
@Component(OutBoundType.TASK_TYPE+0)
@Transactional(rollbackFor=Exception.class,timeout = 1000)
@Slf4j
@SuppressWarnings("all")
public class MoveBoundStrategy extends DefaultOutBoundPickCodeStrategy {

    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;
    @Autowired
    ContainerTaskMapper containerTaskMapper;
    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;
    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;
    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;
    @Autowired
    QcSxStoreMapper qcSxStoreMapper;
    @Autowired
    PortInfoMapper portInfoMapper;
    @Autowired
    PickStainStrategy pickStainStrategy;
    @Autowired
    OutBoundContainerService outBoundContainerService;

    @Autowired
    SxStoreCkService sxStoreCkService;


    /**
     * 移库出库
     * @param outboundTask
     */
    @Override
    public void unbound(OutboundTask outboundTask) {
        //根据订单号查询需要移库的明细
        List<OutboundTaskDetail> outboundTaskDetailList = outBoundTaskDetailMapper.findByMap(MapUtils.put("billNo", outboundTask.getBillNo()).getMap(), OutboundTaskDetail.class);
        for (OutboundTaskDetail outboundTaskDetail : outboundTaskDetailList) {
            //查询库存
            Map<String, Object> sxStore = qcSxStoreMapper.findSxStore(outboundTaskDetail.getContainerCode(), outboundTaskDetail.getLotId(), outboundTaskDetail.getItemId());
            if (sxStore==null){
                LogServices.logSysBusiness("单号："+outboundTaskDetail.getBillNo()+",托盘号:"+outboundTaskDetail.getContainerCode()+"正在出库中或库存中不存在！！！");
                outBoundContainerService.deleteDetailAndInsertHistory(outboundTaskDetail);
                continue;
            }

            String pickCode = outboundTaskDetail.getPickCode();
            List<PickStation> listPickStations = null;
            String target = "" ;
            AgvStorageLocation agvLocation = null;
            if (StringUtils.isEmpty(pickCode)){
                //拣选站要求为空
                agvLocation = getPickStationAndLock();
                if(StringUtils.isEmpty(agvLocation)) { LogServices.logSysBusiness("拣选站上有托盘或已被锁定，无可用拣选站");return;}
                target = agvLocation.getRcsPositionCode();
            }else {
                //拣选站不为空
                agvLocation = agvStorageLocationMapper.findByPickCodeAndLock("pickStation4", 0, 0);
                if(StringUtils.isEmpty(agvLocation)) { LogServices.logSysBusiness("拣选站："+pickCode+"上有托盘或已被锁定，不可用");return;}
                target = agvLocation.getRcsPositionCode();
                agvLocation.setLocationLock(1);
                agvStorageLocationMapper.update(agvLocation);
            }

            //封装成托盘任务
            ContainerTask containerTask = getContainerTask(outboundTaskDetail, sxStore);
            String sourceLocation = PrologCoordinateUtils.splicingStr((Integer) sxStore.get("x"), (Integer) sxStore.get("y"), (Integer) sxStore.get("layer"));
            containerTask.setSource(sourceLocation);
            containerTask.setTaskState(1);
            containerTask.setTarget(target);
            containerTask.setTargetType(OutBoundEnum.TargetType.AGV.getNumber()); //Agv目标区域

            //封装成托盘任务明细
            ContainerTaskDetail containerTaskDetail = getContainerTaskDetail(outboundTaskDetail, sxStore);

            if (outboundTaskDetail.getQty()>((BigDecimal) sxStore.get("qty")).floatValue()){
                //出库数量大于库存数量
                containerTaskDetail.setQty(((BigDecimal) sxStore.get("qty")).floatValue());
            }else {
                containerTaskDetail.setQty(outboundTaskDetail.getQty());
            }

            containerTask.setTaskCode(null);
            //保存托盘任务明细
            containerTaskDetailMapperMapper.save(containerTaskDetail);
            //保存托盘任务
            containerTaskMapper.save(containerTask);
            sxStoreCkService.buildSxCkTaskByContainerTask(containerTask);

            //转到历史记录
            outBoundContainerService.deleteDetailAndInsertHistory(outboundTaskDetail, containerTaskDetail);

        }
        //将移库出库订单转入到历史
        outBoundContainerService.deleteOutBoundAndInsertHistory(outboundTask);
    }


    /**
     * 将移库出库明细转为托盘任务明细
     * @param outboundTaskDetail
     * @param sxStore
     * @return
     */
    private ContainerTaskDetail getContainerTaskDetail(OutboundTaskDetail outboundTaskDetail, Map<String, Object> sxStore) {
        ContainerTaskDetail containerTaskDetail = new ContainerTaskDetail();
        containerTaskDetail.setContainerCode((String) sxStore.get("containerNo"));
        containerTaskDetail.setBillNo(outboundTaskDetail.getBillNo());
        containerTaskDetail.setSeqNo(outboundTaskDetail.getSeqNo());
        containerTaskDetail.setItemId(outboundTaskDetail.getItemId());
        containerTaskDetail.setLotId(outboundTaskDetail.getLotId());
        containerTaskDetail.setOwnerId(outboundTaskDetail.getOwnerId());
        containerTaskDetail.setCreateTime(new Date(System.currentTimeMillis()));
        return containerTaskDetail;
    }

    /**
     * 生成托盘任务
     * @param outboundTaskDetail
     * @param sxStore
     * @return
     */
    private ContainerTask getContainerTask(OutboundTaskDetail outboundTaskDetail, Map<String, Object> sxStore) {
        ContainerTask containerTask = new ContainerTask();
        containerTask.setContainerCode((String) sxStore.get("containerNo"));
        containerTask.setTaskType(0);
        containerTask.setSourceType(1);
        containerTask.setLotId(outboundTaskDetail.getLotId());
        containerTask.setItemId(outboundTaskDetail.getItemId());
        containerTask.setItemName(outboundTaskDetail.getItemName());
        containerTask.setOwnerId(outboundTaskDetail.getOwnerId());
        containerTask.setQty(((BigDecimal) sxStore.get("qty")).floatValue());
        containerTask.setCreateTime(new Date(System.currentTimeMillis()));
        return containerTask;
    }

}