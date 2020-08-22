package com.prolog.eis.service.login.impl;

import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

            String restjson = HttpUtils.post(url, json);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restjson);
            String data = helper.getString("data");
            PrologApiJsonHelper jwtData = PrologApiJsonHelper.createHelper(data);
            LoginWmsResponse.accessToken = jwtData.getString("access_token");
            LoginWmsResponse.expiresIn = jwtData.getString("expires_in");
            LoginWmsResponse.tokenType = jwtData.getString("token_type");
            LoginWmsResponse.refreshToken = jwtData.getString("refresh_token");

        } catch (Exception e) {
            FileLogHelper.WriteLog("WMSLogin","EIS->WMS [WMSInterface] 登录失败：[message]:"+e.toString());
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
            String restjson = HttpUtils.post(url, json);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restjson);
            String data = helper.getString("data");
            PrologApiJsonHelper jwtData = PrologApiJsonHelper.createHelper(data);
            LoginWmsResponse.accessToken = jwtData.getString("access_token");
            LoginWmsResponse.expiresIn = jwtData.getString("expires_in");
            LoginWmsResponse.tokenType = jwtData.getString("token_type");
            LoginWmsResponse.refreshToken = jwtData.getString("refresh_token");
        } catch (Exception e) {
            FileLogHelper.WriteLog("EIS->WMS [WMSInterface] 刷新token失败：[message]:"+e.toString());
        }

    }


}
