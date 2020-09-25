package com.prolog.eis.service.login.impl;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class WmsLoginServiceImpl implements WmsLoginService {

    @Value("${userCode}")
    private String userCode;

    @Value("${userPass}")
    private String userPass;

    @Value("${prolog.wms.url:}")
    private String wmsIp;
    @Value("${prolog.wms.port:}")
    private String wmsPort;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 登录获取 token
     * @return
     */
    @Override
    public void loginWms() {
        String url = String.format("http://%s:%s/api/v1/OAuth/token", wmsIp, wmsPort);
        Map<String,Object> map = new HashMap<>();
        map.put("userCode",userCode);
        map.put("userPass",userPass);
        map.put("accountCode","db_jianmin_wms");
        map.put("messageID",UUID.randomUUID().toString().replaceAll("-", ""));
        String json = null;
        try {
            json = PrologApiJsonHelper.toJson(map);

            LoginWmsResponse.getTokenTime = System.currentTimeMillis() / 1000;

            String restJson = restTemplate.postForObject(url, PrologHttpUtils.getRequestEntity(json), JSONObject.class).toString();
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            String data = helper.getString("data");
            String message = helper.getString("Message");
            PrologApiJsonHelper jwtData = PrologApiJsonHelper.createHelper(data);
            LoginWmsResponse.accessToken = jwtData.getString("access_token");
            LoginWmsResponse.expiresIn = jwtData.getString("expires_in");
            LoginWmsResponse.tokenType = jwtData.getString("token_type");
            LoginWmsResponse.refreshToken = jwtData.getString("refresh_token");

            LogServices.logWms(url,json,message,restJson);

        } catch (Exception e) {
           // String resultMsg = "EIS->WMS [WMSInterface] 登录失败：[message]:" + e.getMessage();
            LogServices.logSys(e);
        }
    }

    /**
     * 刷新token
     */
    public void flushToken(){
        String url = String.format("http://%s:%s/api/v1/OAuth/refresh", wmsIp, wmsPort);
        Map<String,Object> map = new HashMap<>();
        map.put("refreshToken",LoginWmsResponse.refreshToken);

        try {
            String json = PrologApiJsonHelper.toJson(map);
            String restJson = restTemplate.postForObject(url, PrologHttpUtils.getRequestEntity(json), JSONObject.class).toString();
             PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            String data = helper.getString("data");
            String message = helper.getString("message");
            PrologApiJsonHelper jwtData = PrologApiJsonHelper.createHelper(data);
            LoginWmsResponse.accessToken = jwtData.getString("access_token");
            LoginWmsResponse.expiresIn = jwtData.getString("expires_in");
            LoginWmsResponse.tokenType = jwtData.getString("token_type");
            LoginWmsResponse.refreshToken = jwtData.getString("refresh_token");

            LogServices.logWms(url,json,message,restJson);
        } catch (Exception e) {
           /* String resultMsg = "EIS->WMS [WMSInterface] 刷新token失败：[message]:" + e.getMessage();
            LogServices.logSysBusiness(resultMsg);*/
           LogServices.logSys(e);
        }

    }


}
