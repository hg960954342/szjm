package com.prolog.eis.service.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewMCSResultHandler implements ResultHandler<Map<String, Object>> {

    List<Map<String, Object>> list=new ArrayList<>();


    public List<Map<String, Object>> getList() {
        return list;
    }

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String, Object> x = resultContext.getResultObject();
        String JSON = (String) x.get("params");
        String typeOld = (String) x.get("type") == null ? "" : (String) x.get("type");
        String containerNoOld = (String) x.get("containerNo") == null ? "" : (String) x.get("containerNo");
        EisParams params = JSONObject.parseObject(JSON, EisParams.class);
        List<EisParams.CarryListBean> listEisParams = params.getCarryList();
        for(EisParams.CarryListBean y:listEisParams){
            int Type = y.getType();
            if (Type == 1) {
                x.put("type", String.format("%s 入库", typeOld));
            } else if (Type == 2) {
                x.put("type", String.format("%s 出库", typeOld));
            } else if (Type == 3) {
                x.put("type", String.format("%s 同层移位", typeOld));
            } else if (Type == 4) {
                x.put("type", String.format("%s 输送线前进", typeOld));
            } else {
                x.put("type", String.format("%s 未知状态", typeOld));
            }
            String containerNo = y.getContainerNo();
            x.put("containerNo", String.format("%s %s", containerNoOld, containerNo));
        };

          list.add(x);
    }
}
