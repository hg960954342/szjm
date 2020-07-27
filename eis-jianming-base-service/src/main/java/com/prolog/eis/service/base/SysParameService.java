package com.prolog.eis.service.base;

import java.util.List;

import com.prolog.eis.model.base.SysParame;

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
