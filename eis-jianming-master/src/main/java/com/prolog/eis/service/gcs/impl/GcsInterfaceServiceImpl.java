package com.prolog.eis.service.gcs.impl;

import org.springframework.stereotype.Service;

@Service
public class GcsInterfaceServiceImpl {

	/*@Autowired
	private RestTemplate restTemplate;
	@Value("${prolog.gcs.url:}")
	private String gcsUrl;
	@Value("${prolog.gcs.port:}")
	private String gcsPort;
	@Autowired
	private GcsTaskMapper gcsTaskMapper;
	@Autowired
	private GcsTaskHistoryMapper gcsTaskHistoryMapper;
	@Autowired
	private SxPathPlanningTaskService sxPathPlanningTaskService;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private WmsMoveTaskMapper wmsMoveTaskMapper;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private PortTemsInfoMapper portTemsInfoMapper;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private MCSTaskMapper mcsTaskMapper;
	@Autowired
	private SxCarAcrossTaskMapper sxCarAcrossTaskMapper;
	@Autowired
	private SxCarAcrossMapper sxCarAcrossMapper;
	@Autowired
	private McsInterfaceService mcsInterfaceService;
	@Autowired
	private CarAcrossLayerService carAcrossLayerService;
	@Autowired
	private SxCarMapper sxCarMapper;
	@Autowired
	private SxCarService sxCarService;




	@Override
	@Transactional
	public String sendGcsTaskPush(String containerNo, int layer, int taskType, int priority, String locIdFrom,
			String locIdTo) throws Exception {
		String uuid = null;
		uuid = PrologStringUtils.newGUID();
		try {
			List<GcsTaskPushReqDto> list = new ArrayList<GcsTaskPushReqDto>();
			GcsTaskPushReqDto reqDto = new GcsTaskPushReqDto();
			reqDto.setId(uuid);
			reqDto.setContainerId(containerNo);
			reqDto.setCreateTime(PrologDateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			reqDto.setFloor(layer);
			reqDto.setLocIdFrom(locIdFrom);
			reqDto.setLocIdTo(locIdTo);
			reqDto.setPriority(priority);
			reqDto.setTaskId(uuid);
			reqDto.setTaskType(taskType);
			list.add(reqDto);
			String postUrl = String.format("%s%s/%s", gcsUrl, gcsPort, "Interface/TaskOrderFromEis");
			String data = PrologApiJsonHelper.toJson(list);

			FileLogHelper.WriteLog("sendGCSTask", "EIS->GCS任务："+ data);
			String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendGCSTask", "EIS->GCS任务返回："+ restJson);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			boolean ret = helper.getBoolean("ret");
			if (ret) {
				sxPathPlanningTaskService.createPathMxTaskID(containerNo, uuid);

				List<GCSErrorDto> resultData = helper.getObjectList("data", GCSErrorDto.class);
				if(null == resultData) {
					sxPathPlanningTaskService.createPathMxTaskID(containerNo, uuid);	
				}else {
					if(resultData.isEmpty()) {
						sxPathPlanningTaskService.createPathMxTaskID(containerNo, uuid);
					}else {
						FileLogHelper.WriteLog("sendGcsTaskPushError","异常");	
					}
				}

				//this.saveOrUpdateGcsTask(containerNo, layer, taskType, priority, locIdFrom, locIdTo, uuid, 1, 1, "", 1);
			} 
		} catch (Exception e) {
			FileLogHelper.WriteLog("sendGcsTaskPushError", e.getMessage());
		}
		return uuid;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void gcsTaskReport(GcsOrderReportReqDto gcsOrderReportReqDto) throws Exception {

		if(gcsOrderReportReqDto.getWorkStatus() == 2){
			Coordinate currntCoordinate = PrologCoordinateUtils.analysis(gcsOrderReportReqDto.getPosition());
			int currentLayer = currntCoordinate.getLayer();
			int currentX = currntCoordinate.getX();
			int currentY = currntCoordinate.getY();

			List<String> rkInStorePositions = wmsInboundTaskMapper.getRkInStorePositions(currentLayer,currentX,currentY);
			if(!rkInStorePositions.isEmpty()) {
				//gcs回告完成后检查起点是否为提升机西码头入库口，如果是则清除在途库存
				ztckContainerMapper.deleteByMap(MapUtils.put("containerCode", gcsOrderReportReqDto.getContainerCode()).put("taskType", 50).getMap(), ZtckContainer.class);
			}

			int nodeType = sxPathPlanningTaskService.updateCompleteTask(gcsOrderReportReqDto.getContainerCode(), gcsOrderReportReqDto.getBillCode());
			if(nodeType == 3) {
				Coordinate coordinate = PrologCoordinateUtils.analysis(gcsOrderReportReqDto.getLocCodeTo());
				int sourceLayer = coordinate.getLayer();
				int sourceX = coordinate.getX();
				int sourceY = coordinate.getY();

				SxStoreLocation sxStoreLocation = this.getStoreLocation(sourceLayer, sourceX, sourceY);
				if(null != sxStoreLocation) {
					//更新库存
					List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", gcsOrderReportReqDto.getContainerCode()).getMap(),
							SxStore.class);
					if (sxStores.size() == 1) {
						// 修改库存状态为已上架
						//EIS移位完成
						if(sxStores.get(0).getStoreState() != 41) {
							if(null!= sxStores.get(0).getSourceLocationId()) {
								sxStoreTaskFinishService.moveTaskFinish(sxStores.get(0).getSourceLocationId());
							}
						}
						sxStoreMapper.updateContainerGround(gcsOrderReportReqDto.getContainerCode());
						SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
								.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
						int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
						sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
								MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);
						sxStoreTaskFinishService.computeLocation(sxStores.get(0));

						//上报入库wms
						List<WmsInboundTask> tasks = wmsInboundTaskMapper.findByMap(MapUtils.put("finished", 20).put("containerCode", gcsOrderReportReqDto.getContainerCode()).getMap(), WmsInboundTask.class);
						if(tasks.size() == 0) {
							//只有wms的库存修改才会上报wms
							if(sxStores.get(0).getSxStoreType() == 1) {
								//上报wms移库
								WmsMoveTask wmsMoveTask = new WmsMoveTask();
								wmsMoveTask.setWhNo("HA_WH");
								wmsMoveTask.setAreaNo("HAC_ASRS");
								wmsMoveTask.setBinNo(sxStoreLocation.getWmsStoreNo());
								wmsMoveTask.setPalletId(sxStores.get(0).getContainerSubNo());
								wmsMoveTask.setPalletSize("P");
								wmsMoveTask.setFinished(90);
								wmsMoveTask.setReport(1);
								wmsMoveTask.setCreateTime(new Date());

								wmsMoveTaskMapper.save(wmsMoveTask);	
							}
						}else if(tasks.size() > 1) {
							throw new Exception(String.format("容器：%s存在多个wms入库任务", gcsOrderReportReqDto.getContainerCode()));
						}else {
							//存在入库任务
							if(tasks.get(0).getWmsPush() == 0) {
								wmsInboundTaskMapper.updateMapById(tasks.get(0).getId(), MapUtils.put("finished", 90)
										.put("report", 0)
										.put("weight", sxStores.get(0).getWeight())
										.put("binNo",sxStoreLocation.getWmsStoreNo())
										.put("endTime", new Date()).getMap(), WmsInboundTask.class);
							}else if(tasks.get(0).getWmsPush() == 1){
								//如果是空拖。那么回告‘000000’
								if(tasks.get(0).getTaskType() == 30) {
									wmsInboundTaskMapper.updateMapById(tasks.get(0).getId(), MapUtils.put("finished", 90)
											.put("report", 1)
											.put("weight", sxStores.get(0).getWeight())
											.put("binNo","000000")
											.put("endTime", new Date()).getMap(), WmsInboundTask.class);
								}else {
									wmsInboundTaskMapper.updateMapById(tasks.get(0).getId(), MapUtils.put("finished", 90)
											.put("report", 1)
											.put("weight", sxStores.get(0).getWeight())
											.put("binNo",sxStoreLocation.getWmsStoreNo())
											.put("endTime", new Date()).getMap(), WmsInboundTask.class);
								}
							}
						}
					} else if (sxStores.size() > 1) {
						throw new Exception("查询出多个托盘");
					} else if (sxStores.size() == 0) {
						throw new Exception("库存中没有查到托盘");
					}
				}else {
					//gcs上报没有找到货位的情况可能是货位也可能是gcs小车接驳口
					//查询是否为接驳口
					List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", gcsOrderReportReqDto.getContainerCode()).getMap(),
							SxStore.class);
					if (sxStores.size() == 1) {
						SxStore sxStore = sxStores.get(0);
						Integer storeLocationId = sxStore.getStoreLocationId();
						SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
						// 根据出库任务类型转换
						sxStoreMapper.deleteByContainer(gcsOrderReportReqDto.getContainerCode());
						sxStoreLocationMapper.updateMapById(storeLocationId, MapUtils.put("actualWeight", 0).getMap(),
								SxStoreLocation.class);
						sxStoreLocationGroupMapper.updateMapById(cksxStoreLocation.getStoreLocationGroupId(),
								MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);
						sxStoreTaskFinishService.computeLocation(sxStore);

						WmsOutboundTask task = wmsOutboundTaskMapper.getNoFinishTask(sxStore.getContainerNo());
						if(null == task) {
							return;
						}

						wmsOutboundTaskMapper.updateMapById(task.getId(),MapUtils.put("finished", 50)
								.put("report", 0).getMap(),WmsOutboundTask.class);

						List<PortTemsInfo> portemInfoList = portTemsInfoMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), PortTemsInfo.class);
						if(!portemInfoList.isEmpty()) {
							//修改在途状态
							//如果在途存在则修改在途，如果在途不存在则创建在途
							this.arrivePortTemsInfo(gcsOrderReportReqDto.getContainerCode(),
									task.getStations(),
									portemInfoList.get(0).getJunctionPort(),
									task.getTaskType(),
									sxStore);
						}						
					} else if (sxStores.size() > 1) {
						FileLogHelper.WriteLog("GCS任务回告Error", String.format("查询出多个托盘：%s", gcsOrderReportReqDto.getContainerCode()));
						throw new Exception("查询出多个托盘");
					}else {
						List<PortTemsInfo> portemInfoList = portTemsInfoMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), PortTemsInfo.class);
						if(!portemInfoList.isEmpty()) {
							ztckContainerMapper.updateMapById(gcsOrderReportReqDto.getContainerCode(), MapUtils.put("taskStatus", 20).getMap(), ZtckContainer.class);
						}

						WmsOutboundTask task = wmsOutboundTaskMapper.getNoFinishTask(gcsOrderReportReqDto.getContainerCode());
						if(null == task) {
							return;
						}

						wmsOutboundTaskMapper.updateMapById(task.getId(),MapUtils.put("finished", 50)
								.put("report", 0).getMap(),WmsOutboundTask.class);
					}
				}
			}else {
				//gcs不是最终任务
				//如果有入库任务，则将进度改为50
				//wmsInboundTaskMapper
				//wmsOutboundTaskMapper.updateMapById(task.getId(),MapUtils.put("finished", 50).getMap(),WmsOutboundTask.class);
			}
		}else if(gcsOrderReportReqDto.getWorkStatus() == 1){
			sxPathPlanningTaskService.updateStartTask(gcsOrderReportReqDto.getContainerCode(), gcsOrderReportReqDto.getBillCode(),gcsOrderReportReqDto.getRgvId());
			ztckContainerMapper.deleteByMap(MapUtils.put("containerCode", gcsOrderReportReqDto.getContainerCode()).put("taskType", 50).getMap(), ZtckContainer.class);

			//部分点位离开解锁
			Coordinate coordinate = PrologCoordinateUtils.analysis(gcsOrderReportReqDto.getPosition());
			int sourceLayer = coordinate.getLayer();
			int sourceX = coordinate.getX();
			int sourceY = coordinate.getY();

			//检查是否为暂存点
			List<PortTemsInfo> temsList = portTemsInfoMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), PortTemsInfo.class);
			if(!temsList.isEmpty()) {
				//将任务锁解锁
				portTemsInfoMapper.updateMapById(temsList.get(0).getId(), MapUtils.put("taskLock", 2).getMap(), PortTemsInfo.class);
			}else {
				//先写死，看后面怎么优化，质检口后面一个点位的坐标
				if("0100120066".equals(gcsOrderReportReqDto.getPosition())){
					portInfoMapper.updatePortInfoUnlock(1,12,65);
				}
			}
		}
	}
	
	private void arrivePortTemsInfo(String containerNo,String stations,String junctionPort,int taskType,SxStore sxStore) {
		//修改在途状态
		//如果在途存在则修改在途，如果在途不存在则创建在途
		ZtckContainer ztckContainer = ztckContainerMapper.findById(containerNo, ZtckContainer.class);
		if(null == ztckContainer) {
			//创建在途
			ZtckContainer newZtckContainer = new ZtckContainer();
			newZtckContainer.setContainerCode(containerNo);
			newZtckContainer.setContainerSubCode(sxStore.getContainerSubNo());
			newZtckContainer.setStations(stations);
			//newZtckContainer.setPortNo();
			newZtckContainer.setEntryCode(junctionPort);
			newZtckContainer.setTaskType(taskType);
			newZtckContainer.setTaskStatus(20);
			newZtckContainer.setMaterielNo(sxStore.getTaskProperty1());
			newZtckContainer.setFactoryNo(sxStore.getTaskProperty2());									
			newZtckContainer.setMaterielType(sxStore.getBusinessProperty1());
			newZtckContainer.setMaterielName(sxStore.getBusinessProperty2());
			newZtckContainer.setFactoryCode(sxStore.getBusinessProperty3());
			newZtckContainer.setBoxCount(sxStore.getBusinessProperty4());
			if(null == sxStore.getBusinessProperty5()) {
				newZtckContainer.setDetection(3);
			}else {
				newZtckContainer.setDetection(Integer.valueOf(sxStore.getBusinessProperty5()));
			}
			
			newZtckContainer.setWeight(sxStore.getWeight());
			newZtckContainer.setCreateTime(new Date());

			ztckContainerMapper.save(newZtckContainer);
		}else {
			ztckContainerMapper.updateMapById(containerNo, MapUtils.put("taskStatus", 20).getMap(), ZtckContainer.class);	
		}
	}
	
	private void wmsOutboundTaskComplete(WmsOutboundTask task) {
		
		if(task.getWmsPush() == 0) {
			wmsOutboundTaskMapper.updateMapById(task.getId(),MapUtils.put("finished", 90)
					.put("report", 0).put("endTime", new Date()).getMap(),WmsOutboundTask.class);
		}else {
			wmsOutboundTaskMapper.updateMapById(task.getId(),MapUtils.put("finished", 90)
					.put("report", 1).put("endTime", new Date()).getMap(),WmsOutboundTask.class);
		}
	}

	private void saveMcsTask(List<DeviceJunctionPort> deviceJunctionPortList,String startPosition,String containerNo) throws Exception {
		if(deviceJunctionPortList.size() > 0) {
			String taskId = PrologTaskIdUtils.getTaskId();

			MCSTask mcsTask = new MCSTask();
			mcsTask.setTaskId(taskId);
			mcsTask.setPriority(1);
			mcsTask.setSource(startPosition);
			mcsTask.setStockId(containerNo);
			mcsTask.setTarget("1");
			mcsTask.setType(2);
			mcsTask.setWeight("200");
			mcsTask.setSendCount(0);
			mcsTask.setCreateTime(PrologDateUtils.parseObject(new Date()));
			mcsTask.setTaskState(2);
			
			mcsTaskMapper.save(mcsTask);
		}
	}

	private SxStoreLocation getStoreLocation(int layer,int x,int y) throws Exception {
		List<SxStoreLocation> list = sxStoreLocationMapper.findByMap(MapUtils.put("layer", layer).put("x", x).put("y", y).getMap(), SxStoreLocation.class);
		if(list.isEmpty()) {
			return null;
		}else if(list.size() > 1) {
			throw new Exception("查询出多个货位");
		}else {
			return list.get(0);
		}
	}

	@Override
	public void gcsAlarm(GcsAlarmReqDto gcsAlarmReqDto) throws Exception {
		if(StringUtils.isEmpty(gcsAlarmReqDto.getId())) {
			throw new Exception("id不能为空！");
		}
		if(StringUtils.isEmpty(gcsAlarmReqDto.getTaskId())) {
			throw new Exception("任务单号不能为空！");
		}
		if(StringUtils.isEmpty(gcsAlarmReqDto.getContainerId())) {
			throw new Exception("容器号不能为空！");
		}
		//		if(StringUtils.isEmpty(gcsOrderReportReqDto.getRgvId())) {
		//			throw new Exception("小车编号不能为空！");
		//		}
		if(gcsAlarmReqDto.getErrorType() == 0) {
			throw new Exception("故障类型不能为空！");
		}
	}

	@Override
	@Transactional
	public void saveOrUpdateGcsTask(String containerNo, int layer, int taskType, int priority, String locIdFrom, String locIdTo,
			String uuid,int taskState,int sendCount,String errMsg,int type)throws Exception {
		if(type == 1) {
			GcsTask gcsTask = new GcsTask();
			gcsTask.setCreateTime(PrologDateUtils.parseObject(new Date()));
			gcsTask.setId(uuid);
			gcsTask.setLayer(layer);
			gcsTask.setLocIdFrom(locIdFrom);
			gcsTask.setLocIdTo(locIdTo);
			gcsTask.setPriority(priority);
			gcsTask.setSendCount(sendCount);
			gcsTask.setStockId(containerNo);
			gcsTask.setTaskId(uuid);
			gcsTask.setTaskType(taskType);
			gcsTask.setTaskState(taskState);
			gcsTask.setPriority(priority);
			gcsTask.setErrMsg(errMsg);
			gcsTaskMapper.save(gcsTask);
		}else {
			gcsTaskMapper.updateMapById(uuid, MapUtils.put("errMsg", errMsg).put("sendCount", sendCount+1).getMap(), GcsTask.class);
		}
	}

	@Override
	@Transactional
	public void completeGcsTask(String id) throws Exception {
		GcsTask gcsTask = gcsTaskMapper.findById(id, GcsTask.class);
		GcsTaskHistory gcsTaskHistory = new GcsTaskHistory();
		gcsTaskHistory.setCreateTime(PrologDateUtils.parseObject(new Date()));
		gcsTaskHistory.setErrMsg(gcsTask.getErrMsg());
		gcsTaskHistory.setId(gcsTask.getId());
		gcsTaskHistory.setLayer(gcsTask.getLayer());
		gcsTaskHistory.setLocIdFrom(gcsTask.getLocIdFrom());
		gcsTaskHistory.setLocIdTo(gcsTask.getLocIdTo());
		gcsTaskHistory.setPriority(gcsTask.getPriority());
		gcsTaskHistory.setSendCount(gcsTask.getSendCount());
		gcsTaskHistory.setStockId(gcsTask.getStockId());
		gcsTaskHistory.setTaskId(gcsTask.getTaskId());
		gcsTaskHistory.setTaskState(2);
		gcsTaskHistory.setTaskType(gcsTask.getTaskType());
		gcsTaskHistoryMapper.save(gcsTaskHistory);
		gcsTaskMapper.deleteById(id, GcsTask.class);
	}

	@Override
	public void recall(GcsTask gcsTask) throws Exception {
		try {
			List<GcsTaskPushReqDto> list = new ArrayList<GcsTaskPushReqDto>();
			GcsTaskPushReqDto reqDto = new GcsTaskPushReqDto();
			reqDto.setId(gcsTask.getId());
			reqDto.setContainerId(gcsTask.getStockId());
			reqDto.setCreateTime(PrologDateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			reqDto.setFloor(gcsTask.getLayer());
			reqDto.setLocIdFrom(gcsTask.getLocIdFrom());
			reqDto.setLocIdTo(gcsTask.getLocIdTo());
			reqDto.setPriority(gcsTask.getPriority());
			reqDto.setTaskId(gcsTask.getId());
			reqDto.setTaskType(gcsTask.getTaskType());
			list.add(reqDto);
			String postUrl = String.format("%s%s/%s", gcsUrl, gcsPort, "Interface/TaskOrderFromEis");
			String data = PrologApiJsonHelper.toJson(list);

			FileLogHelper.WriteLog("sendGcsTaskPush", "EIS->GCS请求" + data);
			String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendGcsTaskPush", "EIS->GCS返回" + restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			boolean ret = helper.getBoolean("ret");
			if(ret) {
				this.completeGcsTask(gcsTask.getId());
			}else {
				String msg = helper.getString("msg");
				this.saveOrUpdateGcsTask(gcsTask.getStockId(), gcsTask.getLayer(), gcsTask.getTaskType(), gcsTask.getPriority(),
						gcsTask.getLocIdFrom(), gcsTask.getLocIdTo(), gcsTask.getId(), 2,gcsTask.getSendCount()+1,msg,2);
			}
		} catch (Exception e) {
			this.saveOrUpdateGcsTask(gcsTask.getStockId(), gcsTask.getLayer(), gcsTask.getTaskType(), gcsTask.getPriority(),
					gcsTask.getLocIdFrom(), gcsTask.getLocIdTo(), gcsTask.getId(), 2,gcsTask.getSendCount()+1,e.getMessage(),2);
		}
	}

	@Override
	public List<GcsTask> findRecallGCSTask() throws Exception {
		List<GcsTask> gcsTasks = gcsTaskMapper.findByMap(MapUtils.put("taskState", 2).getMap(), GcsTask.class);
		return gcsTasks;
	}

	@Override
	public void sendCarAcrossReport(String carTaskId) {
		GcsTask gcsTask = gcsTaskMapper.findById(carTaskId, GcsTask.class);
		if(null == gcsTask){
			return;
		}
		try {
			List<GcsTaskPushReqDto> list = new ArrayList<GcsTaskPushReqDto>();
			GcsTaskPushReqDto reqDto = new GcsTaskPushReqDto();
			reqDto.setId(gcsTask.getId());
			reqDto.setContainerId(gcsTask.getStockId());
			reqDto.setCreateTime(PrologDateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			reqDto.setFloor(gcsTask.getLayer());
			reqDto.setLocIdFrom(gcsTask.getLocIdFrom());
			reqDto.setLocIdTo(gcsTask.getLocIdTo());
			reqDto.setPriority(gcsTask.getPriority());
			reqDto.setTaskId(gcsTask.getTaskId());
			reqDto.setTaskType(gcsTask.getTaskType());
			list.add(reqDto);
			String postUrl = String.format("%s%s/%s", gcsUrl, gcsPort, "Interface/CrossLayerFromEis");
			String data = PrologApiJsonHelper.toJson(list);
			String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			boolean ret = helper.getBoolean("ret");
			if(ret) {
				this.completeGcsTask(carTaskId);
			}else {
				String msg = helper.getString("msg");
				gcsTask.setTaskState(2);
				gcsTask.setErrMsg(msg);
				gcsTask.setSendCount(1);
				gcsTaskMapper.update(gcsTask);
			}
		}catch (Exception e){
			String msg = e.getMessage();
			gcsTask.setTaskState(2);
			gcsTask.setErrMsg(msg);
			gcsTask.setSendCount(1);
			gcsTaskMapper.update(gcsTask);
		}
	}


	*//**
	 * 小车跨层任务
	 *//*
	@Override
	public void sendGcsCarAcrossPush(SxCarAcrossTask sxCarAcrossTask, SxCarAcross sxCarAcross) throws Exception {
		if (sxCarAcrossTask.getTaskType() != 1) {
			//提升机状态检测
			boolean check = mcsInterfaceService.checkMcsStatus(sxCarAcrossTask,sxCarAcross);
			if (!check) {
				sxCarAcrossMapper.updateMapById(sxCarAcross.getId(), MapUtils.put("abnormalState", 1).getMap(), SxCarAcross.class);
				FileLogHelper.WriteLog("acrossSchedule", "小车任务异常锁定，原因提升机状态检测未通过:" + check);
				return;
			}
		}
		AssertUtil.notEmpty(sxCarAcross.getCarNo(), "小车编号为空%s,任务id: %s", sxCarAcross.getCarNo(), sxCarAcross.getId());
		sxCarAcrossTask.setSendCount(sxCarAcrossTask.getSendCount() + 1);
		sxCarAcrossTask.setRgvId(sxCarAcross.getCarNo());
		try {
			String postUrl = String.format("%s%s/%s", gcsUrl, gcsPort, "Interface/CrossLayerFromEis");
			String data = PrologApiJsonHelper.toJson(sxCarAcrossTask);
			String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean ret = helper.getBoolean("ret");
			FileLogHelper.WriteLog("acrossCarTask", MessageFormat.format("小车跨层任务发送,参数:{0},返回结果:{1}", data, restJson));
			if (ret) {
				sxCarAcrossTask.setSendStatus(1);
				//发送成功，改变跨层状态
				Integer taskType = sxCarAcrossTask.getTaskType();
				HashMap<String, Object> map = new HashMap<>(1);
				if (taskType == 1) {
					map.put("acrossStatus", 3);
				} else if (taskType == 2) {
					map.put("acrossStatus", 5);
				} else if (taskType == 3) {
					map.put("acrossStatus", 9);
				}
				sxCarAcrossTask.setSendErrMsg("");
				sxCarAcrossMapper.updateMapById(sxCarAcrossTask.getAcrossId(), map, SxCarAcross.class);
			} else {
				String msg = helper.getString("msg");
				sxCarAcrossTask.setSendStatus(2);
				sxCarAcrossTask.setSendErrMsg(msg);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			sxCarAcrossTask.setSendStatus(2);
			sxCarAcrossTask.setSendErrMsg(msg);
		}
		sxCarAcrossTaskMapper.updateMapById(sxCarAcrossTask.getId(), MapUtils.put("sendCount", sxCarAcrossTask.getSendCount()).put("taskStatus", 1)
				.put("sendStatus", sxCarAcrossTask.getSendStatus()).put("sendErrMsg", sxCarAcrossTask.getSendErrMsg()).getMap(), SxCarAcrossTask.class);
	}



	*//**
	 * GCS跨层任务回告
	 *
	 * @param sxCarAcrossTask
	 *//*
	@Override
	@Transactional(rollbackFor = Exception.class)
	public synchronized void crossLayerReport(SxCarAcrossTask sxCarAcrossTask) throws Exception {
		String taskId = sxCarAcrossTask.getTaskId();
		List<SxCarAcrossTask> taskList = sxCarAcrossTaskMapper.findByMap(MapUtils.put("taskId", taskId).put("systype", QcConstant.GCS).getMap(), SxCarAcrossTask.class);
		if (CollectionUtils.isEmpty(taskList)) {
			throw new Exception("EIS未找到对应的GCS任务");
		}
		SxCarAcrossTask acrossTask = taskList.get(0);
		if (acrossTask.getTaskStatus() == 3) {
			throw new Exception("当前GCS任务已完成");
		}
		Integer acrossId = acrossTask.getAcrossId();
		SxCarAcross carAcross = sxCarAcrossMapper.findById(acrossId, SxCarAcross.class);
		if (carAcross == null) {
			throw new Exception("EIS未找到对应的小车跨层任务");
		}
		//效验任务，与当前跨层任务状态是否匹配
		carAcrossLayerService.checkSxCarAcrossTask(acrossTask, carAcross);

		String rgvId = sxCarAcrossTask.getRgvId();
		if (sxCarAcrossTask.getTaskType() == 1) {
			List<SxCar> cars = sxCarMapper.findByMap(MapUtils.put("id", rgvId).getMap(), SxCar.class);
			if (StringUtils.isEmpty(rgvId) || CollectionUtils.isEmpty(cars)) {
				FileLogHelper.WriteLog("CrossLayerReport", "GCS->EIS GCS跨层任务回告,错误:" +
						AssertUtil.format("acrossSchedule crossLayerReport end error 小车编号为空或小车编号在EIS系统不存在 :%s",rgvId));
				throw new Exception("小车编号为空或小车编号在EIS系统不存在");
			}
		}
		Integer workStatus = sxCarAcrossTask.getWorkStatus();
		if (workStatus == 1) {
			if (sxCarAcrossTask.getTaskType() == 1) {
				sxCarAcrossMapper.updateMapById(acrossId, MapUtils.put("carNo", rgvId).getMap(), SxCarAcross.class);
				carAcross.setCarNo(rgvId);
				//修改四向小车状态
				//zwyAcrossLayerService.updateSxCarStatus(carAcross, 1);
			}
			//开始
			sxCarAcrossTaskMapper.updateMapById(acrossTask.getId(), MapUtils.put("taskStatus", 2).put("startTime", sxCarAcrossTask.getCreateTime()).getMap(), SxCarAcrossTask.class);
			return;
		} else if (workStatus == 2) {
			Integer taskStatus = acrossTask.getTaskStatus();
			if (taskStatus != 2) {
				FileLogHelper.WriteLog("CrossLayerReport", "GCS->EIS GCS跨层任务回告,错误:" +
						AssertUtil.format("acrossSchedule crossLayerReport end error 任务未回告开始,或设备故障 :%s",taskStatus));
				throw new Exception("任务未回告开始,或设备故障");
			}
			//完成
			sxCarAcrossTaskMapper.updateMapById(acrossTask.getId(), MapUtils.put("taskStatus", 3).put("finishTime", sxCarAcrossTask.getCreateTime()).getMap(), SxCarAcrossTask.class);
			//创建下一步任务
			carAcrossLayerService.createSxCarAcrossTaskForGcs(acrossTask, carAcross);
		}
	}


	@Override
	public void syncGcsCar() throws Exception {
		String postUrl = String.format("%s%s/%s", gcsUrl, gcsPort, "Interface/getRgvData");
		String restJson = restTemplate.postForObject(postUrl, null, String.class);
		List<GcsCarDto> gcsCarDtos = JSON.parseArray(restJson, GcsCarDto.class);
		//过滤id为空
		List<GcsCarDto> gcsCarDtoList = gcsCarDtos.stream().filter(t -> !StringUtils.isEmpty(t.getId())).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(gcsCarDtoList)) {
			return;
		}
		List<SxCar> sxCars = Lists.newArrayList();
		SxCar sxCar;
		for (GcsCarDto gcsCarDto : gcsCarDtos) {
			//转换对象
			sxCar = new SxCar();
			sxCar.setId(gcsCarDto.getId());
			sxCar.setLayer(gcsCarDto.getFloor());
			//默认写1
			sxCar.setBelongArea(Integer.valueOf(gcsCarDto.getArea()));
			sxCar.setCurrCoord(gcsCarDto.getCurrCoord());
			sxCar.setLastUpdateTime(new Date());
			sxCar.setAlarm(gcsCarDto.getAlarm());
//			sxCar.setLeisure(gcsCarDto.isLeisure());
//			sxCar.setAuto(gcsCarDto.isAuto());
//			sxCar.setOnline(gcsCarDto.isOnline());
//			sxCar.setUse(gcsCarDto.isUse());
			sxCars.add(sxCar);
		}
		//同步
		sxCarService.syncGcsCar(sxCars);
	}*/
}
