package com.prolog.eis.service.impl;

public class CkDispatchServiceImpl{

	/*@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private SxOutStoreService sxOutStoreService;
	@Autowired
	private QcSxStoreMapper qcSxStoreMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private PortTemsInfoMapper portTemsInfoMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private SxHoisterConfigMapper sxHoisterConfigMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	@Autowired
	private SxHoisterMapper sxHoisterMapper;
	@Autowired
	private SysParameMapper sysParameMapper;
	@Autowired
	private SxInStoreService sxInStoreService;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxPathPlanningTaskService sxPathPlanningTaskService;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	@Autowired
	private QcSxPathPlanningConfigHzMapper qcSxPathPlanningConfigHzMapper;
	@Autowired
	private SxPathPlanningConfigMxMapper sxPathPlanningConfigMxMapper;
	@Autowired
	private SxPathPlanningTaskHzMapper sxPathPlanningTaskHzMapper;
	@Autowired
	private SxConnectionRimMapper sxConnectionRimMapper;
	@Autowired
	private SxPathPlanningTaskMxMapper sxPathPlanningTaskMxMapper;
	@Autowired
	private LayerPortOriginService layerPortOriginService;
	@Autowired
	private SysParameService sysParameService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void xiafaChuKuTask(WmsOutboundTask wmsOutboundTask, PortInfoDto portInfoDto,SxStoreDto sxStore) throws Exception {

		boolean bl = sxOutStoreService.checkStore(sxStore.getContainerNo(), sxStore.getLayer(), sxStore.getX(), sxStore.getY(), "1", portInfoDto.getJunctionPort());
		if(bl) {
			if(portInfoDto.getTaskType() == 4) {
				portInfoDto.setTaskLock(1);
				portInfoMapper.updateMapById(portInfoDto.getId(), MapUtils.put("taskLock", 1).getMap(), PortInfo.class);
			}
			
			if(wmsOutboundTask.getTaskType() == 30) {
				//修改wms任务进度状态
				//wms空托任务将空托盘写进出库任务
				wmsOutboundTaskMapper.updateMapById(wmsOutboundTask.getId(), MapUtils.put("finished", 10)
						.put("containerCode", sxStore.getContainerNo())
						.put("portNo", portInfoDto.getWmsPortNo())
						.put("entryCode", portInfoDto.getJunctionPort())
						.put("containerCode", sxStore.getContainerNo())
						.put("report", 0).getMap(), WmsOutboundTask.class);
			}else {
				//修改wms任务进度状态				
				wmsOutboundTaskMapper.updateMapById(wmsOutboundTask.getId(), MapUtils.put("finished", 10)
						.put("portNo", portInfoDto.getWmsPortNo())
						.put("entryCode", portInfoDto.getJunctionPort())
						.put("containerCode", sxStore.getContainerNo())
						.put("report", 0).getMap(), WmsOutboundTask.class);	
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void xiafaTenpTask(int taskId,List<PortTemsInfoDto> temPortTemsInfoDto,SxStoreDto sxStore) throws Exception {

		if(!temPortTemsInfoDto.isEmpty()) {
			//发到暂存位 {
			PortTemsInfoDto portTemsInfo = temPortTemsInfoDto.get(0);
			// 验证出库任务是否锁定
			boolean bl = sxOutStoreService.checkStore(sxStore.getContainerNo(), sxStore.getLayer(), sxStore.getX(), sxStore.getY(), "1", portTemsInfo.getJunctionPort());
			if(bl) {
				portTemsInfo.setTaskLock(1);
				portTemsInfoMapper.updateMapById(portTemsInfo.getId(), MapUtils.put("taskLock", 1).getMap(), PortTemsInfo.class);
				//修改wms任务进度状态						
				wmsOutboundTaskMapper.updateMapById(taskId, MapUtils.put("finished", 10)
						.put("entryCode", portTemsInfo.getJunctionPort())
						.put("containerCode", sxStore.getContainerNo())
						.put("report", 0).getMap(), WmsOutboundTask.class);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void iqcTask(List<WmsOutboundTask> iqcTasks,List<TempPortZtTaskDto> tempPortZtTaskList) throws Exception {

		List<TempPortZtTaskDto> temList = ListHelper.distinct(tempPortZtTaskList,p->p.getJunctionPortId());
		for (WmsOutboundTask wmsOutboundTask : iqcTasks) {
			TempPortZtTaskDto tempPortZtTaskDto = ListHelper.firstOrDefault(temList, p->p.getContainerCode().equals(wmsOutboundTask.getContainerCode()));
			if(null != tempPortZtTaskDto) {
				//需要发送质检的出库任务
				// 调用生成路径的方法

				SxPathPlanningConfigHz sxPathPlanningConfigHz = qcSxPathPlanningConfigHzMapper.getPointToPointConfigHz("@" + tempPortZtTaskDto.getTemJunctionPort() + "@","@" + tempPortZtTaskDto.getJunctionPort() + "@");
				if(null != sxPathPlanningConfigHz) {
					List<SxPathPlanningConfigMx> mxList = sxPathPlanningConfigMxMapper.findByMap(MapUtils.put("configHzId", sxPathPlanningConfigHz.getId()).getMap(), SxPathPlanningConfigMx.class);

					//生成路径任务
					SxPathPlanningTaskHz taskHz = new SxPathPlanningTaskHz();
					taskHz.setConfigId(sxPathPlanningConfigHz.getId());
					taskHz.setStartLocation(sxPathPlanningConfigHz.getStartLocation());
					taskHz.setTargetLocation(sxPathPlanningConfigHz.getTargetLocation());
					taskHz.setContainerNo(tempPortZtTaskDto.getContainerCode());
					taskHz.setPathType(2);
					taskHz.setMsg(sxPathPlanningConfigHz.getMsg());
					taskHz.setCreateTime(new Date());

					sxPathPlanningTaskHzMapper.save(taskHz);

					List<SxPathPlanningTaskMx> taskMxList = new ArrayList<SxPathPlanningTaskMx>();
					for(int i = 0;i<mxList.size();i++) {
						SxPathPlanningConfigMx sxPathPlanningConfigMx = mxList.get(i);

						SxPathPlanningTaskMx mx = new SxPathPlanningTaskMx();
						mx.setSortIndex(i + 1);
						if(i == 0) {
							mx.setIsComplete(40);
						}else if(i==1){
							mx.setIsComplete(10);
						}else {
							mx.setIsComplete(0);
						}
						mx.setTaskHzId(taskHz.getId());
						mx.setNodeType(sxPathPlanningConfigMx.getNodeType());
						mx.setTransportationEquipment(sxPathPlanningConfigMx.getTransportationEquipment());
						mx.setNode(sxPathPlanningConfigMx.getNode().replace("@", ""));
						Map<String,Integer> xyz = sxConnectionRimMapper.findXyLayer(mx.getNode());
						mx.setLineDirection(sxPathPlanningConfigMx.getLineDirection());
						mx.setLayer(xyz.get("layer"));
						mx.setX(xyz.get("x"));
						mx.setY(xyz.get("y"));
						mx.setCreateTime(new Date());
						mx.setUpdateTime(new Date());

						taskMxList.add(mx);
					}
					sxPathPlanningTaskMxMapper.saveBatch(taskMxList);

					portInfoMapper.updateMapById(tempPortZtTaskDto.getJunctionPortId(), MapUtils.put("taskLock", 1).getMap(), PortInfo.class);
					//portTemsInfoMapper.updateMapById(tempPortZtTaskDto.getTemJjunctionPortId(), MapUtils.put("taskLock", 2),PortTemsInfo.class);

					ztckContainerMapper.updateMapById(wmsOutboundTask.getContainerCode(), MapUtils.put("taskStatus", 10)
							.put("entryCode", tempPortZtTaskDto.getJunctionPort())
							.put("portNo",tempPortZtTaskDto.getWmsPortNo()).getMap(), ZtckContainer.class);
					//修改wms任务进度状态						
					wmsOutboundTaskMapper.updateMapById(wmsOutboundTask.getId(), 
							MapUtils.put("finished", 10)
							.put("portNo", tempPortZtTaskDto.getWmsPortNo())
							.put("entryCode", tempPortZtTaskDto.getJunctionPort())
							.put("report", 0).getMap(), WmsOutboundTask.class);
				}				
			}else {
				//可能存在去同一个接驳口被过滤的任务
				//this.printErrorMsg("容器无质检任务" + wmsOutboundTask.getContainerCode(),true);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void buildEmptyTask(List<WmsOutboundTask> emptyTasks) throws Exception {
		//wms 下发的暂未考虑  空托不需要子托盘编号，分配空托到

		//空托盘根据母托盘找任务
		List<String> containerList = ListHelper.select(emptyTasks, p->p.getContainerCode());
		if(containerList.isEmpty())
			return;

		String containersStr = PrologInOfListToStringHelper.getSqlStrByList(containerList, "t.container_no");

		List<SxStoreDto> sxStoreList = qcSxStoreMapper.findSxStoreByContainerNo(containersStr);
		for (WmsOutboundTask emptyTask : emptyTasks) {
			//检查母托盘盘是否存在四项库
			SxStoreDto sxStore = ListHelper.firstOrDefault(sxStoreList,p->p.getContainerNo().equals(emptyTask.getContainerCode()));
			if(null == sxStore) {
				throw new Exception(String.format("找不到母托盘：%s 出库任务", emptyTask.getContainerCode()));
			}else {
				// 验证出库任务是否锁定
				boolean bl = sxOutStoreService.checkStore(sxStore.getContainerNo(), sxStore.getLayer(), sxStore.getX(), sxStore.getY(), "1", emptyTask.getPortNo());
				if(bl) {
					//修改wms任务进度状态						
					wmsOutboundTaskMapper.updateMapById(emptyTask.getId(), MapUtils.put("finished", 10).put("report", 0).getMap(), WmsOutboundTask.class);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void buildRkIqcTask() {
		try {
			//获取所有质检任务
			List<WmsInboundTask> rkIqc = wmsInboundTaskMapper.findByMap(MapUtils.put("taskType", 20).put("palletSize", "P").put("finished", 0).getMap(), WmsInboundTask.class);
			//获取包含质检port口的叫料解包区
			List<String> stations = portInfoMapper.getIqcStations();
			for (WmsInboundTask task : rkIqc) {
				//只取第一个包含质检区的任务
				if(stations.contains(task.getStations())) {
					List<WmsIqcInboundTaskDto> taskList = wmsInboundTaskMapper.getRkIqcTask(task.getId());
					for (WmsIqcInboundTaskDto wmsIqcInboundTaskDto : taskList) {
						this.inSxStore(wmsIqcInboundTaskDto,
								wmsIqcInboundTaskDto.getZtWeight(),
								wmsIqcInboundTaskDto.getParentContainerCode(),
								wmsIqcInboundTaskDto.getPortId(),
								wmsIqcInboundTaskDto.getLayer(),
								wmsIqcInboundTaskDto.getX(),
								wmsIqcInboundTaskDto.getY());
						break;
					}
					break;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("buildRkIqcTask", e.toString());
		}
	}

	private void inSxStore(WmsIqcInboundTaskDto wmsInboundTask,Double weight,String containerNo,int portId,int sourceLayer,int sourceX,int sourceY) throws Exception {

		ZtckContainer ztckContainer = ztckContainerMapper.findById(containerNo, ZtckContainer.class);
		if(null == ztckContainer)
			return;

		List<SxConnectionRim> sxHoisterConfigs = sxHoisterConfigMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), SxConnectionRim.class);
		if(sxHoisterConfigs.size()!=1) {
			return;
		}

		SxConnectionRim sxConnectionRim = sxHoisterConfigs.get(0);

		//检查库存是否存在同母托任务
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
		if(sxStores.size()>0) {
			//存在同母托
			return ;
		}

		//查询提升机
		SxHoister sxHoister = sxHoisterMapper.findById(sxConnectionRim.getSxHoisterId(), SxHoister.class);
		String hositorNo = sxHoister.getHoisterNo();

		List<List<Integer>> layerGroups = new ArrayList<>();
		
		if("VMI".equals(wmsInboundTask.getMatType())){
			List<Integer> layers = new ArrayList<Integer>();
			//海关1，2层
			layers.add(1);
			layers.add(2);
			
			layerGroups.add(layers);
		}else if("INX".equals(wmsInboundTask.getMatType())){
			//非海关34567层
			layerGroups = DetetionLayerHelper.getLayers(ztckContainer.getDetection(),3,7);
		}else {
			throw new Exception(String.format("容器:：%s任务异常:%s", containerNo,wmsInboundTask.getMatType()));
		}

		//获取配置的原点
		List<SysParame> sysParame1 = sysParameMapper.findByMap(MapUtils.put("parameNo", "BOTTOM_X").getMap(),
				SysParame.class);
		List<SysParame> sysParame2 = sysParameMapper.findByMap(MapUtils.put("parameNo", "BOTTOM_Y").getMap(),
				SysParame.class);

		int originX = Integer.valueOf(sysParame1.get(0).getParameValue());
		int originY = Integer.valueOf(sysParame2.get(0).getParameValue());

		int reservedLocation = 2;

		//如果是空托，则首先找当前的层是否可入
		Integer locationId = null;
		boolean findLocation = false;
		for (List<Integer> layers : layerGroups) {
			try {
				Integer findLayer = sxInStoreService.findLayer(0,layers, 8, wmsInboundTask.getMaterielNo(), wmsInboundTask.getFactoryNo());

				LayerPortOrigin layerPortOrigin = layerPortOriginService.getPortOrigin(sxConnectionRim.getEntryCode(),findLayer,originX,originY);
				
				int reserveCount = sysParameService.getLayerReserveCount(findLayer);
				
				locationId = sxInStoreService.getInStoreDetail(containerNo, findLayer, wmsInboundTask.getMaterielNo(),
						wmsInboundTask.getFactoryNo(), layerPortOrigin.getOriginX(), layerPortOrigin.getOriginY(), reservedLocation,1,wmsInboundTask.getWeight(),reserveCount);
				if(null != locationId) {
					findLocation = true;
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				//繼續尋找其他層
			}
		}
		if(!findLocation) {
			throw new Exception("貨位不足！！！");
		}

		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationId, SxStoreLocation.class);
		// 1.保存到箱库库存2.锁定货位组(修改货位组升位锁)
		SxStore sxStore = new SxStore();
		sxStore.setStoreLocationId(locationId);
		//库存任务类型 1 wms库存 2 eis库存
		if(wmsInboundTask.getWmsPush() == 0) {
			sxStore.setSxStoreType(2);
		}else if(wmsInboundTask.getWmsPush() == 1) {
			sxStore.setSxStoreType(1);
		}
		sxStore.setContainerNo(containerNo);
		sxStore.setContainerSubNo(wmsInboundTask.getPalletId());
		sxStore.setStoreState(10);
		sxStore.setTaskProperty1(wmsInboundTask.getMaterielNo());
		sxStore.setTaskProperty2(wmsInboundTask.getInDate());
		sxStore.setBusinessProperty1(wmsInboundTask.getMaterielType());
		sxStore.setBusinessProperty2(wmsInboundTask.getMaterielName());
		sxStore.setBusinessProperty3(wmsInboundTask.getFactoryCode());
		sxStore.setBusinessProperty4(wmsInboundTask.getBoxCount());
		//库存记录高度
		sxStore.setBusinessProperty5(String.valueOf(ztckContainer.getDetection()));
		
		if(wmsInboundTask.getTaskType() == 30){
			sxStore.setTaskType(-1);//空托盘
		}else {
			sxStore.setTaskType(wmsInboundTask.getTaskType());//暂不知道有什么意义	
		}
		sxStore.setHoisterNo(hositorNo);
		sxStore.setTaskId(null);
		sxStore.setWeight(weight);
		sxStore.setCreateTime(PrologDateUtils.parseObject(new Date()));
		sxStore.setInStoreTime(PrologDateUtils.parseObject(new Date()));
		sxStoreMapper.save(sxStore);
		sxStoreLocationGroupMapper.updateMapById(sxStoreLocation.getStoreLocationGroupId(),
				MapUtils.put("ascentLockState", 1).getMap(), SxStoreLocationGroup.class);
		sxStoreTaskFinishService.computeLocation(sxStore);

		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
		//调用生成路径的方法
		List<SxPathParameDto> startList = new ArrayList<SxPathParameDto>();
		SxPathParameDto start = new SxPathParameDto();
		start.setParameType(1);
		start.setLayer(sourceLayer);
		start.setConnectionNo(sxConnectionRim.getEntryCode());
		start.setHoisterNo(hositorNo);
		start.setX(sourceX);
		start.setY(sourceY);
		start.setRegionNo(String.valueOf(sxHoister));
		startList.add(start);
		List<SxPathParameDto> endList = new ArrayList<SxPathParameDto>();
		SxPathParameDto end = new SxPathParameDto();
		end.setParameType(2);
		end.setLayer(sxStoreLocation.getLayer());
		end.setX(sxStoreLocation.getX());
		end.setY(sxStoreLocation.getY());
		end.setRegionNo(String.valueOf(sxStoreLocationGroup.getBelongArea()));
		endList.add(end);
		sxPathPlanningTaskService.createPathTask(containerNo, startList, endList);

		wmsInboundTaskMapper.updateMapById(wmsInboundTask.getId(), 
				MapUtils.put("finished", 20)
				.put("containerCode", containerNo)
				.put("weight", ztckContainer.getWeight())
				.put("binNo", sxStoreLocation.getWmsStoreNo())
				.put("report",1)
				.put("shfSd", sxStoreLocation.getDepth()).getMap(), WmsInboundTask.class);

		ztckContainerMapper.deleteById(containerNo, ZtckContainer.class);
		//portInfoMapper.updateMapById(portId, MapUtils.put("taskLock", 2).getMap(), PortInfo.class);
	}*/
}
