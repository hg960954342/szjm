package com.prolog.eis.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.prolog.eis.dao.WmsMoveTaskMapper;
import com.prolog.eis.model.eis.WmsMoveTask;
import com.prolog.eis.service.TestService;

public class TestServiceImpl implements TestService {
	
	@Autowired
	private WmsMoveTaskMapper wmsMoveTaskMapper;

	@Override
	public void storeLocationMove(String containerSubNo, String binNo) {
		WmsMoveTask wmsMoveTask = new WmsMoveTask();
		wmsMoveTask.setWhNo("HA_WH");
		wmsMoveTask.setAreaNo("HAC_ASRS");
		wmsMoveTask.setBinNo(binNo);
		wmsMoveTask.setPalletId(containerSubNo);
		wmsMoveTask.setPalletSize("P");
		wmsMoveTask.setFinished(90);
		wmsMoveTask.setReport(1);
		wmsMoveTask.setCreateTime(new Date());
		
		wmsMoveTaskMapper.save(wmsMoveTask);
	}

}
