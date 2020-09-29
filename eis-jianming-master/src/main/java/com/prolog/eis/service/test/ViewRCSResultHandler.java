package com.prolog.eis.service.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewRCSResultHandler implements ResultHandler<Map<String, Object>> {

    List<Map<String, Object>> list=new ArrayList<>();


    public List<Map<String, Object>> getList() {
        return list;
    }

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext){
         Map<String, Object> x = resultContext.getResultObject();
            String params = (String) x.get("params");
            RcsParams rcsParams = JSONObject.parseObject(params, RcsParams.class);
            List<RcsParams.PositionCodePathBean> listRCS = rcsParams.getPositionCodePath();
            String startPoint = "位置异常";
            String endPoint = "位置异常";
            if (listRCS.size() == 2) {
                startPoint = listRCS.get(0).getPositionCode();
                endPoint = listRCS.get(1).getPositionCode();
            }
            x.put("start", startPoint);
            x.put("end", endPoint);
            list.add(x);

    }
}
