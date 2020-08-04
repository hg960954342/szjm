package com.prolog.eis.service.config;

import com.prolog.eis.model.config.FactoryNameConfig;

import java.util.List;

public interface FactoryNameConfigService {

	List<FactoryNameConfig> getFactoryNameConfigList();
	
	void addFactoryNameConfig(String factoryName);
	
	void updateFactoryNameConfig(int id,String factoryName);
	
	void deleteFactoryNameConfig(int id);
}
