package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("wms_outbound_task")
public class WmsOutboundTask {
	@Id
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("group_id")
	@ApiModelProperty("任务组，GROUP_ID > 0时，GROUP_ID相等的为同一组任务")
	private int groupId;

	@Column("command_no")
	@ApiModelProperty("WMS中間介面KEY值")
	private String commandNo;

	@Column("wms_push")
	@ApiModelProperty("是否wms下发，0不是，1是")
	private int wmsPush;

	@Column("wh_no")
	@ApiModelProperty("實體倉庫，庫區代號(HA_WH)")
	private String whNo;

	@Column("area_no")
	@ApiModelProperty("實體倉庫，儲區代號(HAC_ASRS)")
	private String areaNo;

	@Column("task_type")
	@ApiModelProperty("任务类型 （10一般作业 20质检作业 30空箱作业 40包材任务）")
	private Integer taskType;

	@Column("pallet_id")
	@ApiModelProperty("棧板ID")
	private String palletId;
	
	@Column("container_code")
	@ApiModelProperty("母托编号")
	private String containerCode;

	@Column("pallet_size")
	@ApiModelProperty("WMS記錄料號應收的棧板(P  BY 板入庫  C  BY箱入庫)")
	private String palletSize;
	
	@Column("emerge")
	@ApiModelProperty("緊急預設帶：0 數字大愈快出庫")
	private int emerge;

	@Column("outbound_time")
	@ApiModelProperty("出库时效 （什么时间点必须出库）")
	private Date outboundTime;

	@Column("stations")
	@ApiModelProperty("叫料解包区")
	private String stations;

	@Column("port_no")
	@ApiModelProperty("Port口編號")
	private String portNo;
	
	@Column("entry_code")
	@ApiModelProperty("接驳口编号")
	private String entryCode;
	
	@Column("finished")
	@ApiModelProperty("默认0 10进行中 90完成作業,91強制完成作業（设备单方面）,92強制取消 ，99空出庫-1 异常")
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
	
	@Column("end_time")
	@ApiModelProperty("结束时间")
	private Date endTime;

	@Column("lxk_exit")
	@ApiModelProperty("料箱库出口；0左，1右")
	private Integer lxkExit;

	@Column("wms_datasource_type")
	@ApiModelProperty("wms数据源(release/beta)")
	private String wmsDatasourceType;

	@Column("user_id")
	@ApiModelProperty("创建人")
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getWmsPush() {
		return wmsPush;
	}

	public void setWmsPush(int wmsPush) {
		this.wmsPush = wmsPush;
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

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
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

	public int getEmerge() {
		return emerge;
	}

	public void setEmerge(int emerge) {
		this.emerge = emerge;
	}

	public Date getOutboundTime() {
		return outboundTime;
	}

	public void setOutboundTime(Date outboundTime) {
		this.outboundTime = outboundTime;
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

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getLxkExit() {
		return lxkExit;
	}

	public void setLxkExit(Integer lxkExit) {
		this.lxkExit = lxkExit;
	}

	public String getWmsDatasourceType() {
		return wmsDatasourceType;
	}

	public void setWmsDatasourceType(String wmsDatasourceType) {
		this.wmsDatasourceType = wmsDatasourceType;
	}
}
