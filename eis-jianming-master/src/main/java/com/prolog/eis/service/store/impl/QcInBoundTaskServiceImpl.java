package com.prolog.eis.service.store.impl;

import org.springframework.stereotype.Service;

@Service
public class QcInBoundTaskServiceImpl {

	/*@Autowired
	private SxHoisterConfigMapper sxHoisterConfigMapper;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	@Autowired
	private SxInStoreService sxInStoreService;
	@Autowired
	private SysParameMapper sysParameMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	@Autowired
	private SxPathPlanningTaskService sxPathPlanningTaskService;
	@Autowired
	private PalletInfoMapper palletInfoMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private LedMessageService ledMessageService;
	@Autowired
	private ZtContainerMsgService ztContainerMsgService;
	@Autowired
	private ThroughTaskMapper throughTaskMapper;
	@Autowired
	private QcInBoundReturnExcuteService qcInBoundReturnExcuteService;
	@Autowired
	private LayerPortOriginService layerPortOriginService;
	@Autowired
	private SysParameService sysParameService;
	@Autowired
	private EmptyCaseConfigService emptyCaseConfigService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public McsRequestTaskDto inBoundTask(InBoundRequest inBoundRequest) throws Exception {

		String containerNo = inBoundRequest.getStockId();
		String containerSubNo = inBoundRequest.getStockIdSub();
		String source = inBoundRequest.getSource();
		Coordinate coordinate = PrologCoordinateUtils.analysis(source);
		int sourceLayer = coordinate.getLayer();
		int sourceX = coordinate.getX();
		int sourceY = coordinate.getY();
		int detection = inBoundRequest.getDetection();

		Double weight = 200d;
		if(!StringUtils.isEmpty(inBoundRequest.getWeight())) {
			weight = Double.valueOf(inBoundRequest.getWeight());
		}

		//校验重量
		List<SysParame> weightSysParame = sysParameMapper.findByMap(MapUtils.put("parameNo", "LIMIT_WEIGHT").getMap(),
				SysParame.class);
		Double limitWeight = Double.valueOf(weightSysParame.get(0).getParameValue());

		// 找port口
		List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), PortInfo.class);
		if(portInfos.isEmpty()) {
			return null;
		}

		//入庫驗證
		PortInfo portInfo = portInfos.get(0);
		int validateType = 2;
		if(portInfo.getTaskType() == 3) {
			validateType = 1;
		}
		InStoreValidateDto result = this.getValidateResult(portInfo.getDetection(),validateType,containerNo,containerSubNo,portInfo.getWmsPortNo(),portInfo.getJunctionPort(),detection);
		if(!result.isSuccess()){
			if(portInfo.getShowLed() == 1) {
				this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,result.getMsg());
			}

			if(result.isDetection()) {
				//外观检测20的时候不做处理
				if(detection == 0 && portInfo.getReback() == 1) {
					return this.addMcsTask(false,containerNo,source,"-1",result.getMsg());
				}else {
					return null;
				}
			}else {
				if(portInfo.getReback() == 1) {
					return this.addMcsTask(false,containerNo,source,"-1",result.getMsg());
				}else {
					return null;
				}
			}
		}

		//判斷是否添加空托入庫任務
		if(result.getResultType() == 1) {
			WmsInboundTask task = this.buildEmptyInboundTask(portInfo,containerNo);
			result.setWmsInboundTask(task);
		}
		
		//验证入库库存
		if(null != result.getWmsInboundTask()) {
			WmsInboundTask wmsInboundTask = result.getWmsInboundTask();
			Integer locationId = this.checkHuoWei("","",wmsInboundTask.getTaskType(),wmsInboundTask.getMatType(),containerNo,sourceLayer,detection,portInfo.getJunctionPort());
			if(null == locationId) {
				if(portInfo.getShowLed() == 1) {
					this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
				}
				if(portInfo.getReback() == 1) {
					return this.addMcsTask(false,containerNo,source,"-1","貨位不足！！！");
				}
			}
		}

		if(result.getResultType() == 2) {
			result.getWmsInboundTask().setWeight(weight);

			wmsInboundTaskMapper.updateMapById(result.getWmsInboundTask().getId(), 
					MapUtils.put("weight", weight)
					.put("containerCode",containerNo)
					.put("detection", detection)
					.put("junctionPort", portInfo.getJunctionPort()).getMap(), WmsInboundTask.class);
		}

		if(portInfo.getShowLed() == 1) {
			this.addLedMsg(portInfo.getId(),portInfo.getPortType(),0,containerSubNo + "入庫成功");
		}

		if(portInfo.getPosition() == 1) {
			//西碼頭入庫需要給出前進到bcr指令
			return this.addMcsTask(true,containerNo,source,"0100360011","");
		}

		if(result.getResultType() == 1) {
			//空托入庫
			ztckContainerMapper.deleteByMap(MapUtils.put("containerCode", containerNo).getMap(), ZtckContainer.class);
			this.inSxStore(result.getWmsInboundTask(),portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
			return null;
		}

		if(result.getResultType() == 2) {
			// 质检口入库,清除质检等待状态的对应在途库存
			List<ZtckContainer> ztckContainers = ztckContainerMapper.findByMap(MapUtils.put("taskType", 70).put("containerSubCode", containerSubNo).getMap(), ZtckContainer.class);
			if(!ztckContainers.isEmpty()) {
				ztckContainerMapper.deleteById(ztckContainers.get(0).getContainerCode(), ZtckContainer.class);
			}
			ztckContainerMapper.deleteByMap(MapUtils.put("containerCode", containerNo).getMap(), ZtckContainer.class);
			ztckContainerMapper.deleteByMap(MapUtils.put("containerSubCode", containerSubNo).getMap(), ZtckContainer.class);
			//任務托入庫
			this.inSxStore(result.getWmsInboundTask(),portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
			return null;
		}

		if(result.getResultType() == 3) {
			//借道


			return null;
		}

		return null;
	}

	*//**
	 * 入庫驗證
	 * @param validateType  1 空托入庫驗證  2 字母托入庫驗證
	 * @param containerNo
	 * @param containerSubNo
	 * @param wmsPortNo
	 * @param junctionPort
	 * @return
	 *//*
	private InStoreValidateDto getValidateResult(int isdetection, int validateType, String containerNo,String containerSubNo,String wmsPortNo,String junctionPort,int detection) {

		InStoreValidateDto result = new InStoreValidateDto();

		if(detection == 20 || detection == 0) {
			String mString = String.format("母托%s超高超寬異常", containerNo);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setDetection(true);
			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		PalletInfo palletInfo = palletInfoMapper.findById(containerNo, PalletInfo.class);
		if(null == palletInfo) {
			String mString = String.format("母托%s無資料", containerNo);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		WmsInboundTask inStask2 = wmsInboundTaskMapper.getRkStartParentWmsInboundTask(containerNo);
		if(null != inStask2) {
			String mString = String.format("母托%s存在入庫任務", containerNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		ThroughTask mThroughTask = throughTaskMapper.getRkNoStartParentThroughTask(containerNo);
		if(null != mThroughTask) {
			String mString = String.format("母托%s存在借道任務", containerNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		//驗證母托庫存
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
		if(sxStores.size()>0) {
			//存在同母托
			String mString = String.format("母托%s存在庫存", containerNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);

			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);
			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		if(validateType == 1) {
			result.setSuccess(true);
			result.setResultType(1);
			return result;
		}

		//檢查是否為空拖入庫任務
		WmsInboundTask emptyCaseInStask = wmsInboundTaskMapper.getRkNoStartParentWmsInboundTask(containerNo);
		if(null != emptyCaseInStask) {
			if(emptyCaseInStask.getTaskType() == 30) {
				result.setSuccess(true);
				result.setResultType(1);
				return result;
			}else {
				//需要清除之前的母托任务
				wmsInboundTaskMapper.updateMapById(emptyCaseInStask.getId(), MapUtils.put("containerCode", null).getMap(), WmsInboundTask.class);
			}
		}

		//检查子托有无入库任务
		WmsInboundTask inStask =  wmsInboundTaskMapper.getRkNoStartWmsInboundTask(containerSubNo);
		if(null != inStask) {			
			if("VMI".equals(inStask.getMatType())) {
				if(detection > 3) {
					String mString = String.format("母托%s超高異常", containerNo);
					ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

					result.setSuccess(false);
					result.setMsg(mString);
					return result;
				}
			}else if("INX".equals(inStask.getMatType())){
				if(detection > 4) {
					String mString = String.format("母托%s超高異常", containerNo);
					ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

					result.setSuccess(false);
					result.setMsg(mString);
					return result;
				}
			}else {
				String mString = String.format("子托%s数据异常", containerSubNo);
				ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

				result.setSuccess(false);
				result.setMsg(mString);
				return result;
			}

			result.setSuccess(true);
			result.setResultType(2);
			result.setWmsInboundTask(inStask);
			return result;
		}

		//無入庫任務，檢查是否存在借道任務
		ThroughTask sThroughTask = throughTaskMapper.getRkNoStartThroughTask(containerSubNo);
		if(null != sThroughTask) {
			result.setSuccess(true);
			result.setResultType(3);
			result.setThroughTask(sThroughTask);
			return result;
		}


		//驗證子托有無庫存
		//驗證母托庫存
		List<SxStore> sxStores2 = sxStoreMapper.findByMap(MapUtils.put("containerSubNo", containerSubNo).getMap(), SxStore.class);
		if(sxStores2.size()>0) {
			//存在同母托
			String mString = String.format("子托%s存在庫存", containerSubNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);

			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);
			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		//無借道任務檢查是否字母托盤全為空托任務
		//檢查子托盤有無母托入庫任務
		PalletInfo palletSubInfo = palletInfoMapper.findById(containerSubNo, PalletInfo.class);
		if(null == palletSubInfo) {
			String mString = String.format("子托%s無任務", containerSubNo);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		WmsInboundTask inMtask =  wmsInboundTaskMapper.getRkStartParentWmsInboundTask(containerSubNo);
		if(null != inMtask) {
			String mString = String.format("子托%s存在入庫任務", containerSubNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		if(containerNo.equals(containerSubNo)) {
			String mString = String.format("子托和母托相同");
			FileLogHelper.WriteLog("mcsRequestError", mString);
			ztContainerMsgService.saveAndUpdate(containerNo, containerSubNo, wmsPortNo, junctionPort, mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}else {
			//需要生成空托任務
			result.setSuccess(true);
			result.setResultType(1);

			return result;	
		}
	}

	private McsRequestTaskDto addMcsTask(boolean success,String stockId,String source,String target,String errorMessage) {
		McsRequestTaskDto mcsSendTaskDto = new McsRequestTaskDto();
		mcsSendTaskDto.setSuccess(success);
		mcsSendTaskDto.setStockId(stockId);
		mcsSendTaskDto.setTarget(target);
		mcsSendTaskDto.setSource(source);
		mcsSendTaskDto.setErrorMessage(errorMessage);

		return mcsSendTaskDto;
	}

	private void inSxStore(WmsInboundTask wmsInboundTask,PortInfo portInfo,String containerNo,String source,int sourceLayer,int sourceX,int sourceY,int detection) throws Exception {

		Integer locationId = this.checkHuoWei(wmsInboundTask.getMaterielNo(),wmsInboundTask.getFactoryNo(), wmsInboundTask.getTaskType(),wmsInboundTask.getMatType(),containerNo,sourceLayer,detection,portInfo.getJunctionPort());
		if(null == locationId) {
			if(portInfo.getShowLed() == 1) {
				this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
			}
			if(portInfo.getReback() == 1) {
				this.addMcsTask(false,containerNo,source,"-1","貨位不足！！！");
			}
			return;
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
		sxStore.setBusinessProperty5(String.valueOf(detection));

		if(wmsInboundTask.getTaskType() == 30){
			sxStore.setTaskType(-1);//空托盘
		}else {
			sxStore.setTaskType(wmsInboundTask.getTaskType());//暂不知道有什么意义
		}
		sxStore.setWeight(wmsInboundTask.getWeight());
		sxStore.setCreateTime(PrologDateUtils.parseObject(new Date()));
		sxStore.setInStoreTime(PrologDateUtils.parseObject(new Date()));
		sxStoreMapper.save(sxStore);
		sxStoreLocationMapper.updateMapById(locationId, MapUtils.put("actualWeight", wmsInboundTask.getWeight()).getMap(), SxStoreLocation.class);
		sxStoreLocationGroupMapper.updateMapById(sxStoreLocation.getStoreLocationGroupId(),
				MapUtils.put("ascentLockState", 1).getMap(), SxStoreLocationGroup.class);
		sxStoreTaskFinishService.computeLocation(sxStore);

		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
		//调用生成路径的方法
		List<SxPathParameDto> startList = new ArrayList<SxPathParameDto>();
		SxPathParameDto start = new SxPathParameDto();
		start.setParameType(1);
		start.setLayer(sourceLayer);
		start.setConnectionNo(portInfo.getJunctionPort());
		//start.setHoisterNo(hositorNo);
		start.setX(sourceX);
		start.setY(sourceY);
		start.setRegionNo("1");
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
					.put("containerCode", containerNo)
					.put("binNo", "000000")
					.put("report",wmsInboundTask.getWmsPush())
					.put("shfSd", 0).getMap(), WmsInboundTask.class);
		}else {
			wmsInboundTaskMapper.updateMapById(wmsInboundTask.getId(),
					MapUtils.put("finished", 20)
					.put("containerCode", containerNo)
					.put("binNo", sxStoreLocation.getWmsStoreNo())
					.put("report",wmsInboundTask.getWmsPush())
					.put("shfSd", sxStoreLocation.getDepth()).getMap(), WmsInboundTask.class);	
		}
	}

	private Integer checkHuoWei(String liaohao,String lot,int taskType,String matType,String containerNo,int sourceLayer,int detection,String entryCode) throws Exception {
		List<List<Integer>> layerGroups = new ArrayList<>();

		//获取入库层 
		if(taskType == 30){
			//空托盘			
			layerGroups = emptyCaseConfigService.getEmptyCaseLayer(sourceLayer,1,7);
		}else {
			if("VMI".equals(matType)){
				//海关1，2层				
				List<Integer> layers = new ArrayList<Integer>();

				layers.add(1);
				layers.add(2);

				layerGroups.add(layers);
			}else if("INX".equals(matType)){
				//非海关34567层
				layerGroups = DetetionLayerHelper.getLayers(detection,3,7);
			}else {
				throw new Exception(String.format("容器：%s任务异常:%s", containerNo,matType));
			}
		}

		int reservedLocation = 0;
		if(taskType == 30){
			//空托
			reservedLocation = 1;
		}else {
			//任务托
			reservedLocation = 2;
		}
		
		//查找货位
		Integer locationId = null;
		for (List<Integer> layers : layerGroups) {
			try {
				if(layers.isEmpty()) {
					continue;
				}

				int reserveCount = sysParameService.getLayerReserveCount(layers.get(0));

				Integer findLayer = sxInStoreService.findLayer(0,layers, reserveCount, "", "");

				LayerPortOrigin layerPortOrigin = layerPortOriginService.getPortOrigin(entryCode,findLayer,50,50);
				
				locationId = sxInStoreService.getInStoreDetail(containerNo, findLayer, liaohao,
						lot, layerPortOrigin.getOriginX(), layerPortOrigin.getOriginY(), reservedLocation,1,200,reserveCount);
				if(null != locationId) {
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				//繼續尋找其他層
			}
		}
		return locationId;
	}

	private LayerPortOrigin getPortOrigin(String junctionPort,int layer,int defaultOriginX,int defaultOriginY){
		//获取配置的原点
		//
		List<LayerPortOrigin> layerPortOrigins = layerPortOriginMapper.findByMap(MapUtils.put("entryCode", junctionPort).put("layer", layer).getMap(), LayerPortOrigin.class);
		if(layerPortOrigins.isEmpty()) {
			//记录下哪些接驳点层没有设置原点
			FileLogHelper.WriteLog("getPortOrigin原点配置", "接驳点" + junctionPort + "层" + layer + "未设置原点");

			LayerPortOrigin layerPortOrigin = new LayerPortOrigin();
			layerPortOrigin.setOriginX(defaultOriginX);
			layerPortOrigin.setOriginY(defaultOriginY);

			return layerPortOrigin;
		}else {
			return layerPortOrigins.get(0);
		}
	}

	private WmsInboundTask buildEmptyInboundTask(PortInfo portInfo,String containerNo) {

		Double weight = 200d;

		//检查当前有无未开始的母托托盘
		List<WmsInboundTask> hasEmptyInBoundTasks = wmsInboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).put("finished", 0).getMap(), WmsInboundTask.class);
		if(!hasEmptyInBoundTasks.isEmpty()) {
			WmsInboundTask hasEmptyInBoundTask = hasEmptyInBoundTasks.get(0);
			hasEmptyInBoundTask.setWeight(weight);
			wmsInboundTaskMapper.updateMapById(hasEmptyInBoundTask.getId(), 
					MapUtils.put("containerCode", containerNo)
					.put("weight", weight)
					.put("junctionPort", portInfo.getJunctionPort())
					.getMap(), WmsInboundTask.class);

			return hasEmptyInBoundTask;
		}

		//检查本身有无当前port口的空拖入库任务
		//如果有则找一条修改母托盘编号
		List<WmsInboundTask> emptyInBoundTasks = wmsInboundTaskMapper.getRkEmptyWmsInboundTask(portInfo.getWmsPortNo());
		if(emptyInBoundTasks.isEmpty()) {
			WmsInboundTask task = new WmsInboundTask();
			task.setCommandNo(PrologStringUtils.newGUID());
			task.setWmsPush(0);
			task.setWhNo("HA_WH");
			task.setAreaNo("HAC_ASRS");
			task.setTaskType(30);
			task.setContainerCode(containerNo);
			task.setPalletSize("P");
			task.setWeight(weight);
			task.setPortNo(portInfo.getWmsPortNo());
			task.setFinished(0);
			task.setReport(0);
			task.setCreateTime(new Date());

			wmsInboundTaskMapper.save(task);

			return task;
		}else {
			WmsInboundTask emptyInBoundTask = emptyInBoundTasks.get(0);
			emptyInBoundTask.setWeight(weight);
			wmsInboundTaskMapper.updateMapById(emptyInBoundTask.getId(), 
					MapUtils.put("containerCode", containerNo)
					.put("weight", weight)
					.getMap(), WmsInboundTask.class);

			return emptyInBoundTask;
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
	public void taskReturn(InBoundRequest inBoundRequest) throws Exception{

		if(inBoundRequest.getTarget().equals("-1") || inBoundRequest.getTarget().equals("1")) {
			return;
		}

		Coordinate coordinate = PrologCoordinateUtils.analysis(inBoundRequest.getTarget());
		int targetLayer = coordinate.getLayer();
		int targetX = coordinate.getX();
		int targetY = coordinate.getY();

		if(inBoundRequest.getStatus()==2){
			//根据出入库任务表判断会告的任务是出库任务还是入库任务
			List<WmsOutboundTask> tasks = wmsOutboundTaskMapper.findByMap(MapUtils.put("finished", 10).put("containerCode", inBoundRequest.getStockId()).getMap(), WmsOutboundTask.class);
			if(tasks.size() > 1) {
				FileLogHelper.WriteLog("mcstaskReturnError", String.format("子托%s存在多个wms出库任务", inBoundRequest.getStockId()));

				return;
			}else if(tasks.size() == 1) {
				//质检，一般出库
				qcInBoundReturnExcuteService.taskReturnOutBound(tasks.get(0),inBoundRequest.getStockId(),inBoundRequest.getTarget(),targetLayer,targetX,targetY);
				return;
			}

			//判断有无入库任务
			List<WmsInboundTask> wmsInboundTasklist = wmsInboundTaskMapper.findByMap(MapUtils.put("containerCode", inBoundRequest.getStockId()).getMap(), WmsInboundTask.class);
			if(!wmsInboundTasklist.isEmpty()) {
				WmsInboundTask task = wmsInboundTasklist.get(0);

				//暂时写死//12号提升机的右侧接驳口设置位路径起点
				List<String> rkInStorePositions = wmsInboundTaskMapper.getRkInStorePositions(targetLayer,targetX,targetY);
				if(!rkInStorePositions.isEmpty()) {
					List<SxConnectionRim> sxHoisterConfigs = sxHoisterConfigMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), SxConnectionRim.class);
					if(!sxHoisterConfigs.isEmpty()) {
						//需要入库的托盘
						synchronized ("kucun".intern()) {
							qcInBoundReturnExcuteService.taskReturnInbound(sxHoisterConfigs.get(0),task,inBoundRequest.getTaskId(),task.getWeight(),inBoundRequest.getStockId(),inBoundRequest.getTarget(),targetLayer,targetX,targetY);	
						}
					}
				}else {
					List<SxConnectionRim> sxHoisterConfigs = sxHoisterConfigMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), SxConnectionRim.class);
					if(!sxHoisterConfigs.isEmpty()) {
						//直接入库
						qcInBoundReturnExcuteService.updateCompleteTask(inBoundRequest.getStockId(), sxHoisterConfigs.get(0).getEntryCode(),true);	
					}
				}
			}

			//判斷是否有借道任務
			List<ThroughTask> throughTaskLst = throughTaskMapper.findByMap(MapUtils.put("containerCode", inBoundRequest.getStockId()).getMap(), ThroughTask.class);
			if(!throughTaskLst.isEmpty()) {
				List<String> rkInStorePositions = wmsInboundTaskMapper.getRkInStorePositions(targetLayer,targetX,targetY);
				if(!rkInStorePositions.isEmpty()) {
					//生成借道任務
					//
					List<SxConnectionRim> sxConnectionRims = sxHoisterConfigMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), SxConnectionRim.class);
					if(sxConnectionRims.isEmpty()) {
						return;
					}

					//檢查目標叫料解包區
					List<PortInfoDto> portInfos = portInfoMapper.getSxkStationPort(throughTaskLst.get(0).getEndStations());
					List<PortInfoDto> ckPortInfos = ListHelper.where(portInfos, p->p.getPortType() == 2);

					if(ckPortInfos.isEmpty())
						return;

					//查询出各个port口的出库托盘任务数
					List<PortTaskCount> taskCountList = portInfoMapper.getPortCkTaskCount();
					List<PortInfoDto> portInfoDtos = getPortTaskMinCount(ckPortInfos,taskCountList);
					if(!portInfoDtos.isEmpty()) {
						//String nextPostition = StringUtils.join(ListHelper.select(portlist, p->p.getJunctionPort()), "/");
						// 验证出库任务是否锁定
						for (PortInfoDto portInfoDto : portInfoDtos) {
							try {
								qcInBoundReturnExcuteService.excuteThroughTask(sxConnectionRims.get(0),inBoundRequest.getStockId(),coordinate,portInfoDto);
								break;
							} catch (Exception e) {
								// TODO: handle exception
								FileLogHelper.WriteLog("throughTaskTask", e.getMessage());
							}
						}
					}
				}
			}

			List<PortInfo> ports = portInfoMapper.findByMap(MapUtils.put("layer",targetLayer).put("x", targetX).put("y", targetY).getMap(), PortInfo.class);
			if(ports.size() > 0) {
				qcInBoundReturnExcuteService.arrivePort(inBoundRequest.getStockId(),ports.get(0));
			}
		}else if(inBoundRequest.getStatus() == 1){

			qcInBoundReturnExcuteService.setTaskStart(inBoundRequest.getTaskId());
		}
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
	}*/
}
