package com.prolog.eis.model.base;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("CURRENT_USER")
public class CurrentUser {

	@Id
	@ApiModelProperty("主键")
	private int id;
	
	@Column("USER_ID")
	@ApiModelProperty("用户ID")
	private int userId;
	
	@Column("STATION_ID")
	@ApiModelProperty("站台Id")
	private int stationId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	
	
}
