package com.prolog.eis.model.middle;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("WMS_BA_BINSTA_R")
public class WmsBaBinstaR {
	@Column("WH_NO")
	@ApiModelProperty("實體倉庫，庫區代號(HA_WH)")
	private String whNo;
	
	@Column("AREA_NO")
	@ApiModelProperty("實體倉庫，儲區代號(HAC_ASRS)")
	private String areaNo;
	
	@Column("BIN_NO")
	@ApiModelProperty("實體倉庫，庫位編號")
	private String binNo;

	@Column("PALLET_ID")
	@ApiModelProperty("库位移库棧板ID")
	private String palletId;

	@Column("FINISHED")
	@ApiModelProperty("90完成作業,91強制完成作業,92強制取消")
	private int finished;

	@Column("ERR_CODE")
	@ApiModelProperty("異常代碼")
	private String errCode;

	@Column(value = "ERR_MSG",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("異常說明")
	private String errMsg;

	@Column("FLAG")
	@ApiModelProperty("N:未更新；Y已更新；E：更新異常")
	private String flag;

	@Column("CREAT_TIME")
	@ApiModelProperty("创建时间")
	private Date createTime;

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

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
