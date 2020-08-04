package com.prolog.eis.service.base;

import com.prolog.eis.model.base.SysParame;

import java.util.List;

public interface SysParameService {

	/**
	 * 获取系统参数
	 * @return
	 */
	List<SysParame> getSysParames();
	
	/**
	 * 设置某个系统参数
	 */
	void setSysParameValue(String key,String value) throws Exception;
}
