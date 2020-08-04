package com.prolog.eis.service.base.impl;

import com.prolog.eis.dao.base.SysParameMapper;
import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.service.base.SysParameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysParameServiceImpl implements SysParameService {

	@Autowired
	private SysParameMapper sysParameMapper;
	
	@Override
	public int getLayerReserveCount(int layer) {
		
		String key = String.format("LAYER%s_RESERVE_COUNT", layer);
		SysParame sysParame = sysParameMapper.findById(key, SysParame.class);
		if(null == sysParame) {
			return 10;
		}else {
			return Integer.valueOf(sysParame.getParameValue());
		} 
	}
}
