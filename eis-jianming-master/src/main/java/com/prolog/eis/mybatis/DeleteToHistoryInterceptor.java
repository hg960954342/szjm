package com.prolog.eis.mybatis;

import lombok.SneakyThrows;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 拦截删除语句 并转历史记录表 需要匹配规则 原始类名+History
 * 只支持deleteById deleteByMap
 *
 * @author dubux
 */
@Component
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DeleteToHistoryInterceptor implements Interceptor {


    @Autowired
    BaseJdbcTemplate baseJdbcTemplate;

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object result = null;
        if (target instanceof Executor) {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];

            Object parameter = args[1];
            BoundSql boundSql = ms.getBoundSql(parameter);
            String commandName = ms.getSqlCommandType().name();
            //执行删除之前保存历史
            if ("DELETE".equals(commandName)) {
                Map map = (Map) parameter;
               if(map.containsKey("c")) {
                   Class classes=(Class)map.get("c");
                   String historyName = String.format("%s%s",classes.getName(),"History");
                   if (isPresent(historyName)) {
                       if(map.containsKey("id")){
                           Object id=map.get("id");
                           Map<String, Object> bean = baseJdbcTemplate.findById(id, classes);
                           Object dest = this.copyProperties(historyName, bean);
                           baseJdbcTemplate.save(dest);
                       }
                       if(map.containsKey("params")){
                           Map<String, Object> params=(Map<String, Object>)map.get("params");
                           List<Map<String,Object>> list = baseJdbcTemplate.findByMap(params,classes);
                           for (Map<String,Object> bean : list) {
                               Object dest = this.copyProperties(historyName, bean);
                               baseJdbcTemplate.save(dest);
                           }
                       }
                   }
               }




            }


            result = invocation.proceed();


        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    @SneakyThrows
    private Object copyProperties(String historyName, Map<String,Object> bean) {
        Object obj = Class.forName(historyName).newInstance();
        BeanMap beanMap = BeanMap.create(obj);
        beanMap.putAll(bean);
        return obj;
    }


    private boolean isPresent(String name) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }



}
