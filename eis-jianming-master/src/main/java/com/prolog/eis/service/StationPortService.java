package com.prolog.eis.service;

import java.util.List;

import com.prolog.eis.dto.eis.StationPortDto;
import com.prolog.eis.model.eis.PortInfo;

public interface StationPortService {

	List<StationPortDto> getStationPortDtos();
	
	void addStationPortDtos(int stationId,int portId);
	
	void deleteStationPortDtos(int stationId,int portId);
	
	List<PortInfo> getOtherPortInfo(int stationId);
}
