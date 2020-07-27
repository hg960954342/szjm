package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("WMS_CALLCAR_TASK_HISTORY")
public class WmsCallCarTaskHistory {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	private int id;
	
	@Column("WH_NO")
	@ApiModelProperty("實體倉庫，庫區代號(HA_WH)")
	private String whNo;
	
	@Column("AREA_NO")
	@ApiModelProperty("實體倉庫，儲區代號(HAC_ASRS)")
	private String areaNo;
	
	@Column("PALLET_ID")
	@ApiModelProperty("棧板ID")
	private String palletId;
	
	@Column("PRODUCT_ID")
	@ApiModelProperty("料號")
	private String productId;
	
	@Column("FACTORY_NO")
	@ApiModelProperty("物料厂商")
	private String factoryNo;
	
	@Column("MATERIEL_TYPE")
	@ApiModelProperty("物料类别")
	private String materielType;
	
	@Column("MATERIEL_NAME")
	@ApiModelProperty("物料类别")
	private String materielName;
	
	@Column("IO")
	@ApiModelProperty("I 需要派车入库   O 需要派车接走")
	private String io;
	
	@Column("STATIONS")
	@ApiModelProperty("叫料解包區")
	private String stations;
	
	@Column("PORT")
	@ApiModelProperty("Port口編號")
	private String port;
	
	@Column("FINISHED")
	@ApiModelProperty("默认0  90完成作業,91強制完成作業（设备单方面）,92強制取消 ")
	private int finished;
	
	@Column("ERR_CODE")
	@ApiModelProperty("异常代码 ")
	private int errCode;
	
	@Column("ERR_MSG")
	@ApiModelProperty("异常消息")
	private String errMsg;
	
	@Column("CREAT_TIME")
	@ApiModelProperty("创建时间")
	private Date creatTime;

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

	public String getPalletId() {
		return palletId;
	}

	public void setPalletId(String palletId) {
		this.palletId = palletId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getFactoryNo() {
		return factoryNo;
	}

	public void setFactoryNo(String factoryNo) {
		this.factoryNo = factoryNo;
	}

	public String getMaterielType() {
		return materielType;
	}

	public void setMaterielType(String materielType) {
		this.materielType = materielType;
	}

	public String getMaterielName() {
		return materielName;
	}

	public void setMaterielName(String materielName) {
		this.materielName = materielName;
	}

	public String getIo() {
		return io;
	}

	public void setIo(String io) {
		this.io = io;
	}

	public String getStations() {
		return stations;
	}

	public void setStations(String stations) {
		this.stations = stations;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
}
