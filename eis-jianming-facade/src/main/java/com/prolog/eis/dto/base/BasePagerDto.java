package com.prolog.eis.dto.base;

import java.util.List;

public class BasePagerDto {
	private String PkColumn;
	// 查询条件集合
	private List<QuerySqlConditionDto> conditions;
	// 排序方式集合
	private List<QuerySqlOrderDto> orders;
	private Object columns;
	// 当前页数
	private int pageIndex;
	// 页面总数
	private int pageCount;
	// 每一页显示记录数
	private int pageSize;
	// 总的记录条数
	private int totalCount;

	
	
	
	
	
	
	public List<QuerySqlOrderDto> getOrders() {
		return orders;
	}

	public void setOrders(List<QuerySqlOrderDto> orders) {
		this.orders = orders;
	}

	public Object getColumns() {
		return columns;
	}

	public void setColumns(Object columns) {
		this.columns = columns;
	}

	public List<QuerySqlConditionDto> getConditions() {
		return conditions;
	}

	public void setConditions(List<QuerySqlConditionDto> conditions) {
		this.conditions = conditions;
	}

	 

	public BasePagerDto() {
		super();
	}

	public String getPkColumn() {
		return PkColumn;
	}

	public void setPkColumn(String pkColumn) {
		PkColumn = pkColumn;
	}

	/**
	 * 通过构造函数 传入 总记录数 和 当前页
	 * 
	 * @param totalCount
	 * @param pageIndex
	 */
	public BasePagerDto(int totalCount, int pageIndex) {
		this.totalCount = totalCount;
		this.pageIndex = pageIndex;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	@SuppressWarnings("unused")
	private int startPages; // 开始位置，从0开始

	public int getStartPages() {
		return (pageIndex - 1) * pageSize;
	}

	public void setStartPages(int startPages) {
		this.startPages = startPages;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * 取得总页数，总页数=总记录数/每页显示数量
	 * 
	 * @return
	 */
	public int getPageCount() {
		pageCount = getTotalCount() / getPageSize();
		return (totalCount % pageSize == 0) ? pageCount : pageCount + 1;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
