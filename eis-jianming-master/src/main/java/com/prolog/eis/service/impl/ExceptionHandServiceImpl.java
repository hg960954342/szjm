package com.prolog.eis.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.WmsInboundTaskMapper;
import com.prolog.eis.dao.WmsOutboundTaskMapper;
import com.prolog.eis.dao.ZtckContainerMapper;
import com.prolog.eis.model.eis.WmsInboundTask;
import com.prolog.eis.model.eis.WmsOutboundTask;
import com.prolog.eis.model.eis.ZtckContainer;
import com.prolog.eis.service.ExceptionHandService;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.utils.MapUtils;

@Service
public class ExceptionHandServiceImpl implements ExceptionHandService{

	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addEmptyContainer(String containerNo,String userId) throws Exception {
		
		List<WmsInboundTask> inboundTasks = wmsInboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).getMap(), WmsInboundTask.class);
		if(!inboundTasks.isEmpty()) {
			throw new Exception("母托盤已經存在入庫任務");
		}
		
		List<WmsOutboundTask> outboundTasks = wmsOutboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).getMap(), WmsOutboundTask.class);
		if(!outboundTasks.isEmpty()) {
			throw new Exception("母托盤已經存在出庫任務");
		}
		
		ZtckContainer ztckContainer = ztckContainerMapper.findById(containerNo, ZtckContainer.class);
		if(null == ztckContainer) {
			throw new Exception("母托盤已經存在在途任務");
		}

		WmsInboundTask wmsInboundTask = new WmsInboundTask();
		wmsInboundTask.setCommandNo(PrologStringUtils.newGUID());
		wmsInboundTask.setWmsPush(0);
		wmsInboundTask.setWhNo("HA_WH");
		wmsInboundTask.setAreaNo("HAC_ASRS");
		wmsInboundTask.setTaskType(30);
		wmsInboundTask.setContainerCode(containerNo);
		wmsInboundTask.setPalletSize("P");
		wmsInboundTask.setWeight(200d);
		wmsInboundTask.setFinished(0);
		wmsInboundTask.setReport(0);
		wmsInboundTask.setCreateTime(new Date());
		wmsInboundTask.setUserId(userId);
		
		wmsInboundTaskMapper.save(wmsInboundTask);
	}
}
