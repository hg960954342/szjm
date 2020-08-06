package com.prolog.eis.service.login.impl;

import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologMd5Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WmsLoginServiceImpl implements WmsLoginService {

    @Value("${userCode}")
    private String userCode;

    @Value("${userPass}")
    private String userPass;

    /**
     * 登录获取 token
     * @return
     */
    @Override
    public void loginWms() {
        String url= "http://localhost:8091/api/v1/OAuth/token";
        Map<String,Object> map = new HashMap<>();
        map.put("userCode",userCode);
        String userPassMd5 = PrologMd5Util.md5(userPass);
        map.put("userPass",userPassMd5);
        String json = null;
        try {
            json = PrologApiJsonHelper.toJson(map);
            String restjson = HttpUtils.post(url, json);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restjson);
            String data = helper.getString("data");
            PrologApiJsonHelper jwtData = PrologApiJsonHelper.createHelper(data);
            LoginWmsResponse.accessToken = jwtData.getString("access_token");
            LoginWmsResponse.expiresIn = jwtData.getString("expires_in");
            LoginWmsResponse.tokenType = jwtData.getString("token_type");
            LoginWmsResponse.refreshToken = jwtData.getString("refresh_token");

        } catch (Exception e) {
            FileLogHelper.WriteLog("EIS->WMS [WMSInterface] 登录失败：[message]:"+e.toString());
        }
    }

    /**
     * 刷新token
     */
    public void flushToken(){
        String url= "http://localhost:8091/api/v1/OAuth/refresh";
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
