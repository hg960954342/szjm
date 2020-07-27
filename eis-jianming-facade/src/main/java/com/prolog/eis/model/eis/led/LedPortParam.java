package com.prolog.eis.model.eis.led;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("led_port_param")
public class LedPortParam {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("port_id")
	@ApiModelProperty("port口id")
	private int portId;
	
	@Column("led_title")
	@ApiModelProperty("title")
	private String ledTitle;

	@Column("uda0")
	@ApiModelProperty("备用字段0")
	private String uda0;

	@Column("uda1")
	@ApiModelProperty("备用字段1")
	private String uda1;

	@Column("uda2")
	@ApiModelProperty("备用字段2")
	private String uda2;

	@Column("uda3")
	@ApiModelProperty("备用字段3")
	private String uda3;

	@Column("uda4")
	@ApiModelProperty("备用字段4")
	private String uda4;

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

	public String getLedTitle() {
		return ledTitle;
	}

	public void setLedTitle(String ledTitle) {
		this.ledTitle = ledTitle;
	}

	public String getUda0() {
		return uda0;
	}

	public void setUda0(String uda0) {
		this.uda0 = uda0;
	}

	public String getUda1() {
		return uda1;
	}

	public void setUda1(String uda1) {
		this.uda1 = uda1;
	}

	public String getUda2() {
		return uda2;
	}

	public void setUda2(String uda2) {
		this.uda2 = uda2;
	}

	public String getUda3() {
		return uda3;
	}

	public void setUda3(String uda3) {
		this.uda3 = uda3;
	}

	public String getUda4() {
		return uda4;
	}

	public void setUda4(String uda4) {
		this.uda4 = uda4;
	}

}
