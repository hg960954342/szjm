package com.prolog.eis.model.eis;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("lxk_box_travel_path")
public class LxkBoxTravelPath {
	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("command_no")
	@ApiModelProperty("command_no")
	private String commandNo;
	
	@Column("box_no")
	@ApiModelProperty("box_no")
	private String boxNo;
	
	@Column("type")
	@ApiModelProperty("0出库，1入库")
	private int type;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime = new Date();

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

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
