package com.prolog.eis.util;

import net.sf.json.JSONObject;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
	private static CloseableHttpClient client;
	static {
		 // 全局请求设置
	    RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
	    // 创建cookie store的本地实例
	    CookieStore cookieStore = new BasicCookieStore();
	    // 创建HttpClient上下文
	    HttpClientContext context = HttpClientContext.create();
	    context.setCookieStore(cookieStore);
	    // 创建一个HttpClient
	    client = HttpClients.custom().setDefaultRequestConfig(globalConfig).setDefaultCookieStore(cookieStore).build();
	   
	}
	@SuppressWarnings("unused")
	public static String post(String url ,String json) throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(url);
	    StringEntity entity = new StringEntity(json,"UTF-8");
	    httpPost.setEntity(entity);
	    httpPost.setHeader("Accept","application/json, text/javascript, */*; q=0.01");
	    httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
	    httpPost.setHeader("Accept-Encoding", "gzip, deflate");
	    httpPost.setHeader("Referer", "http://www.prolog-int.cn:3111/outLogin");
	    httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
	    httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
	    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
	    httpPost.setConfig(requestConfig);   
	    CloseableHttpResponse  response = client.execute(httpPost);
	    int state = response.getStatusLine().getStatusCode();
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
		    result.append(line);
		}
		return result.toString();
	}
	
	public static String get(String url ) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
	    HttpGet httpGet = new HttpGet(url);
	    httpGet.setHeader("Accept", "application/json");
	    httpGet.setHeader("Content-type", "application/json");
	    CloseableHttpResponse  response = client.execute(httpGet);
	    int state = response.getStatusLine().getStatusCode();
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
		    result.append(line);
		}
		Map<String,String> re = new HashMap<String,String>();
		re.put("state", ""+state);
		re.put("datas", result.toString());
		return JSONObject.fromObject(re).toString();
	}
	
	public static String postXML(String url,String xml){
		CloseableHttpClient client = null;
		CloseableHttpResponse resp = null;
		try{
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/xml; charset=UTF-8");
			client = HttpClients.createDefault();
			StringEntity entityParams = new StringEntity(xml,"utf-8");
			httpPost.setEntity(entityParams);
			client = HttpClients.createDefault();
			resp = client.execute(httpPost);
			String resultMsg = EntityUtils.toString(resp.getEntity(),"utf-8");
			return resultMsg;
		}catch (Exception e){
			e.printStackTrace();
			FileLogHelper.WriteLog("postXML", e.toString());
		}finally {
			try {
				if(client!=null){
					client.close();
				}
				if(resp != null){
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				FileLogHelper.WriteLog("postXML", e.toString());
			}
		}
		return null;
	}
}
