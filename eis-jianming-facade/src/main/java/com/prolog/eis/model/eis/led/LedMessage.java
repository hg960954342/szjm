package com.prolog.eis.model.eis.led;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("led_message")
public class LedMessage {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("port_id")
	@ApiModelProperty("port口id")
	private int portId;
	
	@Column("read_state")
	@ApiModelProperty("是否读取 0未读 1已读")
	private int readState;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("state_str")
	@ApiModelProperty("状态")
	private String stateStr;
	
	@Column("message_type")
	@ApiModelProperty("信息类型 0正常 10报警 20异常")
	private int messageType;
	
	@Column("message")
	@ApiModelProperty("信息")
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}
