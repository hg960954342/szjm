package com.prolog.eis.service.impl;

import org.springframework.stereotype.Service;

@Service
public class YkDispatchServiceImpl{

	/*@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxOutStoreService sxOutStoreService;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void buildYkTask(SxStoreDto sxStore) throws Exception {
		String containerCode = sxStore.getContainerNo();
		Integer storeLocationId = sxStore.getStoreLocationId();
		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
		
		//查询wms出库任务
		List<WmsOutboundTask> tasks = wmsOutboundTaskMapper.findByMap(MapUtils.put("finished", 10).put("containerCode", containerCode).getMap(), WmsOutboundTask.class);
		if(tasks.size() > 1) {
			FileLogHelper.WriteLog("buildYkTaskError", String.format("容器：%s存在多个wms出库任务", containerCode));

			return;
		}else if(tasks.size() == 1) {
			sxOutStoreService.checkMoveStore(containerCode, sxStoreLocation.getLayer(), sxStoreLocation.getX(), sxStoreLocation.getY()
					, String.valueOf(sxStoreLocationGroup.getBelongArea()), tasks.get(0).getEntryCode());
		}
		
	}*/
}
