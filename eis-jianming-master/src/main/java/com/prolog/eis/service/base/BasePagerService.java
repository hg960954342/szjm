package com.prolog.eis.service.base;

import com.prolog.eis.dto.base.DataEntity;

import java.util.List;

public interface BasePagerService {

	/**
	 * huhao 2018.09.27 pm 16:16
	 * @param startPages
	 * @param pageSize
	 * @return
	 */
	public List<DataEntity> getPagers(String columns, String tableName, String conditions, String orders, int startRowNum,
			int endRowNum);
	
	/**
	 * huhao 2018.09.29 pm14:05
	 * @param tableName
	 */
	public int getTotalCount(String tableName,String conditions);
}
