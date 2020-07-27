package com.prolog.eis.service;

import java.util.List;

import com.prolog.eis.model.eis.ZtContainerMsg;

public interface ZtContainerMsgService {

	void saveAndUpdate(String containerNo,String containerSubCode,String portNo,String entryCode,String errorMsg);
	
	String getContainNoErrorMsg(String containerNo,String portNo);
	
	/**
	 * 获取指定地方的异常消息
	 * @param position
	 * @return
	 */
	List<ZtContainerMsg> getZtContainerMsgList(String position);
}
