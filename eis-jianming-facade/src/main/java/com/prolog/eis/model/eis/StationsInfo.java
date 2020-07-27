package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("stations_info")
public class StationsInfo {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("wms_station_no")
	@ApiModelProperty("wms解包区号")
	private String wmsStationNo;
	
	@Column("remark")
	@ApiModelProperty("备注")
	private String remark;
	
	@Column("sort_index")
	@ApiModelProperty("排序值")
	private int sortIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWmsStationNo() {
		return wmsStationNo;
	}

	public void setWmsStationNo(String wmsStationNo) {
		this.wmsStationNo = wmsStationNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
	
	public StationsInfo(int id, String wmsStationNo, String remark, int sortIndex) {
		super();
		this.id = id;
		this.wmsStationNo = wmsStationNo;
		this.remark = remark;
		this.sortIndex = sortIndex;
	}

	public StationsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
