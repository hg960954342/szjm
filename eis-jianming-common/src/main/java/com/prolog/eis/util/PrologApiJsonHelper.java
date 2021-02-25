package com.prolog.eis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrologApiJsonHelper {

	// 对象格式的json
	private JSONObject jsonOb = null;

	private PrologApiJsonHelper() {

	}

	/**
	 * 将字符串序列化成json对象
	 * 
	 * @date 2018年9月4日 下午6:22:47
	 * @author dengss
	 * @param json
	 * @return
	 */
	public static PrologApiJsonHelper createHelper(String json) {
		PrologApiJsonHelper helper = new PrologApiJsonHelper();
		JSONObject objJson = JSONObject.parseObject(json);
		helper.jsonOb = objJson;
		return helper;
	}

	/**
	 * json对象序列化为字符串
	 * 
	 * @return 符合格式的json字符串
	 * @throws Exception
	 */
	public static String toJson(Object obj) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonData = objectMapper.writeValueAsString(obj);
		return jsonData;
	}

	/**
	 * 通过key取int类型的值
	 * 
	 * @date 2018年9月4日 下午6:24:10
	 * @author dengss
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return jsonOb.getInteger(key);
	}

	/**
	 * 通过key取List对象
	 * 
	 * @date 2018年10月28日 下午21:48
	 * @author chenbo
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getIntList(String key) throws Exception {
		JSONArray jsonArray = jsonOb.getJSONArray(key);
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < jsonArray.size(); i++) {
			Integer obj = jsonArray.getInteger(i);
			list.add(obj);
		}
		return list;
	}

	/**
	 * 通过key取String类型的值
	 * 
	 * @date 2018年9月4日 下午6:25:09
	 * @author dengss
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return jsonOb.getString(key);
	}

	/**
	 * 通过key取List对象
	 * 
	 * @date 2018年10月28日 下午21:48
	 * @author chenbo
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public List<String> getStringList(String key) throws Exception {
		JSONArray jsonArray = jsonOb.getJSONArray(key);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			String str = jsonArray.getString(i);
			list.add(str);
		}
		return list;
	}

	/**
	 * 通过key取Boolean类型的值
	 * 
	 * @date 2018年9月4日 下午6:25:35
	 * @author dengss
	 * @param key
	 * @return
	 */
	public Boolean getBoolean(String key) {
		return jsonOb.getBoolean(key);
	}

	/**
	 * 通过key取Double类型的值
	 * 
	 * @date 2018年9月4日 下午6:25:59
	 * @author dengss
	 * @param key
	 * @return
	 */
	public Double getDouble(String key) {
		return jsonOb.getDouble(key);
	}

	/**
	 * 通过key取Long类型的值
	 * 
	 * @date 2018年9月4日 下午6:26:19
	 * @author dengss
	 * @param key
	 * @return
	 */
	public Long getLong(String key) {
		return jsonOb.getLong(key);
	}

	/**
	 * 通过key返回对象
	 * 
	 * @date 2018年9月4日 下午6:26:39
	 * @author dengss
	 * @param key
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> T getObject(String key, Class<T> clazz) throws Exception {
		String jsonStr = jsonOb.getString(key);
		ObjectMapper objectMapper = new ObjectMapper();
		// 设置在反序列化时忽略在JSON字符串中存在，而在Java中不存在的属性
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		T obj = objectMapper.readValue(jsonStr, clazz);
		return obj;
	}
	
	/**
	 * 直接返回对象
	 * @date 2019年5月14日 上午9:11:14
	 * @author dengss
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> T getObject(Class<T> clazz) throws Exception {
	   ObjectMapper objectMapper = new ObjectMapper();
	   // 设置在反序列化时忽略在JSON字符串中存在，而在Java中不存在的属性
	   objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	   T obj = objectMapper.readValue(jsonOb.toString(), clazz);
	   return obj;
	}
	
	/**
	 * 返回List对象
	 * @date 2018年9月4日 下午6:27:04
	 * @author dengss
	 * @param key
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> getObjectList(Class<T> clazz) throws Exception {
		JSONArray jsonArray = jsonOb.getJSONArray(jsonOb.toString());
		List<T> objList = new ArrayList<T>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		for (Object object : jsonArray) {
			JSONObject tObj = (JSONObject) object;
			String str = tObj.toString();
			T obj = objectMapper.readValue(str, clazz);
			objList.add(obj);
		}
		return objList;
	}

	/**
	 * 通过key取List对象
	 * 
	 * @date 2018年9月4日 下午6:27:04
	 * @author dengss
	 * @param key
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T extends Object> List<T> getObjectList(String key, Class<T> clazz) throws Exception {
		JSONArray jsonArray = jsonOb.getJSONArray(key);
		List<T> objList = new ArrayList<T>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		for (Object object : jsonArray) {
			JSONObject tObj = (JSONObject) object;
			String str = tObj.toString();
			T obj = objectMapper.readValue(str, clazz);
			objList.add(obj);
		}
		return objList;
	}
	
	/**
	 * 返回List对象
	 * @date 2018年9月4日 下午6:27:04
	 * @author dengss
	 * @param key
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T extends Object> List<T> getArrayList(String json, Class<T> clazz) throws Exception {
		JSONArray jsonArray = JSONArray.parseArray(json);
		List<T> objList = new ArrayList<T>();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		for (Object object : jsonArray) {
			JSONObject tObj = (JSONObject) object;
			String str = tObj.toString();
			T obj = objectMapper.readValue(str, clazz);
			objList.add(obj);
		}
		return objList;
	}


	public Object getObjectValue(String key) {
		return jsonOb.get(key);
	}

	public String getBcrPointValue(String bcrPointValue,String containerCode) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		jsonObject.put("message", "操作成功");
		jsonObject.put("code", "200");
		JSONObject valueJson = new JSONObject();
		valueJson.put("bcrPointValue", bcrPointValue);
		valueJson.put("containerCode", containerCode);
		jsonObject.put("data", valueJson);
		return jsonObject.toString();
	}

	
	public String getCheckSizeBcrValue(String bcrPointValue, String isPass) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		jsonObject.put("message", "操作成功");
		jsonObject.put("code", "200");
		JSONObject valueJson = new JSONObject();
		valueJson.put("isPass", isPass);
		valueJson.put("bcrPointValue", bcrPointValue);
		jsonObject.put("data", valueJson);
		return jsonObject.toString();
	}

	public String setOutErr(String msg) throws IOException {
		JSONObject res = new JSONObject();
		res.put("success", "false");
		res.put("message", msg);
		res.put("code", "200");
		res.put("data", null);
		return res.toString();
	}
	
	public <T> String getMcsValue(T inBoundRequestResponse) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		jsonObject.put("message", "操作成功");
		jsonObject.put("code", "200");
		List<T> inBoundRequestResponses = new ArrayList<T>();
		inBoundRequestResponses.add(inBoundRequestResponse);
		jsonObject.put("data", inBoundRequestResponses);
		return jsonObject.toString();
	}
	
	public static <T> String getGcsValue(T response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ret", true);
		jsonObject.put("msg", "操作成功");
		jsonObject.put("code", "200");
		List<T> listResponse = new ArrayList<T>();
		listResponse.add(response);
		jsonObject.put("data", listResponse);
		return jsonObject.toString();
	}
	
	public static <T> String setOutGcsErr(String msg) throws IOException {
		JSONObject res = new JSONObject();
		res.put("ret", false);
		res.put("msg", msg);
		res.put("code", "200");
		res.put("data", null);
		return res.toString();
	}
	
	/**
	 * 从List<A> copy到List<B>
	 * @param list List<B>
	 * @param clazz B
	 * @return List<B>
	 */
	public static <T> List<T> copyList(List<?> list,Class<T> clazz){
		String oldOb = JSON.toJSONString(list);
		return JSON.parseArray(oldOb, clazz);
	}
	
	
}
