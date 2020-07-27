package com.prolog.eis.service.config;

import java.util.List;

import com.prolog.eis.model.config.MaterielNameConfig;

public interface MaterielNameConfigService {

	List<MaterielNameConfig> getMaterielNameConfigList();
	
	void addMaterielNameConfig(String materielName);
	
	void updateMaterielNameConfig(int id,String materielName);
	
	void deleteMaterielNameConfig(int id);
}
