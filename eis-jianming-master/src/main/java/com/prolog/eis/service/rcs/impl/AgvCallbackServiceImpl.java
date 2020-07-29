package com.prolog.eis.service.rcs.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.service.rcs.AgvCallbackService;

@Service
public class AgvCallbackServiceImpl implements AgvCallbackService{

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void agvCallback(String taskCode,String method) throws Exception{
		
		
	}
}
