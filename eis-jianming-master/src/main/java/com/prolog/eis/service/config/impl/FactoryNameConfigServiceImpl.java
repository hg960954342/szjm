package com.prolog.eis.service.config.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.config.FactoryNameConfigMapper;
import com.prolog.eis.model.config.FactoryNameConfig;
import com.prolog.eis.service.config.FactoryNameConfigService;
import com.prolog.framework.utils.MapUtils;

@Service
public class FactoryNameConfigServiceImpl implements FactoryNameConfigService{

	@Autowired
	private FactoryNameConfigMapper factoryNameConfigMapper;
	
	@Override
	public List<FactoryNameConfig> getFactoryNameConfigList(){
		return factoryNameConfigMapper.findByMap(new HashMap<String, Object>(), FactoryNameConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addFactoryNameConfig(String factoryName) {
		
		FactoryNameConfig config = new FactoryNameConfig();
		config.setFactoryName(factoryName);
		
		factoryNameConfigMapper.save(config);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateFactoryNameConfig(int id,String factoryName) {
		
		factoryNameConfigMapper.updateMapById(id,MapUtils.put("factoryName", factoryName).getMap(),FactoryNameConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFactoryNameConfig(int id) {
		factoryNameConfigMapper.deleteById(id, FactoryNameConfig.class);
	}
}
