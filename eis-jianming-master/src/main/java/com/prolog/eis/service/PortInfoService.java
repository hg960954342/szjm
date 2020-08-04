package com.prolog.eis.service;

import com.prolog.eis.model.eis.PortInfo;

import java.util.List;

public interface PortInfoService {

	List<PortInfo> getPortInfos();
	
	void addPortInfo(int portType,String wmsPortNo,String junctionPort,int layer,int x,int y,int lock,int maxCkCount,String remarks);
	
	void editPortInfo(int id,int portType,String wmsPortNo,String junctionPort,int layer,int x,int y,int lock,String remarks);
	
	void deletePortInfo(int id) throws Exception;
	
	void changePortDir(int id,int portType) throws Exception;

	void switchPortDirection(String junctionPort, int mode) throws Exception;
}
