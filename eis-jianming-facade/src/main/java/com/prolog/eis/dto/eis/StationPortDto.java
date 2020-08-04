package com.prolog.eis.dto.eis;

import com.prolog.eis.model.eis.PortInfo;

import java.util.List;

public class StationPortDto {

	private int id;
	
	private String wmsStationNo;
	
	private List<PortInfo> portInfoList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWmsStationNo() {
		return wmsStationNo;
	}

	public void setWmsStationNo(String wmsStationNo) {
		this.wmsStationNo = wmsStationNo;
	}

	public List<PortInfo> getPortInfoList() {
		return portInfoList;
	}

	public void setPortInfoList(List<PortInfo> portInfoList) {
		this.portInfoList = portInfoList;
	}

	public StationPortDto(int id, String wmsStationNo) {
		super();
		this.id = id;
		this.wmsStationNo = wmsStationNo;
	}
}
