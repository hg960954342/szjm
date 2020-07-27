package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("wms_move_task")
public class WmsMoveTask {
	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;

	@Column("wh_no")
	@ApiModelProperty("實體倉庫，庫區代號(HA_WH)")
	private String whNo;

	@Column("area_no")
	@ApiModelProperty("實體倉庫，儲區代號(HAC_ASRS)")
	private String areaNo;
	
	@Column("bin_no")
	@ApiModelProperty("新的庫位編號")
	private String binNo;

	@Column("pallet_id")
	@ApiModelProperty("棧板ID")
	private String palletId;

	@Column("pallet_size")
	@ApiModelProperty("WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)")
	private String palletSize;

	@Column("finished")
	@ApiModelProperty("默认0 10进行中 90完成作業,91強制完成作業（设备单方面）,92強制取消 -1 异常")
	private int finished;
	
	@Column("report")
	@ApiModelProperty("默认0 0不需要上报 1需要上报")
	private int report;

	@Column("err_msg")
	@ApiModelProperty("異常說明")
	private String errMsg;

	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@Column("wms_datasource_type")
	@ApiModelProperty("wms数据源(release/beta)")
	private String wmsDatasourceType;

	public String getWmsDatasourceType() {
		return wmsDatasourceType;
	}

	public void setWmsDatasourceType(String wmsDatasourceType) {
		this.wmsDatasourceType = wmsDatasourceType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWhNo() {
		return whNo;
	}

	public void setWhNo(String whNo) {
		this.whNo = whNo;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getBinNo() {
		return binNo;
	}

	public void setBinNo(String binNo) {
		this.binNo = binNo;
	}

	public String getPalletId() {
		return palletId;
	}

	public void setPalletId(String palletId) {
		this.palletId = palletId;
	}

	public String getPalletSize() {
		return palletSize;
	}

	public void setPalletSize(String palletSize) {
		this.palletSize = palletSize;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public int getReport() {
		return report;
	}

	public void setReport(int report) {
		this.report = report;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
