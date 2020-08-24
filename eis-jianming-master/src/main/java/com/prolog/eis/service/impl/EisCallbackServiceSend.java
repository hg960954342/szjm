package com.prolog.eis.service.impl;

import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EisCallbackServiceSend   {

    @Value("${prolog.wms.url:}")
    private String wmsIp;
    @Value("${prolog.wms.port:}")
    private String wmsPort;
    @Autowired
    EisCallbackService eisCallbackService;

    @Autowired
    private WmsLoginService wmsLoginService;




    public void recall(RepeatReport repeatReport) {
        boolean isSucucess= false;
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;

            String url = repeatReport.getReportUrl();
            String json = repeatReport.getReportData();



            //发送回告
            String restJson = HttpUtils.post(url, json, token);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            if ("0".equals(helper.getString("stateCode"))) {
                //回告成功 删除
                isSucucess= true;
            }else
            {
                repeatReport.setMessage(LogServices.spliitString(restJson));
                isSucucess= false;
            }

        } catch (IOException e) {
            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败：" + e.getMessage();
            LogServices.logSys(new RuntimeException(resultMsg));
             isSucucess= false;
        }


        eisCallbackService.updateResport(isSucucess,repeatReport);
    }



}
