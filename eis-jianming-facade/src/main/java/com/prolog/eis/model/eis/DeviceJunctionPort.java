package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("device_junction_port")
public class DeviceJunctionPort {

	@Id
	@Column("device_no")
	@ApiModelProperty("主键")
	private String deviceNo;
	
	@Column("entry_code")
	@ApiModelProperty("出入口编号对应 eis sx_connection_rim entry_code")
	private String entryCode;
	
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
	
	@Column("position")
	@ApiModelProperty("位置 1 23楼需要发mcs前进指令的任务  0其他")
	private int position;

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public DeviceJunctionPort(String deviceNo, String entryCode, int layer, int x, int y, int portLock, int position) {
		super();
		this.deviceNo = deviceNo;
		this.entryCode = entryCode;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.portLock = portLock;
		this.position = position;
	}

	public DeviceJunctionPort() {
		super();
		// TODO Auto-generated constructor stub
	}
}
