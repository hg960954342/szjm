package com.prolog.eis.dto.eis.led;

import java.util.Date;

public class LedMessageDto {
	
	// 消息id
	private int id;
	
	// led Ip
	private String ledIp;
	
	// port口id
	private int portId;
	
	// 是否读取 0未读 1已读
	private int readState;
	
	// 创建时间
	private Date createTime;
	
	// 状态
	private String stateStr;
	
	// 信息类型 0正常 10报警 20异常
	private int messageType;
	
	// 信息
	private String message;
	
	// led尺寸
	private String ledSize;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLedIp() {
		return ledIp;
	}

	public void setLedIp(String ledIp) {
		this.ledIp = ledIp;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public int getReadState() {
		return readState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLedSize() {
		return ledSize;
	}

	public void setLedSize(String ledSize) {
		this.ledSize = ledSize;
	}
	
	
}
