package com.prolog.eis.util;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.prolog.framework.utils.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class NameAndSimplePropertyPreFilter extends SimplePropertyPreFilter implements NameFilter, SerializeFilter {

    private Map<String,String> excepts=new HashMap<>();

    public NameAndSimplePropertyPreFilter(Map<String,String> excepts){
        this.excepts=excepts;
    }

    public NameAndSimplePropertyPreFilter() {
       super();
    }

    @Override
    public String process(Object source, String name, Object value) {
        if (name == null || name.length() == 0) {
            return name;
        }
        //设置默认值
        if(excepts.size()==0){
            excepts.put("data","data");
            excepts.put("size","size");
            excepts.put("details","details");
            excepts.put("messageID","MessageID");
        }
       if(excepts.containsKey(name)){
           return excepts.get(name);
       }
        String pascalName = name.toUpperCase();
        return pascalName;
    }
}
