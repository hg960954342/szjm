package com.prolog.eis.service.config;

import java.util.List;

import com.prolog.eis.model.config.FactoryNameConfig;

public interface FactoryNameConfigService {

	List<FactoryNameConfig> getFactoryNameConfigList();
	
	void addFactoryNameConfig(String factoryName);
	
	void updateFactoryNameConfig(int id,String factoryName);
	
	void deleteFactoryNameConfig(int id);
}
