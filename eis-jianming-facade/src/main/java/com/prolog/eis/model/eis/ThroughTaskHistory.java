package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("through_task_history")
public class ThroughTaskHistory {

	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("pallet_id")
	@ApiModelProperty("棧板ID")
	private String palletId;

	@Column("container_code")
	@ApiModelProperty("母托盘编号")
	private String containerCode;
	
	@Column("materiel_no")
	@ApiModelProperty("料号")
	private String materielNo;

	@Column("factory_no")
	@ApiModelProperty("物料厂商")
	private String factoryNo;

	@Column("materiel_type")
	@ApiModelProperty("物料类别")
	private String materielType;

	@Column("materiel_name")
	@ApiModelProperty("物料名称")
	private String materielName;
	
	@Column("start_stations")
	@ApiModelProperty("起始叫料解包区")
	private String startStations;
	
	@Column("start_port")
	@ApiModelProperty("起始Port口")
	private String startPort;
	
	@Column("end_stations")
	@ApiModelProperty("结束叫料解包区")
	private String endStations;
	
	@Column("end_port")
	@ApiModelProperty("结束Port口")
	private String endPort;
	
	@Column("finished")
	@ApiModelProperty("默认0 10进行中  20进入提升机 50进入到暂存位 90完成作業,91強制完成作業（设备单方面）,92強制取消 -1 异常")
	private int finished;
	
	@Column("err_msg")
	@ApiModelProperty("異常說明")
	private String errMsg;

	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPalletId() {
		return palletId;
	}

	public void setPalletId(String palletId) {
		this.palletId = palletId;
	}

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getMaterielNo() {
		return materielNo;
	}

	public void setMaterielNo(String materielNo) {
		this.materielNo = materielNo;
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

	public String getStartStations() {
		return startStations;
	}

	public void setStartStations(String startStations) {
		this.startStations = startStations;
	}

	public String getStartPort() {
		return startPort;
	}

	public void setStartPort(String startPort) {
		this.startPort = startPort;
	}

	public String getEndStations() {
		return endStations;
	}

	public void setEndStations(String endStations) {
		this.endStations = endStations;
	}

	public String getEndPort() {
		return endPort;
	}

	public void setEndPort(String endPort) {
		this.endPort = endPort;
	}

	public int getFinished() {
		return finished;
	}

	public void setFinished(int finished) {
		this.finished = finished;
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
