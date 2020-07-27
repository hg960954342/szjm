package com.prolog.eis.service.config.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.config.FactoryTypeConfigMapper;
import com.prolog.eis.model.config.FactoryTypeConfig;
import com.prolog.eis.service.config.FactoryTypeConfigService;
import com.prolog.framework.utils.MapUtils;

@Service
public class FactoryTypeConfigServiceImpl implements FactoryTypeConfigService{

	@Autowired
	private FactoryTypeConfigMapper factoryTypeConfigMapper;
	
	@Override
	public List<FactoryTypeConfig> getFactoryTypeConfigList(){
		return factoryTypeConfigMapper.findByMap(new HashMap<String, Object>(), FactoryTypeConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addFactoryTypeConfig(String factoryType) {
		
		FactoryTypeConfig config = new FactoryTypeConfig();
		config.setFactoryType(factoryType);
		
		factoryTypeConfigMapper.save(config);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateFactoryTypeConfig(int id,String factoryType) {
		
		factoryTypeConfigMapper.updateMapById(id,MapUtils.put("factoryType", factoryType).getMap(),FactoryTypeConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteFactoryTypeConfig(int id) {
		
		factoryTypeConfigMapper.deleteById(id,FactoryTypeConfig.class);
	}
}
