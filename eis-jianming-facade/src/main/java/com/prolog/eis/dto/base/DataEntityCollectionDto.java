package com.prolog.eis.dto.base;

import java.util.List;
import java.util.Map;

public class DataEntityCollectionDto {

	public List<DataEntity> rows;
	
	private int totalCount;

	private Map<String, Object> customObjDic;
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<DataEntity> getRows() {
		return rows;
	}

	public void setRows(List<DataEntity> rows) {
		this.rows = rows;
	}

	public DataEntityCollectionDto() {
		super();
	}

	
	public Map<String, Object> getCustomObjDic() {
		return customObjDic;
	}

	public void setCustomObjDic(Map<String, Object> customObjDic) {
		this.customObjDic = customObjDic;
	}

	@Override
	public String toString() {
		return "DataEntityCollectionDto [rows=" + rows + ", totalCount=" + totalCount + "]";
	}
	
	
	

}
