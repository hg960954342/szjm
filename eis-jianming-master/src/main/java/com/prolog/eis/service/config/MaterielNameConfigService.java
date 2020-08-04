package com.prolog.eis.service.config;

import com.prolog.eis.model.config.MaterielNameConfig;

import java.util.List;

public interface MaterielNameConfigService {

	List<MaterielNameConfig> getMaterielNameConfigList();
	
	void addMaterielNameConfig(String materielName);
	
	void updateMaterielNameConfig(int id,String materielName);
	
	void deleteMaterielNameConfig(int id);
}
