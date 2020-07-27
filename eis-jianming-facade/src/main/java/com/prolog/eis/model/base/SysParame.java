package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("SYS_PARAME")
public class SysParame {

	@Id
	@Column("PARAME_NO")
	@ApiModelProperty("参数编号")
	private String parameNo;
	
	@Column("PARAME_VALUE")
	@ApiModelProperty("参数含义")
	private String parameValue;
	
	@Column("PARAME_TYPE")
	@ApiModelProperty("参数类型")
	private int parameType;
	
	@Column("IS_READ_ONLY")
	@ApiModelProperty("是否只读 0：可修改  1：只读")
	private int isReadOnly;
	
	@Column("VISIBILITY")
	@ApiModelProperty("是否前台展示 0：不展示  1：展示")
	private int visibility;
	
	@Column("DEFAULT_VALUE")
	@ApiModelProperty("默认值")
	private String defaultValue;

	@Column("REMARK")
	@ApiModelProperty("备注")
	private String remark;
	
	@Column("SORTINDEX")
	@ApiModelProperty("排序索引")
	private int sortindex;

	public String getParameNo() {
		return parameNo;
	}

	public void setParameNo(String parameNo) {
		this.parameNo = parameNo;
	}

	public String getParameValue() {
		return parameValue;
	}

	public void setParameValue(String parameValue) {
		this.parameValue = parameValue;
	}

	public int getParameType() {
		return parameType;
	}

	public void setParameType(int parameType) {
		this.parameType = parameType;
	}

	public int getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(int isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSortindex() {
		return sortindex;
	}

	public void setSortindex(int sortindex) {
		this.sortindex = sortindex;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = prime * result + isReadOnly;
		result = prime * result + ((parameNo == null) ? 0 : parameNo.hashCode());
		result = prime * result + parameType;
		result = prime * result + ((parameValue == null) ? 0 : parameValue.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + sortindex;
		result = prime * result + visibility;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SysParame other = (SysParame) obj;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (isReadOnly != other.isReadOnly)
			return false;
		if (parameNo == null) {
			if (other.parameNo != null)
				return false;
		} else if (!parameNo.equals(other.parameNo))
			return false;
		if (parameType != other.parameType)
			return false;
		if (parameValue == null) {
			if (other.parameValue != null)
				return false;
		} else if (!parameValue.equals(other.parameValue))
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (sortindex != other.sortindex)
			return false;
		if (visibility != other.visibility)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SysParame [parameNo=" + parameNo + ", parameValue=" + parameValue + ", parameType=" + parameType
				+ ", isReadOnly=" + isReadOnly + ", visibility=" + visibility + ", defaultValue=" + defaultValue
				+ ", remark=" + remark + ", sortindex=" + sortindex + "]";
	}

	public SysParame(String parameNo, String parameValue, int parameType, int isReadOnly, int visibility,
			String defaultValue, String remark, int sortindex) {
		super();
		this.parameNo = parameNo;
		this.parameValue = parameValue;
		this.parameType = parameType;
		this.isReadOnly = isReadOnly;
		this.visibility = visibility;
		this.defaultValue = defaultValue;
		this.remark = remark;
		this.sortindex = sortindex;
	}

	public SysParame() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
