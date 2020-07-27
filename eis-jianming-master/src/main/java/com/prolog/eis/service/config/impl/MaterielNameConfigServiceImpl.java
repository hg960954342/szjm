package com.prolog.eis.service.config.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.config.MaterielNameConfigMapper;
import com.prolog.eis.model.config.MaterielNameConfig;
import com.prolog.eis.service.config.MaterielNameConfigService;
import com.prolog.framework.utils.MapUtils;

@Service
public class MaterielNameConfigServiceImpl implements MaterielNameConfigService{

	@Autowired
	private MaterielNameConfigMapper materielNameConfigMapper;
	
	@Override
	public List<MaterielNameConfig> getMaterielNameConfigList(){
		return materielNameConfigMapper.findByMap(new HashMap<String, Object>(), MaterielNameConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addMaterielNameConfig(String materielName) {
		
		MaterielNameConfig config = new MaterielNameConfig();
		config.setMaterielName(materielName);
		
		materielNameConfigMapper.save(config);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMaterielNameConfig(int id,String materielName) {
		
		materielNameConfigMapper.updateMapById(id,MapUtils.put("materielName", materielName).getMap(),MaterielNameConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteMaterielNameConfig(int id) {
		
		materielNameConfigMapper.deleteById(id,MaterielNameConfig.class);
	}
}
