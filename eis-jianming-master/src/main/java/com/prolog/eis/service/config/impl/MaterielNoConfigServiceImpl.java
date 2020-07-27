package com.prolog.eis.service.config.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.config.MaterielNoConfigMapper;
import com.prolog.eis.model.config.MaterielNoConfig;
import com.prolog.eis.service.config.MaterielNoConfigService;
import com.prolog.framework.utils.MapUtils;

@Service
public class MaterielNoConfigServiceImpl implements MaterielNoConfigService {

	@Autowired
	private MaterielNoConfigMapper materielNoConfigMapper;
	
	@Override
	public List<MaterielNoConfig> getMaterielNoConfigList(){
		return materielNoConfigMapper.findByMap(new HashMap<String, Object>(), MaterielNoConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addMaterielNoConfig(String materielNo) {
		
		MaterielNoConfig config = new MaterielNoConfig();
		config.setMaterielNo(materielNo);
		
		materielNoConfigMapper.save(config);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMaterielNoConfig(int id,String materielNo) {
		
		materielNoConfigMapper.updateMapById(id,MapUtils.put("materielNo", materielNo).getMap(),MaterielNoConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteMaterielNoConfig(int id) {
		
		materielNoConfigMapper.deleteById(id,MaterielNoConfig.class);
	}
}
