package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.service.rcs.AgvCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgvCallbackServiceImpl implements AgvCallbackService {


    @Autowired
    private final Map<String, AgvMethod> strategyMap = new ConcurrentHashMap<>();

    //agv 回告 method ：star : 任务开始、outbin : 走出储位、end : 任务结束
    @Override
    public void agvCallback(String taskCode, String method) {
        AgvMethod agvMethod = strategyMap.get(String.format("%sMethod", method));
        if (agvMethod != null) {
            agvMethod.doAction(taskCode, method);
        }




    }
}



