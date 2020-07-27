package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("zt_container_msg")
public class ZtContainerMsg {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;

	@Column("container_code")
	@ApiModelProperty("母托盘号")
	private String containerCode;
	
	@Column("container_subcode")
	@ApiModelProperty("字托盘号")
	private String containerSubCode;
	
	@Column("port_no")
	@ApiModelProperty("Wms port口编号")
	private String portNo;
	
	@Column("entry_code")
	@ApiModelProperty("eis接驳口")
	private String entryCode; 
	
	@Column("error_msg")
	@ApiModelProperty("eis异常消息")
	private String errorMsg;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public ZtContainerMsg(String containerCode, String containerSubCode, String portNo, String entryCode,
			String errorMsg, Date createTime) {
		super();
		this.containerCode = containerCode;
		this.containerSubCode = containerSubCode;
		this.portNo = portNo;
		this.entryCode = entryCode;
		this.errorMsg = errorMsg;
		this.createTime = createTime;
	}

	public ZtContainerMsg() {
		super();
		// TODO Auto-generated constructor stub
	}
}
