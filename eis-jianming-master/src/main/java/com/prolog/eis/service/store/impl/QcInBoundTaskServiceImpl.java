package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.CheckOutTaskMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.DeviceJunctionPortMapper;
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
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.model.eis.DeviceJunctionPort;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.CallBackCheckOutService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.base.SysParameService;
import com.prolog.eis.service.impl.inbound.InBoundContainerService;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.service.mcs.impl.McsInterfaceServiceSend;
import com.prolog.eis.service.store.CallBackStatus;
import com.prolog.eis.service.store.MCSCallBack;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.sxk.SxInStoreService;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.eis.util.detetionlayer.DetetionLayerHelper;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@SuppressWarnings("all")
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
	@Autowired
	private McsInterfaceService mcsInterfaceService;
	@Autowired
	private DeviceJunctionPortMapper deviceJunctionPortMapper;

	@Autowired
	private McsInterfaceServiceSend mcsInterfaceServiceSend;
	@Autowired
	private CallBackCheckOutService callBackCheckOutService;
	@Autowired
	CheckOutTaskMapper checkOutTaskMapper;
	@Autowired
	InBoundContainerService inBoundContainerService;

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
			weight = Double.valueOf(inBoundRequest.getWeight())/10.00;
		}

		//根据托盘码查询入库托盘任务
		ContainerTask containerTask = containerTaskMapper.queryContainerTaskByConcode(containerNo);


		if(weight > limitWeight || detection != 1) {
			return this.addMcsTask(false,1,containerNo,source,"-1",inBoundRequest.getStockId()+"托盘超重或者尺寸异常");
		}


		// 找port口
		List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), PortInfo.class);
		if(portInfos.isEmpty()) {
			return this.addMcsTask(false,1,containerNo,source,"-1","入库口"+source+"不存在");
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
				return this.addMcsTask(false,1,containerNo,source,"-1",result.getMsg());
			}else {
				return null;
			}
		}

		if(result.getResultType() == 2) {
			//解锁agv点位
			clearAgvLock(containerTask);
			//入库成功，删除agv区域的托盘
			clearAgvLocationComtainer(containerNo);
			//生成库存并入库
			McsRequestTaskDto mcsRequestTaskDto = this.taskContainerInSxStore(result.getInboundTask(),weight,portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
			return mcsRequestTaskDto;
		}else if(result.getResultType() == 1){
			//生成库存并入库
			McsRequestTaskDto mcsRequestTaskDto = this.emptyContainerInSxStore(result.getInboundTask(),weight,portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
			return mcsRequestTaskDto;
		}

		return null;
	}

	/**
	 * 入庫驗證 1 空托入庫驗證  2 字母托入庫驗證
	 * @param
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
            LogServices.logSysBusiness("mcsRequestError"+ mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
		if(!sxStores.isEmpty()) {			
			String mString = String.format("托盘%s已在库内", containerNo);
            LogServices.logSysBusiness("mcsRequestError"+ mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}

		//检查是否存在入库任务
		List<InboundTask> temInboundTasks = inboundTaskMapper.findByMap(MapUtils.put("containerCode", containerNo).getMap(), InboundTask.class);

		if(temInboundTasks.isEmpty()) {
			String mString = String.format("托盘%s无入库任务", containerNo);
            LogServices.logSysBusiness("mcsRequestError"+ mString);

			result.setSuccess(false);
			result.setMsg(mString);
			return result;
		}




		result.setInboundTask(temInboundTasks.get(0));
		result.setSuccess(true);
		result.setResultType(2);

		return result;
		//}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void foldInBound(String deviceNo,String containerNo) throws Exception {
		DeviceJunctionPort deviceJunctionPort = deviceJunctionPortMapper.findById(deviceNo, DeviceJunctionPort.class);
		if(null == deviceJunctionPort) {
            LogServices.logSysBusiness("mcsfoldInBoundError"+ String.format("设备编号%s未配置", deviceNo));
			return;
		}

		String source = PrologCoordinateUtils.splicingStr(deviceJunctionPort.getX(), deviceJunctionPort.getY(), deviceJunctionPort.getLayer());

		//验证
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(), SxStore.class);
		if(!sxStores.isEmpty()) {
            LogServices.logSysBusiness("mcsfoldInBoundError"+ String.format("容器%s存在库存", containerNo));

			mcsInterfaceServiceSend.sendMcsTaskWithOutPathAsyc(1,
					containerNo, 
					source,
					"-1",
					"0", "99",0);
			return;
		}

		String emptyContainNo = PrologStringUtils.newGUID();
		//直接入库，但是优先入当前层，建民碟盘机
		InboundTask inboundTask = new InboundTask();
		inboundTask.setBillNo(PrologStringUtils.newGUID());
		inboundTask.setWmsPush(0);
		inboundTask.setReBack(0);
		inboundTask.setEmptyContainer(1);
		inboundTask.setContainerCode(emptyContainNo);
		inboundTask.setTaskType(0);
		inboundTask.setOwnerId("空托");
		inboundTask.setItemId("空托");
		inboundTask.setLotId("空托");
		inboundTask.setQty(1);
		inboundTask.setTaskState(3);
		inboundTask.setCreateTime(new Date());
		inboundTask.setStartTime(new Date());
		inboundTask.setRukuTime(new Date());
		inboundTaskMapper.save(inboundTask);

		//优先找1层
		Integer locationId = checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),emptyContainNo,deviceJunctionPort.getLayer(),1,3,1,1);
		if(null == locationId) {
			//找不到 1层 直接屏蔽
			//locationId = checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),emptyContainNo,deviceJunctionPort.getLayer(),1,3,1,3);
		}

		if(null == locationId) {
            LogServices.logSysBusiness("mcsfoldInBoundError"+ String.format("货位不足"));

			mcsInterfaceServiceSend.sendMcsTaskWithOutPathAsyc(1,
					containerNo, 
					source,
					"-1",
					"0", "99",0);
			return;
		}

		//生成入库库存
		this.buildRuKuSxStore(locationId,inboundTask,emptyContainNo,200d);

		//发送指令
		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationId, SxStoreLocation.class);
		String target = PrologCoordinateUtils.splicingStr(sxStoreLocation.getX(), sxStoreLocation.getY(), sxStoreLocation.getLayer());

		mcsInterfaceServiceSend.sendMcsTaskWithOutPathAsyc(1,
				emptyContainNo,
				source,
				target,
				"0", "99",0);
	}	

	private void clearAgvLocationComtainer(String containerNo) {
		containerTaskMapper.deleteByMap(MapUtils.put("containerCode", containerNo).getMap(), ContainerTask.class);
	}

	private void clearAgvLock(ContainerTask containerTask) {
		AgvStorageLocation agvLocation = agvStorageLocationMapper.findByRcs(containerTask.getSource());
		agvLocation.setTaskLock(0);
		agvLocation.setLocationLock(0);
		agvStorageLocationMapper.update(agvLocation);
	}


	private McsRequestTaskDto emptyContainerInSxStore(InboundTask inboundTask,double weight,PortInfo portInfo,String containerNo,String source,int sourceLayer,int sourceX,int sourceY,int detection) throws Exception {
		Integer locationId = this.checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),containerNo,sourceLayer,detection,3,1,1);
		if(null == locationId) {
			if(portInfo.getShowLed() == 1) {
				//this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
			}
			if(portInfo.getReback() == 1) {
				this.addMcsTask(false,1,containerNo,source,"-1","貨位不足！！！");
			}
			return null;
		}

		//生成入库库存
		this.buildRuKuSxStore(locationId,inboundTask,containerNo,weight);

		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationId, SxStoreLocation.class);
		String target = PrologCoordinateUtils.splicingStr(sxStoreLocation.getX(), sxStoreLocation.getY(), sxStoreLocation.getLayer());
		return this.addMcsTask(true,1,containerNo,source,target,"");
	}

	private McsRequestTaskDto taskContainerInSxStore(InboundTask inboundTask,double weight,PortInfo portInfo,String containerNo,String source,int sourceLayer,int sourceX,int sourceY,int detection) throws Exception {

		Integer locationId = this.checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),containerNo,sourceLayer,detection,null,1,3);
		//没找到货位
		if(null == locationId) {
			if(portInfo.getShowLed() == 1) {
				//this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
			}
			if(portInfo.getReback() == 1) {
				this.addMcsTask(false,1,containerNo,source,"-1","貨位不足！！！");
			}
			return null;
		}

		//生成入库库存
		this.buildRuKuSxStore(locationId,inboundTask,containerNo,weight);

		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationId, SxStoreLocation.class);
		String target = PrologCoordinateUtils.splicingStr(sxStoreLocation.getX(), sxStoreLocation.getY(), sxStoreLocation.getLayer());
		return this.addMcsTask(true,1,containerNo,source,target,"");
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

		inboundTaskMapper.updateMapById(inboundTask.getId(), MapUtils.put("taskState", 3).put("rukuTime", new Date()).getMap(), InboundTask.class);
	}

	private Integer checkHuoWei(String itemId,String lot,String containerNo,int sourceLayer,int detection,Integer defaultReserveCount,int minLayer,int maxLayer) throws Exception {
		List<List<Integer>> layerGroups = DetetionLayerHelper.getLayers(detection,minLayer,maxLayer);

		//查找货位
		Integer locationId = null;
		SysParame pOriginX = sysParameMapper.findById("ORIGINX", SysParame.class);
		SysParame pOriginY = sysParameMapper.findById("ORIGINY", SysParame.class);

		int originX = Integer.valueOf(pOriginX.getParameValue());
		int originY = Integer.valueOf(pOriginY.getParameValue());

		for (List<Integer> layers : layerGroups) {
			try {
				if(layers.isEmpty()) {
					continue;
				}

				Integer reserveCount = defaultReserveCount;
				if(null == reserveCount) {
					reserveCount = sysParameService.getLayerReserveCount(layers.get(0));	
				}

				Integer findLayer = sxInStoreService.findLayer(0,layers, reserveCount, "", "");

				locationId = sxInStoreService.getInStoreDetail(containerNo, findLayer, itemId,
						lot, originX, originY, 1,1,200,reserveCount);
				if(null != locationId) {
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
				LogServices.logSys(e);
			}
		}
		return locationId;
	}


    @Autowired
    private final Map<String, MCSCallBack> MCSCallBackMap = new ConcurrentHashMap<>();
	@Override
	@Transactional(rollbackFor = Exception.class,propagation=Propagation.SUPPORTS)
	public void taskReturn(String taskId,int status,int type,String containerNo,String rgvId,String address) throws Exception{

		if("-1".equals(address) || "1".equals(address)) {
			return;
		}

		Coordinate coordinate = PrologCoordinateUtils.analysis(address);
		int targetLayer = coordinate.getLayer();
		int targetX = coordinate.getX();
		int targetY = coordinate.getY();

        MCSCallBack mCSCallBack=MCSCallBackMap.get(CallBackStatus.MCSCallBack_STATUS+status+CallBackStatus.TYPE+type);
        if(mCSCallBack!=null){
            mCSCallBack.container(containerNo,targetLayer,targetX,targetY,address);
        }

	}

	@Override
	public void rcsCompleteForward(String containerCode,int agvLocationId) throws Exception {

		//根据agv点位，找到四向库入库口
		AgvStorageLocation agvStorageLocation = agvStorageLocationMapper.findById(agvLocationId, AgvStorageLocation.class);
		if(null != agvStorageLocation) {
			List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("junctionPort", agvStorageLocation.getDeviceNo()).getMap(), PortInfo.class);
			if(!portInfos.isEmpty()) {
				PortInfo portInfo = portInfos.get(0);
				String source = PrologCoordinateUtils.splicingStr(portInfo.getX(), portInfo.getY(), portInfo.getLayer());

				mcsInterfaceServiceSend.sendMcsTaskWithOutPathAsyc(4,
						containerCode, 
						source,
						"1",
						"", "99",0);
			}
		}
	}


	@Autowired
	CallBackService callBackService;
     @Override
	public SxStore rukuSxStoreUpdate(String containerNo) throws Exception{
       return callBackService.rukuSxStore(containerNo);
	}




	private McsRequestTaskDto addMcsTask(boolean success,int mcsType,String stockId,String source,String target,String errorMessage) {
		McsRequestTaskDto mcsSendTaskDto = new McsRequestTaskDto();
		mcsSendTaskDto.setSuccess(success);
		mcsSendTaskDto.setType(mcsType);
		mcsSendTaskDto.setStockId(stockId);
		mcsSendTaskDto.setTarget(target);
		mcsSendTaskDto.setSource(source);
		mcsSendTaskDto.setErrorMessage(errorMessage);

		return mcsSendTaskDto;
	}
}
