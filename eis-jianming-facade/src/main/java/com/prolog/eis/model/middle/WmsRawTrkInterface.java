package com.prolog.eis.model.middle;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("WMS_RAW_TRK_INTERFACE")
public class WmsRawTrkInterface {
	@Column("TRK_DT")
	@ApiModelProperty("資料產生日期")
	private Date trkDt;

	@Column("GROUP_ID")
	@ApiModelProperty("任务组")
	private int groupId;

	@Column("COMMAND_NO")
	@ApiModelProperty("WMS中間介面KEY值")
	private String commandNo;

	@Column("WH_NO")
	@ApiModelProperty("實體倉庫，庫區代號(HA_WH)")
	private String whNo;

	@Column("AREA_NO")
	@ApiModelProperty("實體倉庫，儲區代號(HAC_ASRS)")
	private String areaNo;

	@Column("DEV_NO")
	@ApiModelProperty("作業站編號：工作站編號")
	private String devNo;

	@Column("BIN_NO")
	@ApiModelProperty("庫位編號預設帶：000000")
	private String binNo;

	@Column("EMERGE")
	@ApiModelProperty("緊急預設帶：0")
	private int emerge;

	@Column("IO")
	@ApiModelProperty("入庫：'I' 出庫：'O'")
	private String io;

	@Column("WMS_BACK")
	@ApiModelProperty("1:資料OK 	2:退出")
	private Integer wmsBack;

	@Column("WMS_ACTION")
	@ApiModelProperty("WMS 作業代號")
	private String wmsAction;

	@Column("WMS_SERIAL_NO")
	@ApiModelProperty("WMS 文件號")
	private String wmsSerialNo;

	@Column("WMS_USER")
	@ApiModelProperty("寫入該筆資料的使用者ID")
	private String wmsUser;

	@Column("WMS_FLAG")
	@ApiModelProperty("WMS是否處理該筆資料(N/Y) default:N")
	private String wmsFlag;

	@Column("PALLET_ID")
	@ApiModelProperty("棧板ID")
	private String palletId;

	@Column("PALLET_SIZE")
	@ApiModelProperty("WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)")
	private String palletSize;

	@Column("WEIGHT")
	@ApiModelProperty("重量")
	private Double weight;

	@Column("STATUS")
	@ApiModelProperty("預設帶：0資料轉換結果0:未轉換	1:OK 2:NG")
	private Integer status;

	@Column("FINISHED")
	@ApiModelProperty("90完成作業	91強制完成作業	92強制取消")
	private int finished;

//	@Column("RETURN")
//	@ApiModelProperty("0:預設值")
//	private int return;

	@Column("ERR_CODE")
	@ApiModelProperty("異常代碼")
	private String errCode;

	@Column(value = "ERR_MSG",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("異常說明")
	private String errMsg;

	@Column(value = "WMS_ERR_CODE",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("WMS異常代碼")
	private String wmsErrCode;

	@Column(value = "WMS_ERR_MSG",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("WMS異常說明")
	private String wmsErrMsg;

	@Column(value = "PRODUCT_ID",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("(@2.1)WMS需傳 入庫料號 作為判斷貨品規格之用途。")
	private String productId;

	@Column(value = "BOX_COUNT",jdbcType = Column.JdbcType_INTEGER)
	@ApiModelProperty("(@2.1)WMS需傳棧板箱數。")
	private Integer boxCount;

	@Column(value = "PORT",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("Port口編號")
	private String port;

	@Column(value = "MAT_TYPE",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("VMI    INX")
	private String matType;

	@Column(value = "STATIONS",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("叫料解包區")
	private String stations;
	
	@Column(value = "TASK_TYPE",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("NORMAL:一般作业  IQC:质检作业  PPBOX:空托盘")
	private String taskType;
	
	@Column(value = "VENDOR_CODE",jdbcType = Column.JdbcType_VARCHAR)
	@ApiModelProperty("厂商代码")
	private String vendorCode;

	public Date getTrkDt() {
		return trkDt;
	}

	public void setTrkDt(Date trkDt) {
		this.trkDt = trkDt;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getCommandNo() {
		return commandNo;
	}

	public void setCommandNo(String commandNo) {
		this.commandNo = commandNo;
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

	public String getDevNo() {
		return devNo;
	}

	public void setDevNo(String devNo) {
		this.devNo = devNo;
	}

	public String getBinNo() {
		return binNo;
	}

	public void setBinNo(String binNo) {
		this.binNo = binNo;
	}

	public int getEmerge() {
		return emerge;
	}

	public void setEmerge(int emerge) {
		this.emerge = emerge;
	}

	public String getIo() {
		return io;
	}

	public void setIo(String io) {
		this.io = io;
	}

	public Integer getWmsBack() {
		return wmsBack;
	}

	public void setWmsBack(Integer wmsBack) {
		this.wmsBack = wmsBack;
	}

	public String getWmsAction() {
		return wmsAction;
	}

	public void setWmsAction(String wmsAction) {
		this.wmsAction = wmsAction;
	}

	public String getWmsSerialNo() {
		return wmsSerialNo;
	}

	public void setWmsSerialNo(String wmsSerialNo) {
		this.wmsSerialNo = wmsSerialNo;
	}

	public String getWmsUser() {
		return wmsUser;
	}

	public void setWmsUser(String wmsUser) {
		this.wmsUser = wmsUser;
	}

	public String getWmsFlag() {
		return wmsFlag;
	}

	public void setWmsFlag(String wmsFlag) {
		this.wmsFlag = wmsFlag;
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
	

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getWmsErrCode() {
		return wmsErrCode;
	}

	public void setWmsErrCode(String wmsErrCode) {
		this.wmsErrCode = wmsErrCode;
	}

	public String getWmsErrMsg() {
		return wmsErrMsg;
	}

	public void setWmsErrMsg(String wmsErrMsg) {
		this.wmsErrMsg = wmsErrMsg;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(Integer boxCount) {
		this.boxCount = boxCount;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMatType() {
		return matType;
	}

	public void setMatType(String matType) {
		this.matType = matType;
	}

	public String getStations() {
		return stations;
	}

	public void setStations(String stations) {
		this.stations = stations;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
}
