package com.prolog.eis.service.config;

import java.util.List;

import com.prolog.eis.model.config.MaterielNoConfig;

public interface MaterielNoConfigService {

	List<MaterielNoConfig> getMaterielNoConfigList();
	
	void addMaterielNoConfig(String materielNo);
	
	void updateMaterielNoConfig(int id,String materielNo);
	
	void deleteMaterielNoConfig(int id);
}
