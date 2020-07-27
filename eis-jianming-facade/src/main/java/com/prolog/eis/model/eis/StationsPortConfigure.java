package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("stations_port_configure")
public class StationsPortConfigure {

	@Id
	@Column("id")
	@ApiModelProperty("主键")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("stations_id")
	@ApiModelProperty("主键")
	private int stationsId;
	
	@Column("port_id")
	@ApiModelProperty("主键")
	private int portId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStationsId() {
		return stationsId;
	}

	public void setStationsId(int stationsId) {
		this.stationsId = stationsId;
	}

	public int getPortId() {
		return portId;
	}

	public void setPortId(int portId) {
		this.portId = portId;
	}

	public StationsPortConfigure(int id, int stationsId, int portId) {
		super();
		this.id = id;
		this.stationsId = stationsId;
		this.portId = portId;
	}

	public StationsPortConfigure() {
		super();
		// TODO Auto-generated constructor stub
	}
}
