package com.prolog.eis.dto.sxk;

import java.util.List;

public class AreaSortDto {

	private int sortIndex;	//排列优先级

	private List<Integer> types;		//对应的任务类型

	/**
	 * @return the sortIndex
	 */
	public int getSortIndex() {
		return sortIndex;
	}

	/**
	 * @param sortIndex the sortIndex to set
	 */
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	/**
	 * @return the types
	 */
	public List<Integer> getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public AreaSortDto(int sortIndex, List<Integer> types) {
		super();
		this.sortIndex = sortIndex;
		this.types = types;
	}

	public AreaSortDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
