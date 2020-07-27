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
	
	public static HttpEntity<String> getRequestTextPlainEntity(String data){
		// headers
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("api-version", "1.0");
		requestHeaders.add("content-type", "text/plain; charset=UTF-8");
		HttpEntity<String> requestEntity = new HttpEntity<String>(data, requestHeaders);
		return requestEntity;
	}
}
