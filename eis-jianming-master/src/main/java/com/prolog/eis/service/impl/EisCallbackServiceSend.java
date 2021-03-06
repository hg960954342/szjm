package com.prolog.eis.service.impl;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EisCallbackServiceSend {

    @Value("${prolog.wms.url:}")
    private String wmsIp;
    @Value("${prolog.wms.port:}")
    private String wmsPort;
    @Autowired
    EisCallbackService eisCallbackService;

    @Autowired
    private WmsLoginService wmsLoginService;


    @Autowired
    private RestTemplate restTemplate;


    public void recall(RepeatReport repeatReport) {
        boolean isSucucess = false;
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;

            String url = repeatReport.getReportUrl();
            String json = repeatReport.getReportData();

            String restJson = restTemplate.postForObject(url, PrologHttpUtils.getWmsRequestEntity(json, token), JSONObject.class).toString();

            //发送回告
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            String message = helper.getString("message");
            if ("0".equals(helper.getString("stateCode"))) {
                //回告成功 删除
                isSucucess = true;
            } else {
                repeatReport.setMessage(LogServices.spliitString(message));
                isSucucess = false;
            }

            LogServices.logWms(url, json,message, restJson);
        } catch (Exception e) {
//            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败：" + e.getMessage();
//            LogServices.logSysBusiness(resultMsg);
            LogServices.logSys(e);
            isSucucess = false;
        }


        eisCallbackService.updateResport(isSucucess, repeatReport);
    }


}
