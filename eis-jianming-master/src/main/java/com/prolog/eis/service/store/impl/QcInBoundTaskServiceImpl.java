package com.prolog.eis.service.store.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.base.SysParameMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.dao.wms.InboundTaskMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.dto.eis.InStoreValidateDto;
import com.prolog.eis.dto.eis.mcs.InBoundRequest;
import com.prolog.eis.dto.eis.mcs.McsRequestTaskDto;
import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.base.SysParameService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.sxk.SxInStoreService;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.eis.util.detetionlayer.DetetionLayerHelper;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;

@Service
public class QcInBoundTaskServiceImpl implements QcInBoundTaskService{

	@Autowired
	private InboundTaskMapper inboundTaskMapper;
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
	private PortInfoMapper portInfoMapper;
	@Autowired
	private ContainerTaskMapper containerTaskMapper;
	@Autowired
	private AgvStorageLocationMapper agvStorageLocationMapper;
	@Autowired
	private SysParameService sysParameService;
	@Autowired
	private EisCallbackService eisCallbackService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public McsRequestTaskDto inBoundTask(InBoundRequest inBoundRequest) throws Exception {

		String containerNo = inBoundRequest.getStockId();
		//建民没有子托盘
		//String containerSubNo = inBoundRequest.getStockIdSub();
		String source = inBoundRequest.getSource();
		Coordinate coordinate = PrologCoordinateUtils.analysis(source);
		int sourceLayer = coordinate.getLayer();
		int sourceX = coordinate.getX();
		int sourceY = coordinate.getY();
		int detection = inBoundRequest.getDetection();

		SysParame sysParame = sysParameMapper.findById("LIMIT_WEIGHT", SysParame.class);
		Double limitWeight = Double.valueOf(sysParame.getParameValue());

		//校验重量
		//验证超重
		Double weight = 0d;
		if(!StringUtils.isEmpty(inBoundRequest.getWeight())) {
			weight = Double.valueOf(inBoundRequest.getWeight());
		}

		if(weight > limitWeight) {
			return this.addMcsTask(false,containerNo,source,"-1",inBoundRequest.getStockId()+"托盘超重");
		}

		// 找port口
		List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), PortInfo.class);
		if(portInfos.isEmpty()) {
			return this.addMcsTask(false,containerNo,source,"-1","入库口"+source+"不存在");
		}

		//入庫驗證
		PortInfo portInfo = portInfos.get(0);

		InStoreValidateDto result = this.getValidateResult(portInfo.getDetection(),containerNo,portInfo.getWmsPortNo(),portInfo.getJunctionPort(),portInfo.getTaskType(),detection);
		if(!result.isSuccess()){
			if(portInfo.getShowLed() == 1) {
				//后续看是否有和led对接的方法
				//this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,result.getMsg());
			}

			if(portInfo.getReback() == 1) {
				return this.addMcsTask(false,containerNo,source,"-1",result.getMsg());
			}else {
				return null;
			}
		}

		if(result.getResultType() == 2) {
			//入库成功，删除agv区域的托盘
			clearAgvLocationComtainer(containerNo);
			//生成库存并入库
			this.taskContainerInSxStore(result.getInboundTask(),weight,portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
			return null;
		}else if(result.getResultType() == 1){
			//生成库存并入库
			this.emptyContainerInSxStore(result.getInboundTask(),weight,portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
			return null;
		}

		return null;
	}

	/**
	 * 入庫驗證
	 * @param validateType  1 空托入庫驗證  2 字母托入庫驗證
	 * @param containerNo
	 * @param wmsPortNo
	 * @param junctionPort
	 * @return
	 */
	private InStoreValidateDto getValidateResult(int isdetection, String containerNo,String wmsPortNo,String junctionPort,int taskType,int detection) {

		InStoreValidateDto result = new InStoreValidateDto();

		List<InboundTask> inboundTasks = inboundTaskMapper.getRkStartInboundTask(containerNo);
		if(!inboundTasks.isEmpty()) {
			String mString = String.format("托盘%s已在库内", containerNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
		if(!sxStores.isEmpty()) {			
			String mString = String.format("托盘%s已在库内", containerNo);
			FileLogHelper.WriteLog("mcsRequestError", mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		//判断入库口类型
		if(taskType == 3) {
			//空托入库口//创建空托入库任务
			InboundTask inboundTask = new InboundTask();
			inboundTask.setBillNo(PrologStringUtils.newGUID());
			inboundTask.setWmsPush(0);
			inboundTask.setReBack(0);
			inboundTask.setEmptyContainer(0);
			inboundTask.setContainerCode(PrologStringUtils.newGUID());
			inboundTask.setTaskType(0);
			inboundTask.setItemId("空托");
			inboundTask.setQty(0);
			inboundTask.setTaskState(3);
			inboundTask.setCreateTime(new Date());
			inboundTask.setStartTime(new Date());
			inboundTask.setRukuTime(new Date());
			inboundTaskMapper.save(inboundTask);

			result.setInboundTask(inboundTask);
			result.setSuccess(true);
			result.setResultType(1);

			return result;
		}else {
			//检查是否存在入库任务
			List<InboundTask> temInboundTasks = inboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).getMap(), InboundTask.class);
			if(temInboundTasks.isEmpty()) {
				String mString = String.format("托盘%s无入库任务", containerNo);
				FileLogHelper.WriteLog("mcsRequestError", mString);

				result.setSuccess(false);
				result.setMsg(mString);
				return result;
			}	

			result.setInboundTask(temInboundTasks.get(0));
			result.setSuccess(true);
			result.setResultType(2);

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

	private void clearAgvLocationComtainer(String containerNo) {
		containerTaskMapper.deleteByMap(MapUtils.put("containerCode", containerNo).getMap(), ContainerTask.class);
	}

	private void emptyContainerInSxStore(InboundTask inboundTask,double weight,PortInfo portInfo,String containerNo,String source,int sourceLayer,int sourceX,int sourceY,int detection) throws Exception {
		Integer locationId = this.checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),containerNo,sourceLayer,detection,portInfo.getJunctionPort(),1,1);
		if(null == locationId) {
			if(portInfo.getShowLed() == 1) {
				//this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
			}
			if(portInfo.getReback() == 1) {
				this.addMcsTask(false,containerNo,source,"-1","貨位不足！！！");
			}
			return;
		}

		//生成入库库存
		this.buildRuKuSxStore(locationId,inboundTask,containerNo,weight);
	}

	private void taskContainerInSxStore(InboundTask inboundTask,double weight,PortInfo portInfo,String containerNo,String source,int sourceLayer,int sourceX,int sourceY,int detection) throws Exception {

		Integer locationId = this.checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),containerNo,sourceLayer,detection,portInfo.getJunctionPort(),1,3);
		if(null == locationId) {
			if(portInfo.getShowLed() == 1) {
				//this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
			}
			if(portInfo.getReback() == 1) {
				this.addMcsTask(false,containerNo,source,"-1","貨位不足！！！");
			}
			return;
		}

		//生成入库库存
		this.buildRuKuSxStore(locationId,inboundTask,containerNo,weight);
	}

	private void buildRuKuSxStore(Integer locationId,InboundTask inboundTask,String containerNo,Double weight) throws Exception {
		//生成入库库存
		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationId, SxStoreLocation.class);
		// 1.保存到箱库库存2.锁定货位组(修改货位组升位锁)
		SxStore sxStore = new SxStore();
		sxStore.setStoreLocationId(locationId);
		//库存任务类型 1 wms库存 2 eis库存
		if(inboundTask.getWmsPush() == 0) {
			sxStore.setSxStoreType(2);
		}else if(inboundTask.getWmsPush() == 1) {
			sxStore.setSxStoreType(1);
		}
		sxStore.setContainerNo(containerNo);
		sxStore.setStoreState(10);
		sxStore.setTaskProperty1(inboundTask.getOwnerId() + "and" + inboundTask.getItemId());
		sxStore.setTaskProperty2(inboundTask.getLotId());

		if(inboundTask.getEmptyContainer() == 1){
			sxStore.setTaskType(-1);//空托盘
		}else {
			sxStore.setTaskType(1);//任务托
		}
		sxStore.setOwnerId(inboundTask.getOwnerId());
		sxStore.setItemId(inboundTask.getItemId());
		sxStore.setLotId(inboundTask.getLotId());
		sxStore.setQty(inboundTask.getQty());
		sxStore.setWeight(weight);
		sxStore.setCreateTime(new Date());
		sxStore.setInStoreTime(new Date());
		sxStoreMapper.save(sxStore);
		sxStoreLocationMapper.updateMapById(locationId, MapUtils.put("actualWeight", weight).getMap(), SxStoreLocation.class);
		sxStoreLocationGroupMapper.updateMapById(sxStoreLocation.getStoreLocationGroupId(),
				MapUtils.put("ascentLockState", 1).getMap(), SxStoreLocationGroup.class);
		sxStoreTaskFinishService.computeLocation(sxStore);
	}

	private Integer checkHuoWei(String itemId,String lot,String containerNo,int sourceLayer,int detection,String entryCode,int minLayer,int maxLayer) throws Exception {
		List<List<Integer>> layerGroups = DetetionLayerHelper.getLayers(detection,minLayer,maxLayer);

		//查找货位
		Integer locationId = null;
		SysParame pOriginX = sysParameMapper.findById("originX", SysParame.class);
		SysParame pOriginY = sysParameMapper.findById("originY", SysParame.class);

		int originX = Integer.valueOf(pOriginX.getParameValue());
		int originY = Integer.valueOf(pOriginY.getParameValue());

		for (List<Integer> layers : layerGroups) {
			try {
				if(layers.isEmpty()) {
					continue;
				}

				int reserveCount = sysParameService.getLayerReserveCount(layers);

				Integer findLayer = sxInStoreService.findLayer(0,layers, reserveCount, "", "");

				//LayerPortOrigin layerPortOrigin = layerPortOriginService.getPortOrigin(entryCode,findLayer,50,50);

				locationId = sxInStoreService.getInStoreDetail(containerNo, findLayer, itemId,
						lot, originX, originY, 1,1,200,reserveCount);
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


	public void taskReturn(String taskId,int status,int type,String containerNo,String rgvId,String address) throws Exception{

		if("-1".equals(address) || "1".equals(address)) {
			return;
		}

		Coordinate coordinate = PrologCoordinateUtils.analysis(address);
		int targetLayer = coordinate.getLayer();
		int targetX = coordinate.getX();
		int targetY = coordinate.getY();

		//检查当前点位是库内还是接驳口
		if(status == 2){
			switch (type) {
			case 1:
				//入库
				this.containerRuKu(containerNo,targetLayer,targetX,targetY,address);
			case 2:
				//出库
				this.containerChuKu(containerNo,targetLayer,targetX,targetY,address);
				break;
			case 3:
				//移库
				this.containerYiKu(containerNo,targetLayer,targetX,targetY,address);
				break;
			case 4:
				//小车换层
				break;
			case 5:
				//输送线行走
				break;
			default:
				break;
			}
		}
	}

	private void containerRuKu(String containerCode,int targetLayer,int targetX,int targetY,String address) throws Exception {
		//检查到位的托盘
		SxStoreLocation sxStoreLocation = getStoreLocation(targetLayer,targetX,targetY);
		if(null == sxStoreLocation) {
			//检查有无入库库存
			List<InboundTask> inboundTasks = inboundTaskMapper.findByMap(MapUtils.put("containerCode", containerCode).getMap(), InboundTask.class);
			if(inboundTasks.isEmpty()) {
				FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("托盘%s无入库任务", containerCode));

				return;
			}

			//调用回告入库的方法
			//eisCallbackService

			rukuSxStore(containerCode);
		}else {
			FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("点位%s不是托盘库货位", address));

			return;
		}
	}

	private void containerChuKu(String containerCode,int targetLayer,int targetX,int targetY,String address) throws Exception {

		//任务完成
		// 找port口
		List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), PortInfo.class);
		if(portInfos.isEmpty()) {
			FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("点位%s不是托盘库出入口", address));

			return;
		}

		ContainerTask containerTask = containerTaskMapper.selectStartTaskByContainerCode(containerCode);
		if(null == containerTask) {
			//没有正在从托盘库内正在出库的任务
			FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("托盘%s无容器出库任务", containerCode));
		}
		//清除托盘库库存
		SxStore sxStore = this.clearSxStore(containerCode);

		PortInfo portInfo = portInfos.get(0);
		if(portInfo.getCallCar() == 1) {
			//修改容器任务
			//获取agv点位坐标
			List<AgvStorageLocation> agvStorageLocations = agvStorageLocationMapper.findByMap(MapUtils.put("locationType", 2).put("deviceNo",portInfo.getJunctionPort()).getMap(), AgvStorageLocation.class);
			if(agvStorageLocations.isEmpty()) {

				FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("Agv输送线区域点位不存在%s", portInfo.getJunctionPort()));
				return;
			}

			containerTask.setSource(agvStorageLocations.get(0).getRcsPositionCode());
			containerTask.setSourceType(2);
			containerTask.setTaskState(1);
			containerTask.setItemId(sxStore.getItemId());
			containerTask.setLotId(sxStore.getLotId());
			containerTask.setOwnerId(sxStore.getOwnerId());
			containerTask.setQty(sxStore.getQty());
			containerTaskMapper.update(containerTask);
		}else {
			//非agv的出库口到位
		}
	}

	private void containerYiKu(String containerCode,int targetLayer,int targetX,int targetY,String address) throws Exception {

		//检查是否存在点位
		SxStoreLocation sxStoreLocation = getStoreLocation(targetLayer,targetX,targetY);

		if(null != sxStoreLocation) {
			//更新库存
			List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerCode).getMap(),
					SxStore.class);
			if (sxStores.size() == 1) {
				// 修改库存状态为已上架
				//EIS移位完成
				SxStore sxStore = sxStores.get(0);

				if(null!= sxStore.getSourceLocationId()) {
					sxStoreTaskFinishService.moveTaskFinish(sxStore.getSourceLocationId());
				}
				sxStoreMapper.updateContainerGround(containerCode);
				SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
						.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);
				sxStoreTaskFinishService.computeLocation(sxStore);
			}else {
				throw new Exception(String.format("托盘%s库存存在多个",containerCode));
			}
		}else {
			FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("点位%s不是托盘库货位", address));
		}
	}

	//判断有无库存
	private SxStore rukuSxStore(String containerNo) throws Exception {
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
				SxStore.class);
		if (sxStores.size() == 1) {
			SxStore sxStore = sxStores.get(0);
			if(sxStore.getStoreState() != 10) {
				FileLogHelper.WriteLog("McsInterfaceCallbackError", String.format("托盘%s入库库存状态异常%s", containerNo,String.valueOf(sxStore.getStoreState())));
			}
			//修改库存为已上架
			sxStore.setStoreState(20);
			sxStoreMapper.update(sxStore);

			Integer storeLocationId = sxStore.getStoreLocationId();
			SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
			// 根据出库任务类型转换
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

	//判断有无库存
	private SxStore clearSxStore(String containerNo) throws Exception {
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
}
