package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("layer_limit_high")
public class LayerLimitHigh {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("layer")
	@ApiModelProperty("层")
	private int portType;

	@Column("limit_high")
	@ApiModelProperty("限高")
	private Double limitHigh;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPortType() {
		return portType;
	}

	public void setPortType(int portType) {
		this.portType = portType;
	}

	public Double getLimitHigh() {
		return limitHigh;
	}

	public void setLimitHigh(Double limitHigh) {
		this.limitHigh = limitHigh;
	}
}
