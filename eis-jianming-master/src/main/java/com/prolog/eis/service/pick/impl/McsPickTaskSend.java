package com.prolog.eis.service.pick.impl;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.dto.pick.McsResultDto;
import com.prolog.eis.dto.pick.SendMcsPickTaskDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.util.PrologHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 * @author chengxudong
 * @description
 **/
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class McsPickTaskSend {


    @Autowired
    private RestTemplate restTemplate;
    @Value("${prolog.mcs.url:}")
    private String mcsUrl;

    @Value("${prolog.mcs.port:}")
    private String mcsPort;



    public McsResultDto send(SendMcsPickTaskDto sendMcsPickTaskDto){
        String postUrl = String.format("http://%s:%s%s", mcsUrl, mcsPort, "/Interface/AllocateRequest");
        String data=JSONObject.toJSONString(sendMcsPickTaskDto);
        McsResultDto  restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), McsResultDto.class);
        LogServices.log(postUrl,data,"", JSONObject.toJSONString(restJson));
        return restJson;
    }

}
