package com.prolog.eis.service;

import java.util.List;

import com.prolog.eis.model.eis.StationsInfo;

public interface StationsInfoService {

	List<StationsInfo> getStationsInfos();
	
	void addStationsInfo(String wmsStationNo,String remark);
	
	void editStationsInfo(int id, String wmsStationNo,String remark);
	
	void deleteStationsInfo(int id);
	
	List<StationsInfo> getLxkStations();
	
	List<StationsInfo> getSxkStations();
}
