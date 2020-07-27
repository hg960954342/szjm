package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("zt_ckcontainer")
public class ZtckContainer {

	@Id
	@Column("container_code")
	@ApiModelProperty("母托盘号")
	private String containerCode;
	
	@Column("container_subcode")
	@ApiModelProperty("字托盘号")
	private String containerSubCode;
	
	@Column("stations")
	@ApiModelProperty("叫料解包区")
	private String stations;
	
	@Column("port_no")
	@ApiModelProperty("叫料解包区")
	private String portNo;
	
	@Column("entry_code")
	@ApiModelProperty("接驳口编号")
	private String entryCode;
	
	@Column("task_type")
	@ApiModelProperty("任务类型 10一般作业 20质检作业  30空托作业 70质检等待")
	private int taskType;
	
	@Column("task_status")
	@ApiModelProperty("任务类型 10进行中   20完成")
	private int taskStatus;
	
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
	
	@Column("factory_code")
	@ApiModelProperty("仓码")
	private String factoryCode;
	
	@Column("box_count")
	@ApiModelProperty("箱数")
	private String boxCount;
	
	@Column("mat_type")
	@ApiModelProperty("mat_type   VMI  海关  一楼  INX  群志 二三楼")
	private String matType;
	
	@Column("weight")
	@ApiModelProperty("托盘重量")
	private Double weight;
	
	@Column("detection")
	@ApiModelProperty("高度值")
	private int detection;

	@Column("hoist_no")
	@ApiModelProperty("提升机编号")
	private String hoistNo;
	
	@Column("targe_layer")
	@ApiModelProperty("提升机编号")
	private int targeLayer;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	public String getContainerCode() {
		return containerCode;
	}

	public void setContainerCode(String containerCode) {
		this.containerCode = containerCode;
	}

	public String getContainerSubCode() {
		return containerSubCode;
	}

	public void setContainerSubCode(String containerSubCode) {
		this.containerSubCode = containerSubCode;
	}

	public String getStations() {
		return stations;
	}

	public void setStations(String stations) {
		this.stations = stations;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
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

	public String getFactoryCode() {
		return factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	public String getBoxCount() {
		return boxCount;
	}

	public void setBoxCount(String boxCount) {
		this.boxCount = boxCount;
	}

	public String getMatType() {
		return matType;
	}

	public void setMatType(String matType) {
		this.matType = matType;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public int getDetection() {
		return detection;
	}

	public void setDetection(int detection) {
		this.detection = detection;
	}

	public String getHoistNo() {
		return hoistNo;
	}

	public void setHoistNo(String hoistNo) {
		this.hoistNo = hoistNo;
	}

	public int getTargeLayer() {
		return targeLayer;
	}

	public void setTargeLayer(int targeLayer) {
		this.targeLayer = targeLayer;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
