package com.prolog.eis.service.impl.unbound.handler;

import com.prolog.eis.dto.sxk.OutBoundSxStoreDto;
import com.prolog.eis.service.impl.unbound.DivideAndRemainderToFloat;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutBoundSxStoreHandler implements ResultHandler<Map> {

    private List<OutBoundSxStoreDto> list=new ArrayList<>();



    @Override
    public void handleResult(ResultContext<? extends Map> resultContext) {
        Map<String,Object> map=resultContext.getResultObject();
        BigDecimal qty= ((BigDecimal) map.get("qty"));

        BigDecimal miniPackage= ((BigDecimal) map.get("miniPackage"));
        BigDecimal[] b= DivideAndRemainderToFloat.divideAndRemainderToFloat(miniPackage,qty);
        BigDecimal lqty=b[1];
        BigDecimal zqty =b[0];
        OutBoundSxStoreDto outBoundSxStoreDto=new OutBoundSxStoreDto();
        String itemId=(String)map.get("item_id");
        String lotId=(String)map.get("lot_id");
        String ownerId=(String)map.get("owner_id");
        String storeNo=(String)map.get("store_no");
        String containerNo=(String)map.get("CONTAINER_NO");
        outBoundSxStoreDto.setItemId(itemId);
        outBoundSxStoreDto.setLotId(lotId);
        outBoundSxStoreDto.setOwnerId(ownerId);
        outBoundSxStoreDto.setQty(qty);
        outBoundSxStoreDto.setZqty(zqty);
        outBoundSxStoreDto.setLqty(lqty);
        outBoundSxStoreDto.setStoreNo(storeNo);
        outBoundSxStoreDto.setContainerNo(containerNo);
        outBoundSxStoreDto.setOutOrNo(false);
        outBoundSxStoreDto.setOutQty(new BigDecimal("0"));
        list.add(outBoundSxStoreDto);
    }

    public List<OutBoundSxStoreDto> getList() {
        return list;
    }



}
