package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("USERDEPT")
public class UserDept {

	@Id
	@AutoKey(sequence = "USERDEPT_SEQ",type = AutoKey.TYPE_IDENTITY)
	@ApiModelProperty("部门id")
	private int id;
	
	@ApiModelProperty("部门名称")
	private String objectName;

	@ApiModelProperty("父部门id")
	private int parentId;
	
	@ApiModelProperty("部门全路径")
	private String fullPath;
	
	@ApiModelProperty("排序索引")
	private int sortIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public UserDept() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDept(int id, String objectName, int parentId, String fullPath, int sortIndex) {
		super();
		this.id = id;
		this.objectName = objectName;
		this.parentId = parentId;
		this.fullPath = fullPath;
		this.sortIndex = sortIndex;
	}

	@Override
	public String toString() {
		return "UserDept [id=" + id + ", objectName=" + objectName + ", parentId=" + parentId + ", fullPath=" + fullPath
				+ ", sortIndex=" + sortIndex + "]";
	}
	
}
