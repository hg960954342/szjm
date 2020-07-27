package com.prolog.eis.model.base;

import java.util.Map;

public class BaseTreeModel extends BaseModel{

	public int id;

    public int parentId;

    public int sortIndex;

    public String fullPath;

    public String objectName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public BaseTreeModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseTreeModel(Map<String, Object> datas) {
		super(datas);
		// TODO Auto-generated constructor stub
	}

	public BaseTreeModel(int id, int parentId, int sortIndex, String fullPath, String objectName) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.sortIndex = sortIndex;
		this.fullPath = fullPath;
		this.objectName = objectName;
	}

	@Override
	public String toString() {
		return "BaseTreeModel [id=" + id + ", parentId=" + parentId + ", sortIndex=" + sortIndex + ", fullPath="
				+ fullPath + ", objectName=" + objectName + "]";
	}

}
