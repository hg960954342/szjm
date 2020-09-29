package com.prolog.eis.controller;

import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.PrologHttpUtils;
import io.swagger.annotations.Api;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@Api(tags = "EIS转发到WMS接口")
@RequestMapping("/api/v1/router/wms")
public class RouterController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${prolog.wms.url:}")
    private String wmsIp;
    @Value("${prolog.wms.port:}")
    private String wmsPort;

    @Autowired
    private WmsLoginService wmsLoginService;

    @PostMapping("/{interfaceWms}")
    public String routerWms(@PathVariable String interfaceWms, @RequestBody String params) {
        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;
        String url = String.format("http://%s:%s/api/v1/%s", wmsIp, wmsPort, interfaceWms);
        return restTemplate.postForObject(url, PrologHttpUtils.getWmsRequestEntity(params, token), JSONObject.class).toString();


    }


}
