package com.prolog.eis.service.impl;

import org.springframework.stereotype.Service;

@Service
public class MCSLineServiceImpl{

	/*@Autowired
	private PalletInfoMapper palletInfoMapper;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private DeviceJunctionPortMapper deviceJunctionPortMapper;
	@Autowired
	private QcSxStoreMapper qcSxStoreMapper;
	@Autowired
	private SxInStoreService sxInStoreService;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private SxConnectionRimMapper sxConnectionRimMapper;
	@Autowired
	private ZtContainerMsgService ztContainerMsgService;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private McsInterfaceService mcsInterfaceService;
	@Autowired
	private SysParameService sysParameService;
	@Autowired
	private EmptyCaseConfigService emptyCaseConfigService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void emptyPalletRequest(String deviceNo,String containerNo) throws Exception {

		PalletInfo palletInfo = palletInfoMapper.findById(containerNo, PalletInfo.class);
		if(null == palletInfo) {
			throw new Exception("母托盘不存在" + containerNo);
		}

		//检查有无wms任务
		List<WmsInboundTask> inboundTasks = wmsInboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).put("palletSize", "P").getMap(), WmsInboundTask.class);
		if(!inboundTasks.isEmpty()) {
			if(inboundTasks.get(0).getTaskType() != 30) {
				throw new Exception("存在WMS入库任务" + containerNo);	
			}else {
				return;
			}
		}

		List<WmsOutboundTask> outboundTasks = wmsOutboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).put("palletSize", "P").getMap(), WmsOutboundTask.class);
		if(!outboundTasks.isEmpty()) {
			throw new Exception("存在WMS出库任务" + containerNo);
		}

		//生成wms入库任务
		WmsInboundTask wmsInboundTask = new WmsInboundTask();
		wmsInboundTask.setCommandNo("SxkEmptyPallet"+System.currentTimeMillis());
		wmsInboundTask.setWmsPush(0);
		wmsInboundTask.setWhNo("HA_WH");
		wmsInboundTask.setAreaNo("HAC_ASRS");
		wmsInboundTask.setTaskType(30);
		wmsInboundTask.setPalletId(null);
		wmsInboundTask.setContainerCode(containerNo);
		wmsInboundTask.setPalletSize("P");
		//wmsInboundTask.setMatType(i.getMatType());
		wmsInboundTask.setWeight(200d);
		wmsInboundTask.setFinished(0);
		wmsInboundTask.setReport(0);
		wmsInboundTask.setCreateTime(new Date());

		wmsInboundTaskMapper.save(wmsInboundTask);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void splitOutBound(String deviceNo) {
		DeviceJunctionPort deviceJunctionPort = deviceJunctionPortMapper.findById(deviceNo, DeviceJunctionPort.class);
		if(null == deviceJunctionPort) {
			FileLogHelper.WriteLog("splitOutBoundInfo", "设备不存在" + deviceNo);
			return;
		}

		//检查是否已经存在出库任务，否则找到空托盘
		List<WmsOutboundTask> tasks = wmsOutboundTaskMapper.findByMap(MapUtils.put("portNo", deviceJunctionPort.getEntryCode()).getMap(), WmsOutboundTask.class);
		if(!tasks.isEmpty()) {
			//已经有往出库口送的任务了
			FileLogHelper.WriteLog("splitOutBoundInfo", "已经有往出库口送的任务" + deviceNo);
			return;
		}

		//优先找同层库存
		SxStoreDto sxStore = null;
		List<SxStoreDto> sxStores = qcSxStoreMapper.findEmptySxStoreByLayer(deviceJunctionPort.getLayer());
		if(sxStores.isEmpty()) {
			List<SxStoreDto> tempList = qcSxStoreMapper.findEmptySxStore();
			if(!tempList.isEmpty()) {
				sxStore = tempList.get(0);
			}
		}else {
			sxStore = sxStores.get(0);
		}

		if(null == sxStore) {
			FileLogHelper.WriteLog("splitOutBoundInfo", "库内无空托盘");
			return;
		}


		//生成出库任务，自带出库口
		WmsOutboundTask wmsOutboundTask =new WmsOutboundTask();
		wmsOutboundTask.setGroupId(0);
		wmsOutboundTask.setCommandNo(PrologStringUtils.newGUID());
		wmsOutboundTask.setWmsPush(0);
		wmsOutboundTask.setWhNo("HA_WH");
		wmsOutboundTask.setAreaNo("HAC_ASRS");
		wmsOutboundTask.setTaskType(30);
		wmsOutboundTask.setContainerCode(sxStore.getContainerNo());
		wmsOutboundTask.setPalletSize("P");
		wmsOutboundTask.setPortNo(deviceJunctionPort.getEntryCode());
		wmsOutboundTask.setEntryCode(deviceJunctionPort.getEntryCode());
		wmsOutboundTask.setFinished(0);
		wmsOutboundTask.setReport(0);
		wmsOutboundTask.setCreateTime(new Date());

		wmsOutboundTaskMapper.save(wmsOutboundTask);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createEmptyBoxTask(String entryCode,int layer) throws Exception {
		//优先找同层库存
		SxStoreDto sxStore = null;
		List<SxStoreDto> sxStores = qcSxStoreMapper.findEmptySxStoreByLayer(layer);
		if(sxStores.isEmpty()) {
			List<SxStoreDto> tempList = qcSxStoreMapper.findEmptySxStore();
			if(!tempList.isEmpty()) {
				sxStore = tempList.get(0);
			}
		}else {
			sxStore = sxStores.get(0);
		}

		if(null == sxStore) {
			throw new Exception("库内无空托盘");
		}

		//生成出库任务，自带出库口
		WmsOutboundTask wmsOutboundTask =new WmsOutboundTask();
		wmsOutboundTask.setGroupId(0);
		wmsOutboundTask.setCommandNo(PrologStringUtils.newGUID());
		wmsOutboundTask.setWmsPush(0);
		wmsOutboundTask.setWhNo("HA_WH");
		wmsOutboundTask.setAreaNo("HAC_ASRS");
		wmsOutboundTask.setTaskType(30);
		wmsOutboundTask.setContainerCode(sxStore.getContainerNo());
		wmsOutboundTask.setPalletSize("P");
		wmsOutboundTask.setPortNo(entryCode);
		wmsOutboundTask.setEntryCode(entryCode);
		wmsOutboundTask.setFinished(0);
		wmsOutboundTask.setReport(0);
		wmsOutboundTask.setCreateTime(new Date());

		wmsOutboundTaskMapper.save(wmsOutboundTask);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String bcrRequest(String device,String containerNo) throws Exception {

		//检查是否在母托资料里面
		PalletInfo palletInfo = palletInfoMapper.findById(containerNo, PalletInfo.class);
		if(null == palletInfo) {

			return this.sendErrorPort(containerNo, "", "母托"+containerNo+"资料不存在");
		}

		if("noRead".equals(containerNo) || StringUtils.isEmpty(containerNo)) {
			//未读到条码，发送到异常口
			if(StringUtils.isEmpty(containerNo)) {
				containerNo = "noRead";
			}
			return this.sendErrorPort(containerNo, "", "母托"+containerNo+"未读到条码");
		}

		//检查有无入库任务
		List<WmsInboundTask> inTasks = wmsInboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).put("finished",0).getMap(), WmsInboundTask.class);
		if(!inTasks.isEmpty()) {
			//检查
			WmsInboundTask inTask = inTasks.get(0);
			//List<Integer> layers = new ArrayList<Integer>();
			List<List<Integer>> layerGroups = new ArrayList<>();
			if(inTask.getTaskType() == 30) {				
				layerGroups = emptyCaseConfigService.getEmptyCaseLayer(1,1,4);
			}else {
				if("VMI".equals(inTask.getMatType())){
					//海关1，2层
					List<Integer> layers = new ArrayList<Integer>();
					layers.add(1);
					layers.add(2);
					layerGroups.add(layers);
				}else if("INX".equals(inTask.getMatType())){
					//非海关34567层
					if(inTask.getDetection() == 0) {
						List<Integer> layers = new ArrayList<Integer>();
						layers.add(3);
						layers.add(4);	
						layerGroups.add(layers);
					}else {
						layerGroups = DetetionLayerHelper.getLayers(inTask.getDetection(),3,7);	
					}
				}else {
					throw new Exception(String.format("容器：%s任务异常:%s", containerNo,inTask.getMatType()));
				}
			}

			int reservedLocation = 0;
			if(inTask.getTaskType() == 30){
				//空托
				reservedLocation = 1;
			}else {
				//任务托
				reservedLocation = 2;
			}

			//如果是空托，则首先找当前的层是否可入
			Integer locationId = null;
			if(null == locationId) {
				for (List<Integer> layers : layerGroups) {
					try {
						Integer findLayer = sxInStoreService.findLayer(0,layers, 8, inTask.getMaterielNo(), inTask.getFactoryNo());

						int reserveCount = sysParameService.getLayerReserveCount(findLayer);

						locationId = sxInStoreService.getInStoreDetail(containerNo, findLayer, inTask.getMaterielNo(),
								inTask.getFactoryNo(), 10, 0, reservedLocation,1,inTask.getWeight(),reserveCount);
						if(null != locationId) {
							break;
						}
					} catch (Exception e) {
						// TODO: handle exception
						//繼續尋找其他層
						//int a = 0;
					}
				}
			}
			if(null == locationId) {
				//循环
				return "0";
			}
			SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationId, SxStoreLocation.class);

			//比较层哪个少
			List<ZtckContainer> ztList = ztckContainerMapper.findByMap(MapUtils.put("taskType", 50).put("taskStatus", 10).getMap(), ZtckContainer.class);
			int layer = sxStoreLocation.getLayer();
			if(sxStoreLocation.getLayer() > 4) {
				Integer minCount = null;
				for(int i=3;i<5;i++) {
					int temLayer = i;
					int temCount = ListHelper.where(ztList, p->p.getTargeLayer() == temLayer).size();
					if(null == minCount || temCount < minCount) {
						layer = temLayer;
						minCount = temCount;
					}
				}	
			}

			//查询在途
			//根据层查询出库任务
			boolean success = this.checkAvoid(sxStoreLocation.getLayer(),sxStoreLocation.getX(),sxStoreLocation.getY());
			if(success) {
				//1号2号提升机均可以
				//查询
				//List<ZtckContainer> ztList = ztckContainerMapper.findByMap(MapUtils.put("taskType", 50).put("taskStatus", 10).put("targeLayer", sxStoreLocation.getLayer()).getMap(), ZtckContainer.class);
				if(sxStoreLocation.getLayer() > 4) {
					ztList = ListHelper.where(ztList, p->p.getTargeLayer() == 3 || p.getTargeLayer() == 4);
				}else {
					ztList = ListHelper.where(ztList, p->p.getTargeLayer() == sxStoreLocation.getLayer());
				}
				//只判断两个提升机
				HashMap<String, Integer> mapT = new HashMap<String, Integer>();
				mapT.put("T01", 0);
				mapT.put("T02", 0);
				for (ZtckContainer ztckContainer : ztList) {
					if(mapT.containsKey(ztckContainer.getHoistNo())) {
						mapT.put(ztckContainer.getHoistNo(), mapT.get(ztckContainer.getHoistNo()) + 1);
					}
				}

				if(mapT.get("T02") > mapT.get("T01")) {
					//分配T01
					return setHoistNo("T01",layer,containerNo);
				}else {
					//分配T02
					return setHoistNo("T02",layer,containerNo);
				}
			}else {
				return setHoistNo("T02",layer,containerNo);
			}
		}

		//检查有无出库任务
		List<SxkPointDto> sxkPointDtos = wmsOutboundTaskMapper.getCkXmtNodes(containerNo);
		if(!sxkPointDtos.isEmpty()){
			return PrologCoordinateUtils.splicingStr(sxkPointDtos.get(0).getX(), sxkPointDtos.get(0).getY(), sxkPointDtos.get(0).getLayer());
		}

		//无任务
		return this.sendErrorPort(containerNo, "", "母托"+containerNo+"无任务");
	}

	@Override
	public boolean checkAvoid(int layer,int x,int y) {
		List<String> layerTask = wmsOutboundTaskMapper.getT01CkNode(layer);
		if(!layerTask.isEmpty()) {
			return false;
		}

		List<SxPathPlanningTaskDto> sxPathPlanningTaskMxs = qcSxPathPlanningTaskMxMapper.findSxCkPathTask();
		Map<Integer, List<SxPathPlanningTaskDto>> dic = ListHelper.buildGroupDictionary(sxPathPlanningTaskMxs, p->p.getTaskHzId());
		for(Entry<Integer, List<SxPathPlanningTaskDto>> entry : dic.entrySet()){
			List<SxPathPlanningTaskDto> vs = entry.getValue();
			List<SxPathPlanningTaskDto> tems = ListHelper.where(vs, p->p.getNode().contains("T01") && p.getLayer() == layer);
			if(!tems.isEmpty()) {
				//检查有无小于30的任务，否则小于30
				for (SxPathPlanningTaskDto tem : vs) {
					if(tem.getNode().contains("T01") && tem.getIsComplete() < 40) {
						return false;
					}else if(!tem.getNode().contains("T01") && tem.getIsComplete() < 30){
						return false;
					}
				}
			}
		}

		//检查点位
		String position = "";
		if(layer <= 2) {
			position = PrologCoordinateUtils.splicingStr(28, 24, layer);
		}else {
			position = PrologCoordinateUtils.splicingStr(62, 37, layer);
		}
		try {
			boolean isEmpty = mcsInterfaceService.getExitStatus(position);
			if(!isEmpty) {
				return false;
			}

			McsHoistStatusDto mcsHoistStatusDto = mcsInterfaceService.getHoistStatus("T01");
			if(null == mcsHoistStatusDto) {
				return false;
			}else {
				if(mcsHoistStatusDto.getStatus() != 2) {
					return false;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("checkAvoidError", e.toString());
			return false;
		}

		return true;
	}

	private String sendErrorPort(String containerNo,String containerSubNo,String errorMsg) {
		List<PortInfo> errorPortList = portInfoMapper.findByMap(MapUtils.put("position", "1").put("area", 2).put("errorPort", 1).getMap(), PortInfo.class);
		if(errorPortList.isEmpty()) {
			return "0100220015";
		}else {
			PortInfo errorPort = errorPortList.get(0);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, errorPort.getWmsPortNo(), errorPort.getJunctionPort(), errorMsg);
			return PrologCoordinateUtils.splicingStr(errorPort.getX(), errorPort.getY(), errorPort.getLayer());
		}
	}

	private String setHoistNo(String hoistNo, int layer,String containerNo) throws Exception {
		String nextPosition = hoistNo + String.format("%02d", layer) + "02";
		//直接返回2号提升机当前层的内侧
		ZtckContainer ztckContainer = ztckContainerMapper.findById(containerNo, ZtckContainer.class);
		if(null == ztckContainer) {
			//创建在途
			ztckContainer = new ZtckContainer();
			ztckContainer.setContainerCode(containerNo);
			ztckContainer.setEntryCode(nextPosition);
			ztckContainer.setTaskType(50);
			ztckContainer.setTaskStatus(10);
			ztckContainer.setHoistNo(hoistNo);
			ztckContainer.setTargeLayer(layer);
			ztckContainer.setCreateTime(new Date());

			ztckContainerMapper.save(ztckContainer);
		}else {
			ztckContainerMapper.updateMapById(containerNo, MapUtils.put("entryCode", nextPosition)
					.put("taskType", 50)
					.put("taskStatus", 10)
					.put("hoistNo", hoistNo)
					.put("targeLayer", layer)
					.put("createTime", new Date()).getMap(), ZtckContainer.class);
		}

		List<SxConnectionRim> sxConnectionRims = sxConnectionRimMapper.findByMap(MapUtils.put("entryCode", nextPosition).getMap(), SxConnectionRim.class);
		if(sxConnectionRims.isEmpty()) {
			throw new Exception(String.format("接驳口：%s数据异常", nextPosition));
		}

		String position = PrologCoordinateUtils.splicingStr(sxConnectionRims.get(0).getX(), sxConnectionRims.get(0).getY(), sxConnectionRims.get(0).getLayer());
		return position;
	}*/
}
