package com.prolog.eis.service.store.impl;

import org.springframework.stereotype.Service;

@Service
public class QcInBoundReturnExcuteServiceImpl {

	/*@Autowired
	private QcSxConnectionRimMapper qcSxConnectionRimMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private WmsCallCarTaskMapper wmsCallCarTaskMapper;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private SxPathPlanningTaskService sxPathPlanningTaskService;
	@Autowired
	private ZtContainerMsgService ztContainerMsgService;
	@Autowired
	private LedMessageService ledMessageService;
	@Autowired
	private SxConnectionRimMapper sxConnectionRimMapper;
	@Autowired
	private SxPathPlanningTaskMxMapper taskMxMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxHoisterMapper sxHoisterMapper;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private SxInStoreService sxInStoreService;
	@Autowired
	private SysParameMapper sysParameMapper;
	@Autowired
	private SxPathPlanningTaskService pathTaskService;
	@Autowired
	private LayerPortOriginService layerPortOriginService;
	@Autowired
	private SysParameService sysParameService;
	@Autowired
	private EmptyCaseConfigService emptyCaseConfigService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void taskReturnOutBound(WmsOutboundTask task,String containerNo,String targetPosition,int targetLayer,int targetX,int targetY) throws Exception {
		//执行出库逻辑
		//出库任务mcs完成
		List<SxConnectionRim> sxConnectionRimList = qcSxConnectionRimMapper.findRealHoisterJunctionByPosition(targetLayer, targetX, targetY);
		if(!sxConnectionRimList.isEmpty()) {
			//判断有无库存
			SxStore sxStore = this.checkSxStore(containerNo);
			if(null != sxStore) {
				List<PortInfo> ports = portInfoMapper.findByMap(MapUtils.put("junctionPort", sxConnectionRimList.get(0).getEntryCode()).getMap(), PortInfo.class);
				if(ports.isEmpty()) {
					//需要转成在途库存
					createZtContainer(containerNo,task.getPalletId(),task.getStations(),task.getPortNo(),task.getEntryCode()
							,task.getTaskType(),20,sxStore.getTaskProperty1(),sxStore.getTaskProperty2(),sxStore.getBusinessProperty1()
							,sxStore.getBusinessProperty2(),sxStore.getBusinessProperty3(),sxStore.getBusinessProperty4(),sxStore.getBusinessProperty5()
							,sxStore.getWeight());
				}else {
					ztckContainerMapper.deleteById(containerNo, ZtckContainer.class);
					PortInfo port = ports.get(0);
					this.checkCallCar(task,port,sxStore.getTaskProperty1(),sxStore.getTaskProperty2(),sxStore.getBusinessProperty1(),sxStore.getBusinessProperty2());
					this.setWmsTaskState(task,90);
					this.arrivePort(containerNo,port);
				}
			}

			this.updateCompleteTask(containerNo, sxConnectionRimList.get(0).getEntryCode(),true);
		}else {
			//检查是否到达最终目的地
			List<PortInfo> ports = portInfoMapper.findByMap(MapUtils.put("layer",targetLayer).put("x", targetX).put("y", targetY).getMap(), PortInfo.class);
			if(ports.size() == 1) {
				PortInfo portInfo = ports.get(0);

				//判断有无库存
				SxStore sxStore = this.checkSxStore(containerNo);
				if (null != sxStore) {
					if(portInfo.getTaskType() == 4) {
						//到达质检口
						//修改在途状态
						//如果在途存在则修改在途，如果在途不存在则创建在途
						ZtckContainer ztckContainer = ztckContainerMapper.findById(containerNo, ZtckContainer.class);
						if(null == ztckContainer) {
							//创建在途
							createZtContainer(containerNo,sxStore.getContainerSubNo(),task.getStations(),portInfo.getWmsPortNo(),portInfo.getJunctionPort()
									,task.getTaskType(),20,sxStore.getTaskProperty1(),sxStore.getTaskProperty2(),sxStore.getBusinessProperty1(),sxStore.getBusinessProperty2()
									,sxStore.getBusinessProperty3(),sxStore.getBusinessProperty4(),sxStore.getBusinessProperty5(),sxStore.getWeight());
						}else {
							ztckContainerMapper.updateMapById(containerNo, MapUtils.put("taskStatus", 20).put("portNo", portInfo.getWmsPortNo()).put("entryCode", portInfo.getJunctionPort()).getMap(), ZtckContainer.class);	
						}
					}else {
						//判断是否到达西码头入库口
						this.arrivePort(containerNo,portInfo);
						ztckContainerMapper.deleteById(containerNo, ZtckContainer.class);
					}

					this.checkCallCar(task,portInfo,sxStore.getTaskProperty1(),sxStore.getTaskProperty2(),sxStore.getBusinessProperty1(),sxStore.getBusinessProperty2());
				}else{
					ZtckContainer ztckContainer = ztckContainerMapper.findById(containerNo, ZtckContainer.class);
					if(portInfo.getTaskType() == 4) {
						ztckContainerMapper.updateMapById(containerNo, MapUtils.put("taskStatus", 20).put("portNo", portInfo.getWmsPortNo()).getMap(), ZtckContainer.class);
					}else {
						//判断是否到达西码头入库口
						this.arrivePort(containerNo,portInfo);
						ztckContainerMapper.deleteById(containerNo, ZtckContainer.class);
					}

					this.checkCallCar(task,portInfo,ztckContainer.getMaterielNo(),ztckContainer.getFactoryNo(),ztckContainer.getMaterielType(),ztckContainer.getMaterielName());
				}

				this.setWmsTaskState(task,90);
				this.updateCompleteTask(containerNo, portInfo.getJunctionPort(),true);
			}else {
				//判断是否到达拆叠盘机
				String postition = PrologCoordinateUtils.splicingStr(targetX, targetY, targetLayer);
				if("0100330014".equals(postition) || "0100240014".equals(postition)) {
					//判断有无库存
					this.checkSxStore(containerNo);

					ztckContainerMapper.deleteById(containerNo, ZtckContainer.class);
					this.setWmsTaskState(task,90);

					List<SxConnectionRim> connectionRimList = sxConnectionRimMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), SxConnectionRim.class);
					if(!connectionRimList.isEmpty()) {
						this.updateCompleteTask(containerNo, connectionRimList.get(0).getEntryCode(),true);
					}
				}else {
					List<SxConnectionRim> connectionRimList = sxConnectionRimMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), SxConnectionRim.class);
					if(!connectionRimList.isEmpty()) {
						this.updateCompleteTask(containerNo, connectionRimList.get(0).getEntryCode(),true);
					}
				}
			}
		}
	}

	private void checkCallCar(WmsOutboundTask task,PortInfo port,String productId,String factoryNo,String materielType,String materielName) {
		if(task.getWmsPush() == 0) {
			if(port.getCallCar() == 1) {
				WmsCallCarTask wmsCallCarTask = new WmsCallCarTask();
				wmsCallCarTask.setWhNo("HA_WH");
				wmsCallCarTask.setAreaNo("HAC_ASRS");
				wmsCallCarTask.setPalletId(task.getPalletId());
				wmsCallCarTask.setProductId(productId);
				wmsCallCarTask.setFactoryNo(factoryNo);
				wmsCallCarTask.setMaterielType(materielType);
				wmsCallCarTask.setMaterielName(materielName);
				wmsCallCarTask.setIo("O");
				wmsCallCarTask.setStations(task.getStations());
				wmsCallCarTask.setPort(port.getWmsPortNo());
				wmsCallCarTask.setFinished(90);
				wmsCallCarTask.setErrCode(200);
				wmsCallCarTask.setCreatTime(new Date());

				wmsCallCarTaskMapper.save(wmsCallCarTask);
			}
		}
	}

	private void createZtContainer(String containerNo,String containerSubNo,String stations,String wmsPortNo,String junctionPort,
			int taskType,int taskStatus,String taskProperty1,String taskProperty2,String businessProperty1,String businessProperty2,
			String businessProperty3,String businessProperty4,String businessProperty5,Double weight) {
		ZtckContainer newZtckContainer = new ZtckContainer();
		newZtckContainer.setContainerCode(containerNo);
		newZtckContainer.setContainerSubCode(containerSubNo);
		newZtckContainer.setStations(stations);
		newZtckContainer.setPortNo(wmsPortNo);
		newZtckContainer.setEntryCode(junctionPort);
		newZtckContainer.setTaskType(taskType);
		newZtckContainer.setTaskStatus(taskStatus);
		newZtckContainer.setMaterielNo(taskProperty1);
		newZtckContainer.setFactoryNo(taskProperty2);									
		newZtckContainer.setMaterielType(businessProperty1);
		newZtckContainer.setMaterielName(businessProperty2);
		newZtckContainer.setFactoryCode(businessProperty3);
		newZtckContainer.setBoxCount(businessProperty4);
		if(StringUtils.isEmpty(businessProperty5)) {
			newZtckContainer.setDetection(1);
		}else {
			newZtckContainer.setDetection(Integer.valueOf(businessProperty5));	
		}

		newZtckContainer.setWeight(weight);
		newZtckContainer.setCreateTime(new Date());

		ztckContainerMapper.save(newZtckContainer);
	}

	private void setWmsTaskState(WmsOutboundTask task,int finished) {
		if(task.getWmsPush() == 0) {
			//eis任务s
			wmsOutboundTaskMapper.updateMapById(task.getId(), MapUtils.put("finished", finished).put("report", 0).put("endTime", new Date()).getMap(), WmsOutboundTask.class);
		}else {
			wmsOutboundTaskMapper.updateMapById(task.getId(), MapUtils.put("finished", finished).put("report", 1).put("endTime", new Date()).getMap(), WmsOutboundTask.class);	
		}
	}

	//判断有无库存
	private SxStore checkSxStore(String containerNo) throws Exception {
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
				SxStore.class);
		if (sxStores.size() == 1) {
			SxStore sxStore = sxStores.get(0);
			Integer storeLocationId = sxStore.getStoreLocationId();
			SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
			// 根据出库任务类型转换
			sxStoreMapper.deleteByContainer(containerNo);
			sxStoreLocationMapper.updateMapById(storeLocationId, MapUtils.put("actualWeight", 0).getMap(),
					SxStoreLocation.class);
			sxStoreTaskFinishService.computeLocation(sxStore);
			sxStoreLocationGroupMapper.updateMapById(cksxStoreLocation.getStoreLocationGroupId(),
					MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);

			return sxStore;
		}else {
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateCompleteTask(String containerNo, String position,boolean showError) throws Exception {
		List<SxPathPlanningTaskMx> sxPathPlanningTaskMxs = taskMxMapper.findSxPathPlanningTaskMx(containerNo);
		List<SxPathPlanningTaskMx> list = sxPathPlanningTaskMxs.stream()
				.filter(t -> t.getTaskId() != null && t.getNode().equals(position) && (t.getIsComplete() == 30 || t.getIsComplete() == 20))
				.collect(Collectors.toList());
		if (list.size() == 0) {
			if(showError) {
				throw new Exception("【"+ containerNo +"】没有任务");	
			}else {
				return -1;
			}
		} else if (list.size() == 1) {
			SxPathPlanningTaskMx sxPathPlanningTaskMx = list.get(0);
			sxPathPlanningTaskMx.setUpdateTime(PrologDateUtils.parseObject(new Date()));
			sxPathPlanningTaskMx.setIsComplete(40);
			taskMxMapper.update(sxPathPlanningTaskMx);
			// 判断当前节点是不是终点
			if (sxPathPlanningTaskMx.getNodeType() != 3) {
				// 查找下一个节点
				SxPathPlanningTaskMx entity = sxPathPlanningTaskMxs.stream().filter(
						t -> t.getSortIndex() == (sxPathPlanningTaskMx.getSortIndex() + 1) && t.getIsComplete() == 0)
						.findFirst().get();
				entity.setIsComplete(10);
				// MCS系统
				// entity = this.hoisterLogic(entity,sxPathPlanningTaskMxs);
				entity.setUpdateTime(PrologDateUtils.parseObject(new Date()));
				taskMxMapper.update(entity);
			} else {
				// 任务转历史
				sxPathPlanningTaskService.turnHistoryTask(sxPathPlanningTaskMx.getTaskHzId());
			}
			return sxPathPlanningTaskMx.getNodeType();
		} else {
			throw new Exception("【"+ containerNo +"】没有任务有多个任务");
		}
	}

	//判斷是否到达西码头出库口，需要写入Led信息
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void arrivePort(String containerNo,PortInfo portInfo) {
		if(portInfo.getShowLed() == 1 && portInfo.getErrorPort() == 1) {
			//检查是否有在途
			String errorMsg = ztContainerMsgService.getContainNoErrorMsg(containerNo, portInfo.getWmsPortNo());
			if("noRead".equals(containerNo)) {
				errorMsg = "母托未掃到條碼";
			}

			if(!StringUtils.isEmpty(errorMsg)) {
				this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,errorMsg);	
			}else {
				this.addLedMsg(portInfo.getId(),portInfo.getPortType(),0,"母托" + containerNo + "已到達");
			}
		}else if(portInfo.getShowLed() == 1) {
			this.addLedMsg(portInfo.getId(),portInfo.getPortType(),0,"母托" + containerNo + "已到達");
		}
	}

	private void addLedMsg(int portId,int portType,int sate,String msg) {
		String stateStr = "";
		if(portType == 1) {
			stateStr = "入庫";
		} else {
			stateStr = "出庫";
		}

		ledMessageService.saveLedMessage(portId, stateStr, sate, msg);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void taskReturnInbound(SxConnectionRim sxConnectionRim,WmsInboundTask wmsInboundTask,String taskId,Double weight,String containerNo,String source,int sourceLayer,int sourceX,int sourceY) throws Exception {
		//检查库存是否存在同母托任务
		int result = this.updateCompleteTask(containerNo, sxConnectionRim.getEntryCode(),false);
		if(result != -1) {
			return;
		}

		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
		if(sxStores.size()>0) {
			//存在同母托
			FileLogHelper.WriteLog("inSxStoreError", "母托盘重复" + containerNo);

			return ;
		}

		//查询提升机
		SxHoister sxHoister = sxHoisterMapper.findById(sxConnectionRim.getSxHoisterId(), SxHoister.class);
		String hositorNo = sxHoister.getHoisterNo();

		//获取入库层 
		List<List<Integer>> layerGroups = new ArrayList<>();

		if(sourceLayer < 3){
			//空托盘
			List<Integer> layers = new ArrayList<Integer>();
			layers.add(sourceLayer);
			layerGroups.add(layers);
		}else {
			if(wmsInboundTask.getTaskType() == 30) {
				//空托
				layerGroups = emptyCaseConfigService.getEmptyCaseLayer(sourceLayer,sourceLayer,7);
			}else {
				layerGroups = DetetionLayerHelper.getLayers(wmsInboundTask.getDetection(),sourceLayer,7);	
			}
		}

		//获取配置的原点
		SysParame sysParame1 = sysParameMapper.findById("BOTTOM_X", SysParame.class);
		SysParame sysParame2 = sysParameMapper.findById("BOTTOM_Y", SysParame.class);

		int originX = 0;
		if(null != sysParame1) {
			originX = Integer.valueOf(sysParame1.getParameValue());
		}

		int originY = 0;
		if(null != sysParame2) {
			originY = Integer.valueOf(sysParame2.getParameValue());	
		}


		int reservedLocation = 0;
		if(wmsInboundTask.getTaskType() == 30){
			//空托
			reservedLocation = 1;
		}else {
			//任务托
			reservedLocation = 2;
		}

		//拿到货位Id
		Integer locationId = null;
		boolean findLocation = false;
		for(int i = 0;i<layerGroups.size();i++) {
			List<Integer> layers = layerGroups.get(i);

			try {
				int reserveCount = sysParameService.getLayerReserveCount(layers.get(0));

				if(layerGroups.size() == (i - 1)) {
					reserveCount = 15;
				}
				Integer findLayer = sxInStoreService.findLayer(0,layers, reserveCount, wmsInboundTask.getMaterielNo(), wmsInboundTask.getFactoryNo());

				LayerPortOrigin layerPortOrigin = layerPortOriginService.getPortOrigin(sxConnectionRim.getEntryCode(),findLayer,originX,originY);

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
			throw new Exception(containerNo + "没有找到货位！！！");
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
		sxStore.setBusinessProperty5(String.valueOf(wmsInboundTask.getDetection()));

		if(wmsInboundTask.getTaskType() == 30){
			sxStore.setTaskType(-1);//空托盘
		}else {
			sxStore.setTaskType(wmsInboundTask.getTaskType());//暂不知道有什么意义	
		}
		sxStore.setHoisterNo(hositorNo);
		sxStore.setTaskId(taskId);
		sxStore.setWeight(weight);
		sxStore.setCreateTime(PrologDateUtils.parseObject(new Date()));
		sxStore.setInStoreTime(PrologDateUtils.parseObject(new Date()));
		sxStoreMapper.save(sxStore);
		sxStoreLocationMapper.updateMapById(locationId, MapUtils.put("actualWeight", weight).getMap(), SxStoreLocation.class);
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

		//回写入库任务状态
		//如果是空拖则货位写000000
		if(wmsInboundTask.getTaskType() == 30) {
			wmsInboundTaskMapper.updateMapById(wmsInboundTask.getId(), 
					MapUtils.put("finished", 20)
					.put("binNo", "000000")
					.put("report",wmsInboundTask.getWmsPush())
					.put("shfSd", 0).getMap(), WmsInboundTask.class);
		}else {
			wmsInboundTaskMapper.updateMapById(wmsInboundTask.getId(), 
					MapUtils.put("finished", 20)
					.put("binNo", sxStoreLocation.getWmsStoreNo())
					.put("report",wmsInboundTask.getWmsPush())
					.put("shfSd", sxStoreLocation.getDepth()).getMap(), WmsInboundTask.class);
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void excuteThroughTask(SxConnectionRim sxConnectionRim,String containerNo,Coordinate coordinate,PortInfoDto portInfoDto) throws Exception {

		//創建路徑任務
		List<SxPathParameDto> start = new ArrayList<SxPathParameDto>();
		SxPathParameDto startSxPathParameDto = new SxPathParameDto();
		startSxPathParameDto.setConnectionNo(sxConnectionRim.getEntryCode());
		startSxPathParameDto.setParameType(1);
		startSxPathParameDto.setLayer(coordinate.getLayer());
		startSxPathParameDto.setRegionNo("1");
		startSxPathParameDto.setX(coordinate.getX());
		startSxPathParameDto.setY(coordinate.getY());
		start.add(startSxPathParameDto);

		List<SxPathParameDto> end = new ArrayList<SxPathParameDto>();
		SxPathParameDto endSxPathParameDto = new SxPathParameDto();
		endSxPathParameDto.setConnectionNo(portInfoDto.getJunctionPort());
		endSxPathParameDto.setParameType(1);
		endSxPathParameDto.setLayer(portInfoDto.getLayer());
		endSxPathParameDto.setRegionNo("1");
		endSxPathParameDto.setX(portInfoDto.getX());
		endSxPathParameDto.setY(portInfoDto.getY());
		end.add(endSxPathParameDto);

		// 创建路径任务
		pathTaskService.createPathTask(containerNo, start, end);
	}

	private List<PortInfoDto> getPortTaskMinCount(List<PortInfoDto> portlist,List<PortTaskCount> taskCountList) {

		for (PortInfoDto portInfoDto : portlist) {
			PortTaskCount portTaskCount = ListHelper.firstOrDefault(taskCountList, p->portInfoDto.getWmsPortNo().equals(p.getPortNo()));
			if(null!=portTaskCount) {
				portInfoDto.setTaskCount(portTaskCount.getTaskCount());
			}else {
				portInfoDto.setTaskCount(0);
			}
		}
		portlist.sort((p1,p2)->{ return p1.getTaskCount() - p2.getTaskCount();});
		return portlist;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setTaskStart(String taskId) throws Exception{
		List<SxPathPlanningTaskMx> sxPathPlanningTaskMxs = taskMxMapper.findByMap(MapUtils.put("taskId", taskId).getMap(), SxPathPlanningTaskMx.class);
		if(sxPathPlanningTaskMxs.size() == 1) {
			SxPathPlanningTaskMx sxPathPlanningTaskMx = sxPathPlanningTaskMxs.get(0);
			if(sxPathPlanningTaskMx.getIsComplete() == 20) {
				sxPathPlanningTaskMx.setIsComplete(30);
				sxPathPlanningTaskMx.setUpdateTime(PrologDateUtils.parseObject(new Date()));
				taskMxMapper.update(sxPathPlanningTaskMx);
			}
		}
	}*/
}
