package com.prolog.eis.dto.mcs;

public class McsGroupDirectionDto {
	// 提升机坐标
	String coord;
	
	// 出入库方向：1 入库 2出库
	Integer direction;

	public String getCoord() {
		return coord;
	}

	public void setCoord(String coord) {
		this.coord = coord;
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	
	
}
