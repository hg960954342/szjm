package com.prolog.eis.service.store;

import java.util.List;

import com.prolog.eis.model.store.SxHoister;

public interface SxHoisterService {

	void update(int id,String hoisterNo,int islock,int hoisterType) throws Exception;
	
	void add(String hoisterNo,int isLock,int hoisterType) throws Exception;
	
	List<SxHoister> getAllSxHoisters();
	
	void deleteSxHoister(int id);
}
