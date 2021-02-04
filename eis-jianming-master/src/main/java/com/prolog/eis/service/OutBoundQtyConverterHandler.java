package com.prolog.eis.service;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//TODO 出库回库wms 修改qty 单位转换g->>>kg
public class OutBoundQtyConverterHandler implements ResultHandler<Map<String,Object>> {

    List<Map<String,Object>> list=new ArrayList<>();


    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String,Object> map=resultContext.getResultObject();
        //TODO 出库回库wms 修改qty 单位转换G---->KG
        if(map.containsKey("qty")){
            BigDecimal qty=(BigDecimal) map.get("qty");
            BigDecimal resultQty=qty.divide(new BigDecimal("1000"));
            map.put("qty",resultQty);
        }
        if(map.containsKey("standard")){
            BigDecimal standard=(BigDecimal) map.get("standard");
            BigDecimal standardQty=standard.divide(new BigDecimal("1000"));
            map.put("standard",standardQty);
        }
        list.add(map);
    }


    public List<Map<String, Object>> getList() {
        return list;
    }
}
