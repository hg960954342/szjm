package com.prolog.eis.service.simulator.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.dao.middle.WmsRawTrkInterfaceMapper;
import com.prolog.eis.model.middle.WmsRawTrkInterface;
import com.prolog.eis.service.simulator.BuildTestDataService;

@Service
public class BuildTestDataServiceImpl implements BuildTestDataService {

	@Autowired
	private WmsRawTrkInterfaceMapper wmsRawTrkInterfaceMapper;
	
	@Override
	public long wmsRawTrkInterface(List<WmsRawTrkInterface> list) {
		long t = wmsRawTrkInterfaceMapper.saveBatch(list);
		return t;
	}

	@Override
	public List<WmsRawTrkInterface> getWmsRawTrkInterface() {
		List<WmsRawTrkInterface> list = wmsRawTrkInterfaceMapper.findByMap(new HashMap<String, Object>(), WmsRawTrkInterface.class);
		return list;
	}
}
