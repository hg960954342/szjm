package com.prolog.eis.util;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class NameAndSimplePropertyPreFilter extends SimplePropertyPreFilter implements NameFilter, SerializeFilter {
    public String process(Object source, String name, Object value) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.equals("messageID")){
            return name;
        }
        String pascalName = name.toLowerCase();
        return pascalName;
    }
}