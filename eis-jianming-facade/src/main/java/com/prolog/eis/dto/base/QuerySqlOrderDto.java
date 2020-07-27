package com.prolog.eis.dto.base;

public class QuerySqlOrderDto {

	public String Column;
	public QuerySqlOrderType OrderType;

	public String getColumn() {
		return Column;
	}

	public void setColumn(String column) {
		Column = column;
	}

	public QuerySqlOrderType getOrderType() {
		return OrderType;
	}

	public void setOrderType(QuerySqlOrderType orderType) {
		OrderType = orderType;
	}

	public QuerySqlOrderDto() {
		super();
 	}
	

}

enum QuerySqlOrderType {
	ASC, DESC;
}
