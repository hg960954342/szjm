package com.prolog.eis.dto.sxk;

import java.util.List;


public class HoistLayer {
	private int layer;

	private List<RimListDto> hoistList;

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public List<RimListDto> getHoistList() {
		return hoistList;
	}

	public void setHoistList(List<RimListDto> hoistList) {
		this.hoistList = hoistList;
	}
}
