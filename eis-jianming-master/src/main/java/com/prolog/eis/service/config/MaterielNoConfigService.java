package com.prolog.eis.service.config;

import com.prolog.eis.model.config.MaterielNoConfig;

import java.util.List;

public interface MaterielNoConfigService {

	List<MaterielNoConfig> getMaterielNoConfigList();
	
	void addMaterielNoConfig(String materielNo);
	
	void updateMaterielNoConfig(int id,String materielNo);
	
	void deleteMaterielNoConfig(int id);
}
