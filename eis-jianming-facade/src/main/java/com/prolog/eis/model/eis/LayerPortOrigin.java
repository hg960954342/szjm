package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("layer_port_origin")
public class LayerPortOrigin {

	@Id
	@Column("id")
	@ApiModelProperty("接驳口编号")
	private int id;
	
	@Column("entry_code")
	@ApiModelProperty("接驳口编号")
	private String entryCode;
	
	@Column("layer")
	@ApiModelProperty("层")
	private int layer;
	
	@Column("origin_x")
	@ApiModelProperty("X原点")
	private int originX;
	
	@Column("origin_y")
	@ApiModelProperty("Y原点")
	private int originY;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getOriginX() {
		return originX;
	}

	public void setOriginX(int originX) {
		this.originX = originX;
	}

	public int getOriginY() {
		return originY;
	}

	public void setOriginY(int originY) {
		this.originY = originY;
	}

	
}
