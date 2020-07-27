package com.prolog.eis.service.config;

import java.util.List;

import com.prolog.eis.model.config.FactoryTypeConfig;

public interface FactoryTypeConfigService {

	List<FactoryTypeConfig> getFactoryTypeConfigList();
	
	void addFactoryTypeConfig(String factoryType);
	
	void updateFactoryTypeConfig(int id,String factoryType);
	
	void deleteFactoryTypeConfig(int id);
}
