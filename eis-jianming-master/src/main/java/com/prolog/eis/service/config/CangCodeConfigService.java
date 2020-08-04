package com.prolog.eis.service.config;

import com.prolog.eis.model.config.CangCodeConfig;

import java.util.List;

public interface CangCodeConfigService {

	List<CangCodeConfig> getCangCodeConfigList();
	
	void addCangCodeConfig(String cangCode);
	
	void updateCangCodeConfig(int id,String cangCode);
	
	void deleteCangCodeConfig(int id);
}
