package com.prolog.eis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.util.PrologHttpUtils;
import org.junit.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TestMain {

    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(60000);
        httpRequestFactory.setReadTimeout(60000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        return restTemplate;
    }



@Test
public void moveAgv(){



String   taskCode = UUID.randomUUID().toString().replaceAll("-", "");
String containerCode="801111";



String startPosition="060080AB054000"; //出库口  endPosition
String  endPosition="054320AB048300";  //startPosition

String taskTyp="F01";
String reqCode=Math.random()*10+"";
String priority="5";

    JSONObject jsonObject = new JSONObject();
    jsonObject.put("reqCode", reqCode);
    jsonObject.put("reqTime", (new SimpleDateFormat("yyyy MM dd HH:mm:ss")).format(new Date()));
    jsonObject.put("clientCode", "");
    jsonObject.put("tokenCode", "");
    jsonObject.put("interfaceName", "genAgvSchedulingTask");

    jsonObject.put("taskTyp", taskTyp);
    jsonObject.put("wbCode", "");

    JSONArray jsonArray = new JSONArray();

    JSONObject startPositionJson = new JSONObject();
    startPositionJson.put("positionCode", startPosition);
    startPositionJson.put("type", "00");
    jsonArray.add(startPositionJson);

    JSONObject endPositionJson = new JSONObject();
    endPositionJson.put("positionCode", endPosition);
    endPositionJson.put("type", "00");

    jsonArray.add(endPositionJson);

    jsonObject.put("positionCodePath", jsonArray);
    //盲举全部不传货架号
    jsonObject.put("podCode", "");
    jsonObject.put("podDir", "");
    jsonObject.put("priority", priority);
    jsonObject.put("agvCode", "");
    jsonObject.put("taskCode", reqCode);
    jsonObject.put("data", "");

    String data = jsonObject.toString();

    //String msg = "EIS->RCS [RCSInterface] 请求JSON：[message]:" + data;
    //FileLogHelper.WriteLog("RCSRequest", msg);

    String postUrl = String.format("http://%s:%s/cms/services/rest/hikRpcService/genAgvSchedulingTask", "10.5.90.211", "81");
    String result = restTemplate().postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);

    String resultMsg = "EIS->RCS [RCSInterface] 返回JSON：[message]:" + result;
    System.out.println(resultMsg);

}

}
