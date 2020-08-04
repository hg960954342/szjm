package com.prolog.eis.service.config;

import com.prolog.eis.model.config.FactoryTypeConfig;

import java.util.List;

public interface FactoryTypeConfigService {

	List<FactoryTypeConfig> getFactoryTypeConfigList();
	
	void addFactoryTypeConfig(String factoryType);
	
	void updateFactoryTypeConfig(int id,String factoryType);
	
	void deleteFactoryTypeConfig(int id);
}
