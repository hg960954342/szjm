package com.prolog.eis.model.mcs;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("mcs_task")
public class MCSTask {

	@Id
	@ApiModelProperty("主键")
	private int id;		//主键
	
	/**
	 * 规则HHmmss+四位流水码(1-9999)
	 */
	@Column("task_id")
	private String taskId;
	/**
	 * 任务类型：1：入库:2：出库、3.跨层
	 */
	@Column("type")
	private int type;
	
	/**
	 * 库编号
	 */
	@Column("bank_id")
	private int bankId;
	/**
	 * 母托盘编号
	 */
	@Column("container_no")
	private String containerNo;
	/**
	 * 请求位置:原坐标
	 */
	@Column("address")
	private String address;
	/**
	 * 目的位置：目的坐标
	 */
	@Column("target")
	private String target;
	/**
	 * 入库重量
	 */
	@Column("weight")
	private String weight;
	/**
	 * 任务优先级,0-99,0优先级最大
	 */
	@Column("priority")
	private String priority;
	
	/**
	 * 任务状态 0- 正常 1-异常
	 */
	@Column("status")
	private int status;
	
	/**
	 * 任务状态(1.完成、2.失败)
	 */
	@Column("task_state")
	private int taskState;
	
	/**
	 * 发送次数
	 */
	@Column("send_count")
	private int sendCount;	
	
	@Column("err_msg")
	private String errMsg;
	
	@Column("create_time")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
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
