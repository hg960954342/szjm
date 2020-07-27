package com.prolog.eis.model.base;

import java.util.Map;

import com.prolog.framework.core.annotation.Ignore;

public class BaseModel {
	
	@Ignore
	private Map<String,Object> datas;

	public Map<String, Object> getDatas() {
		return datas;
	}

	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}

	public BaseModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseModel(Map<String, Object> datas) {
		super();
		this.datas = datas;
	}

	@Override
	public String toString() {
		return "BaseModel [datas=" + datas + "]";
	}
	
}
