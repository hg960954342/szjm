package com.prolog.eis.simulator.dto;

import java.util.List;

import com.prolog.eis.model.store.SxConnectionRim;
import com.prolog.eis.model.store.SxHoister;

import io.swagger.annotations.ApiModelProperty;

public class SimulatorDataDto {

	@ApiModelProperty("提升机集合")
	private List<SxHoister> sxHoisterList;
	
	@ApiModelProperty("提升机出入口集合")
	private List<SxConnectionRim> sxConnectionRimList;

	public List<SxHoister> getSxHoisterList() {
		return sxHoisterList;
	}

	public void setSxHoisterList(List<SxHoister> sxHoisterList) {
		this.sxHoisterList = sxHoisterList;
	}

	public List<SxConnectionRim> getSxConnectionRimList() {
		return sxConnectionRimList;
	}

	public void setSxConnectionRimList(List<SxConnectionRim> sxConnectionRimList) {
		this.sxConnectionRimList = sxConnectionRimList;
	}
	
	
}
