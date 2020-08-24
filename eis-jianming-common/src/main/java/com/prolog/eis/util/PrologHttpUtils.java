package com.prolog.eis.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class PrologHttpUtils {

	public static HttpEntity<String> getRequestEntity(String data){
		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("api-version", "1.0");
		requestHeaders.add("content-type", "application/json; charset=UTF-8");
		HttpEntity<String> requestEntity = new HttpEntity<String>(data, requestHeaders);
		return requestEntity;
	}

	public static HttpEntity<String> getWmsRequestEntity(String json,String token){
		HttpHeaders requestHeaders = new HttpHeaders();

 		requestHeaders.add("Accept","application/json, text/javascript, */*; q=0.01");
		requestHeaders.add("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestHeaders.add("Accept-Encoding", "gzip, deflate");
		requestHeaders.add("Referer", "http://www.prolog-int.cn:3111/outLogin");
		requestHeaders.add("Content-Type", "application/json; charset=UTF-8");
		requestHeaders.add("X-Requested-With", "XMLHttpRequest");
		requestHeaders.add("Authorization","Bearer"+"\n\r"+token);
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, requestHeaders);
		return requestEntity;
	}

	public static HttpEntity<String> getWmsRequestEntity(String json){
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Accept","application/json, text/javascript, */*; q=0.01");
		requestHeaders.add("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestHeaders.add("Accept-Encoding", "gzip, deflate");
		requestHeaders.add("Referer", "http://www.prolog-int.cn:3111/outLogin");
		requestHeaders.add("Content-Type", "application/json; charset=UTF-8");
		requestHeaders.add("X-Requested-With", "XMLHttpRequest");
		HttpEntity<String> requestEntity = new HttpEntity<String>(json, requestHeaders);
		return requestEntity;
	}
	
	public static HttpEntity<String> getRequestTextPlainEntity(String data){
		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("api-version", "1.0");
		requestHeaders.add("content-type", "text/plain; charset=UTF-8");
		HttpEntity<String> requestEntity = new HttpEntity<String>(data, requestHeaders);
		return requestEntity;
	}
}
