package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("port_tems_info")
public class PortTemsInfo {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("port_info_id")
	@ApiModelProperty("外键PortInfo表Id")
	private int portInfoId;
	
	@Column("port_type")
	@ApiModelProperty("出库口类型 （1暂存位）")
	private int portType;
	
	@Column("task_type")
	@ApiModelProperty("任务类型 （1人工）")
	private int taskType;
	
	@Column("junction_port")
	@ApiModelProperty("出入口编号对应 eis sx_connection_rim entry_code")
	private String junctionPort;
	
	@Column("layer")
	@ApiModelProperty("层")
	private int layer;
	
	@Column("x")
	@ApiModelProperty("X")
	private int x;
	
	@Column("y")
	@ApiModelProperty("Y")
	private int y;
	
	@Column("port_lock")
	@ApiModelProperty("是否锁定 1锁定 2不锁定")
	private int portLock;
	
	@Column("task_lock")
	@ApiModelProperty("是否锁定 1锁定 2不锁定")
	private int taskLock;
	
	@Column("remarks")
	@ApiModelProperty("备注")
	private String remarks;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPortInfoId() {
		return portInfoId;
	}

	public void setPortInfoId(int portInfoId) {
		this.portInfoId = portInfoId;
	}

	public int getPortType() {
		return portType;
	}

	public void setPortType(int portType) {
		this.portType = portType;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getJunctionPort() {
		return junctionPort;
	}

	public void setJunctionPort(String junctionPort) {
		this.junctionPort = junctionPort;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPortLock() {
		return portLock;
	}

	public void setPortLock(int portLock) {
		this.portLock = portLock;
	}

	public int getTaskLock() {
		return taskLock;
	}

	public void setTaskLock(int taskLock) {
		this.taskLock = taskLock;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public PortTemsInfo(int id, int portInfoId, int portType, int taskType, String junctionPort, int layer, int x,
			int y, int portLock, int taskLock, String remarks) {
		super();
		this.id = id;
		this.portInfoId = portInfoId;
		this.portType = portType;
		this.taskType = taskType;
		this.junctionPort = junctionPort;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.portLock = portLock;
		this.taskLock = taskLock;
		this.remarks = remarks;
	}

	public PortTemsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
}
