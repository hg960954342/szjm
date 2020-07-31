package com.prolog.eis.dto.sxk;

import java.util.List;

public class SxHoisterRimDto {
private int id;
	
	private String hoistNo;
	
	private List<HoistLayer> hoistLayerList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHoistNo() {
		return hoistNo;
	}

	public void setHoistNo(String hoistNo) {
		this.hoistNo = hoistNo;
	}

	public List<HoistLayer> getHoistLayerList() {
		return hoistLayerList;
	}

	public void setHoistLayerList(List<HoistLayer> hoistLayerList) {
		this.hoistLayerList = hoistLayerList;
	}
}
