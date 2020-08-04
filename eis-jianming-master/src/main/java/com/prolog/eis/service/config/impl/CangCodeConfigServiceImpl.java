package com.prolog.eis.service.config.impl;

import com.prolog.eis.dao.config.CangCodeConfigMapper;
import com.prolog.eis.model.config.CangCodeConfig;
import com.prolog.eis.service.config.CangCodeConfigService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class CangCodeConfigServiceImpl implements CangCodeConfigService {

	@Autowired
	private CangCodeConfigMapper cangCodeConfigMapper;
	
	@Override
	public List<CangCodeConfig> getCangCodeConfigList(){
		return cangCodeConfigMapper.findByMap(new HashMap<String, Object>(), CangCodeConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addCangCodeConfig(String cangCode) {
		
		CangCodeConfig config = new CangCodeConfig();
		config.setCangCode(cangCode);
		
		cangCodeConfigMapper.save(config);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateCangCodeConfig(int id,String cangCode) {
		cangCodeConfigMapper.updateMapById(id, MapUtils.put("cangCode", cangCode).getMap(), CangCodeConfig.class);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteCangCodeConfig(int id) {
		cangCodeConfigMapper.deleteById(id, CangCodeConfig.class);
	}
}
