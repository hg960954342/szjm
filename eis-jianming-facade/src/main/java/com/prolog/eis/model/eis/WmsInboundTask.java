package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("wms_inbound_task")
public class WmsInboundTask {

	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;

	@Column("command_no")
	@ApiModelProperty("WMS中間介面KEY值")
	private String commandNo;

	@Column("wms_push")
	@ApiModelProperty("是否wms下发，0不是，1是")
	private int wmsPush;
	
	@Column("detection")
	@ApiModelProperty("高度值")
	private int detection;

	@Column("wh_no")
	@ApiModelProperty("實體倉庫，庫區代號(HA_WH)")
	private String whNo;

	@Column("area_no")
	@ApiModelProperty("實體倉庫，儲區代號(HAC_ASRS)")
	private String areaNo;

	@Column("task_type")
	@ApiModelProperty("任务类型 10一般作业 20质检作业  30空托作业 40 包材作业")
	private int taskType;

	@Column("pallet_id")
	@ApiModelProperty("棧板ID")
	private String palletId;

	@Column("container_code")
	@ApiModelProperty("母托盘编号")
	private String containerCode;

	@Column("pallet_size")
	@ApiModelProperty("WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)")
	private String palletSize;

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
	@ApiModelProperty("VMI  海关  一楼  INX  群志 二三楼")
	private String matType;

	@Column("weight")
	@ApiModelProperty("重量")
	private Double weight;

	@Column("stations")
	@ApiModelProperty("叫料解包区")
	private String stations;

	@Column("port_no")
	@ApiModelProperty("入库口")
	private String portNo;
	
	@Column("junction_port")
	@ApiModelProperty("接駁口")
	private String junctionPort;

	@Column("finished")
	@ApiModelProperty("默认0 10进行中  20进入提升机 50进入到暂存位 90完成作業,91強制完成作業（设备单方面）,92強制取消 -1 异常")
	private int finished;

	@Column("report")
	@ApiModelProperty("默认0 0不需要上报 1需要上报")
	private int report;

	@Column("bin_no")
	@ApiModelProperty("入庫的庫位編號")
	private String binNo;
	
	@Column("in_date")
	@ApiModelProperty("Wms批次号")
	private String inDate;
	
	@Column("shf_sd")
	@ApiModelProperty("存储位深度")
	private String shfSd;

	@Column("err_msg")
	@ApiModelProperty("異常說明")
	private String errMsg;

	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("end_time")
	@ApiModelProperty("结束时间")
	private Date endTime;

	@Column("wms_datasource_type")
	@ApiModelProperty("wms数据源(release/beta)")
	private String wmsDatasourceType;

	@Column("user_id")
	@ApiModelProperty("创建人")
	private String userId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCommandNo() {
		return commandNo;
	}

	public void setCommandNo(String commandNo) {
		this.commandNo = commandNo;
	}

	public int getWmsPush() {
		return wmsPush;
	}

	public void setWmsPush(int wmsPush) {
		this.wmsPush = wmsPush;
	}

	public int getDetection() {
		return detection;
	}

	public void setDetection(int detection) {
		this.detection = detection;
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

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
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

	public String getPalletSize() {
		return palletSize;
	}

	public void setPalletSize(String palletSize) {
		this.palletSize = palletSize;
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

	public String getJunctionPort() {
		return junctionPort;
	}

	public void setJunctionPort(String junctionPort) {
		this.junctionPort = junctionPort;
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

	public String getBinNo() {
		return binNo;
	}

	public void setBinNo(String binNo) {
		this.binNo = binNo;
	}

	public String getInDate() {
		return inDate;
	}

	public void setInDate(String inDate) {
		this.inDate = inDate;
	}

	public String getShfSd() {
		return shfSd;
	}

	public void setShfSd(String shfSd) {
		this.shfSd = shfSd;
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

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getWmsDatasourceType() {
		return wmsDatasourceType;
	}

	public void setWmsDatasourceType(String wmsDatasourceType) {
		this.wmsDatasourceType = wmsDatasourceType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
