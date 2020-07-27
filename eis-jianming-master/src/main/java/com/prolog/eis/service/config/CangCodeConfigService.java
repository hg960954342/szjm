package com.prolog.eis.service.config;

import java.util.List;

import com.prolog.eis.model.config.CangCodeConfig;

public interface CangCodeConfigService {

	List<CangCodeConfig> getCangCodeConfigList();
	
	void addCangCodeConfig(String cangCode);
	
	void updateCangCodeConfig(int id,String cangCode);
	
	void deleteCangCodeConfig(int id);
}
