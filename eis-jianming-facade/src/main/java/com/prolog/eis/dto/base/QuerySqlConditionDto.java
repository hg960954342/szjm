package com.prolog.eis.dto.base;

public class QuerySqlConditionDto  {
	public QuerySqlConditionType ConditionType;
 	public String column;
	public Object value;
	public Object value2;
	public Object values;
	 
	public QuerySqlConditionType getConditionType() {
		return ConditionType;
	}
	public void setConditionType(QuerySqlConditionType conditionType) {
		ConditionType = conditionType;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Object getValue2() {
		return value2;
	}
	public void setValue2(Object value2) {
		this.value2 = value2;
	}
	public Object getValues() {
		return values;
	}
	public void setValues(Object values) {
		this.values = values;
	}
	
	 public enum QuerySqlConditionType
	 {
	     EqualTo, //=
	     GreaterThan,// >
	     GreaterThanAndEqualTo,// >=
	     NotEqualTo,//!=
	     LessThan, //<
	     LessThanAndEqualTo, //<=
	     Match, //like 
	     MatchPrefix,// ??(匹配前缀？ like %XXX)
	     In, //in 
	     NotMatch, //not like
	     NotMatchPrefix, //?????(不匹配前缀？not like %XXX)
	     NotIn, //not in 
	     MatchSuffix,  //??(匹配后缀？ like XXX%)
	     NotMatchSuffix,//?????(不匹配后缀？not like XXX%)
	     MatchFullText, //like %xxx%
	     Custom //???? 自定义？
	 }
	
}


