package com.prolog.eis.dto.base;

import java.util.Map;

public class DataEntity {

	private Map<String, Object> datas;

	public Map<String, Object> getDatas() {
		return datas;
	}

	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}

	public DataEntity() {
		super();
	}

	@Override
	public String toString() {
		return "DataEntity [datas=" + datas + "]";
	}
	
	
	
	

}
