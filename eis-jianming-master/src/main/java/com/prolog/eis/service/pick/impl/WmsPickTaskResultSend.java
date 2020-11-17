package com.prolog.eis.service.pick.impl;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.dto.pick.WmsPickResultDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author chengxudong
 * @description
 **/
@Component
public class WmsPickTaskResultSend {
    @Value("${prolog.wms.url:}")
    private String wmsIp;
    @Value("${prolog.wms.port:}")
    private String wmsPort;

    @Autowired
    private WmsLoginService wmsLoginService;
    @Autowired
    private RestTemplate restTemplate;

    public String sendResultTask(WmsPickResultDto wmsPickResultDto){

        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;
        //http://192.168.30.14:8091/api/v1/StockMove/StationPull
        String url = String.format("http://%s:%s/api/v1/StockMove/StationPull", wmsIp, wmsPort);
        String json= JSONObject.toJSONString(wmsPickResultDto);
        String restJson = restTemplate.postForObject(url, PrologHttpUtils.getWmsRequestEntity(json, token), net.sf.json.JSONObject.class).toString();
        //发送回告
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
        String message = helper.getString("message");
        LogServices.logWms(url, json,message, restJson);
        return restJson;

    }
}
