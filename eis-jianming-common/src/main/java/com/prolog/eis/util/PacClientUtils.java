package com.prolog.eis.util;

import com.taobao.pac.sdk.cp.PacClient;
import com.taobao.pac.sdk.cp.SendSysParams;

/**
 * 调用菜鸟接口
 * @author Dss
 * @date 2019年4月28日 下午3:47:25
 */
public class PacClientUtils {
	private static PacClient pc;
	private static SendSysParams ssp;
	private PacClientUtils() {
	}
	/**
	 * 单例模式获取调用阿里的接口
	 * @return
	 */
	public static PacClient getPacClient() {
		if(pc == null ) {
			 //线上环境访问地址
		    //PacClient client = new PacClient("puluoge","puluoge","http://link.cainiao.com/gateway/link.do");
			// 日常环境访问地址
		    PacClient client = new PacClient("puluoge","puluoge","https://linkdaily.tbsandbox.com/gateway/link.do");
		    // 预发环境访问地址
		    //PacClient client = new PacClient("puluoge","puluoge","https://prelink.cainiao.com/gateway/link.do");
			client.setSockTimeout(20000);
	        client.setFileSockTimeout (50000);
	        client.setConnectTimeout (30000);
	        pc =client;
		}
		return pc;
	}

	public static SendSysParams getSendSysParams() {
		if(ssp == null) {
			SendSysParams param = new SendSysParams();
			param.setFromCode("puluoge");
			param.setToCode("puluoge");
			ssp =param;
		}
		return ssp;
	}
}
