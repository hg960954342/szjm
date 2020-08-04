package com.prolog.eis.service.base.impl;

import com.prolog.eis.util.PrologApiJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {
	@Autowired
	private RestTemplate restTemplate;
	@Value("${prolog.eis.url:}")
	private String url;
	@Value("${prolog.eis.port:}")
	private String port;
	
	public void postData() throws Exception{
		for(int i=401 ;i <= 414 ;i++) {
			for(int j =1;j<=20;j++) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("zhant_no",i);
				map.put("station_no", j);
				//String data="";
				String postUrl = String.format("%s%s/%s", url,port,"openGAS");
				String data = PrologApiJsonHelper.toJson(map);
				String result = restTemplate.postForObject(postUrl, getRequestEntity(data), String.class);
				System.out.println(result);
			}
			List<String> list = new ArrayList<String>();
			for(int m=0 ;m<10000000;m++) {
				String a = "111111111111111";
				list.add(a);
				
			}
		}
	}
	
	private HttpEntity<String> getRequestEntity(String data){
		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("api-version", "1.0");
		requestHeaders.add("content-type", "application/json; charset=UTF-8");
		HttpEntity<String> requestEntity = new HttpEntity<String>(data, requestHeaders);
		return requestEntity;
	}
	
}
