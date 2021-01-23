package com.prolog.eis.mybatis;

import com.prolog.eis.base.dao.AbstractResultHandler;
import org.apache.ibatis.session.ResultContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BasePagerDataHandler extends AbstractResultHandler<Map<String,Object>> {


    @Override
    public List<Map<String,Object>> getResults() {
        return super.results;
    }

    @Override
    public void handleResult(ResultContext<? extends Map<String, Object>> resultContext) {
        Map<String, Object> map= resultContext.getResultObject();
        String dataFormat="yyyy-MM-dd hh:mm:ss";
        insertTime("createTime",map,dataFormat);
        insertTime("updateTime",map,dataFormat);
        results.add(map);

    }

    private void insertTime(String keyword, Map<String, Object> map,String dateFormat){
        if(!map.isEmpty()&&map.containsKey(keyword)){
            if(map.get(keyword) instanceof Date){
                Date date=(Date)map.get(keyword);
                SimpleDateFormat format=new SimpleDateFormat(dateFormat);
                String dataString=format.format(date);
                map.put(keyword,dataString);
            }

        }
    }
}
