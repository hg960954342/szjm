package com.prolog.eis.service.store.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.base.SysParameService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.sxk.SxInStoreService;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.detetionlayer.DetetionLayerHelper;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;

@Service
public class QcInBoundTaskServiceImpl implements QcInBoundTaskService{

	@Autowired
	private InboundTaskMapper inboundTaskMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	
//	@Autowired
//	private SxHoisterConfigMapper sxHoisterConfigMapper;
//	@Autowired
//	private SxStoreMapper sxStoreMapper;
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
//	@Autowired
//	private SxPathPlanningTaskService sxPathPlanningTaskService;
//	@Autowired
//	private PalletInfoMapper palletInfoMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;
//	@Autowired
//	private ZtckContainerMapper ztckContainerMapper;
//	@Autowired
//	private LedMessageService ledMessageService;
//	@Autowired
//	private ZtContainerMsgService ztContainerMsgService;
//	@Autowired
//	private ThroughTaskMapper throughTaskMapper;
//	@Autowired
//	private QcInBoundReturnExcuteService qcInBoundReturnExcuteService;
//	@Autowired
//	private LayerPortOriginService layerPortOriginService;
	@Autowired
	private SysParameService sysParameService;
//	@Autowired
//	private EmptyCaseConfigService emptyCaseConfigService;

	
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
		
		InStoreValidateDto result = this.getValidateResult(portInfo.getDetection(),containerNo,portInfo.getWmsPortNo(),portInfo.getJunctionPort(),detection);
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
			//result.getInboundTask().setw
			this.inSxStore(result.getInboundTask(),weight,portInfo,containerNo,source,sourceLayer,sourceX,sourceY,detection);
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
	private InStoreValidateDto getValidateResult(int isdetection, String containerNo,String wmsPortNo,String junctionPort,int detection) {
		
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
		
	}
	
	
	private void inSxStore(InboundTask inboundTask,double weight,PortInfo portInfo,String containerNo,String source,int sourceLayer,int sourceX,int sourceY,int detection) throws Exception {

		Integer locationId = this.checkHuoWei(inboundTask.getOwnerId() + "and" + inboundTask.getItemId(),inboundTask.getLotId(),containerNo,sourceLayer,detection,portInfo.getJunctionPort());
		if(null == locationId) {
			if(portInfo.getShowLed() == 1) {
				//this.addLedMsg(portInfo.getId(),portInfo.getPortType(),20,"貨位不足！！！");
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
		/*if(inboundTask.getWmsPush() == 0) {
			sxStore.setSxStoreType(2);
		}else if(wmsInboundTask.getWmsPush() == 1) {
			sxStore.setSxStoreType(1);
		}*/
		sxStore.setContainerNo(containerNo);
		sxStore.setStoreState(10);
		sxStore.setTaskProperty1(inboundTask.getOwnerId() + "and" + inboundTask.getItemId());
		sxStore.setTaskProperty2(inboundTask.getLotId());
		//sxStore.setBusinessProperty1(wmsInboundTask.getMaterielType());
		//sxStore.setBusinessProperty2(wmsInboundTask.getMaterielName());
		//sxStore.setBusinessProperty3(wmsInboundTask.getFactoryCode());
		//sxStore.setBusinessProperty4(wmsInboundTask.getBoxCount());
		//库存记录高度
		//sxStore.setBusinessProperty5(String.valueOf(detection));

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

	private Integer checkHuoWei(String liaohao,String lot,String containerNo,int sourceLayer,int detection,String entryCode) throws Exception {
		List<List<Integer>> layerGroups = DetetionLayerHelper.getLayers(detection,1,3);
		
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
				
				locationId = sxInStoreService.getInStoreDetail(containerNo, findLayer, liaohao,
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

	/*private void addLedMsg(int portId,int portType,int sate,String msg) {
		String stateStr = "";
		if(portType == 1) {
			stateStr = "入庫";
		} else {
			stateStr = "出庫";
		}

		ledMessageService.saveLedMessage(portId, stateStr, sate, msg);
	}*/

	public void taskReturn(InBoundRequest inBoundRequest) throws Exception{

		return ;
		/*if(inBoundRequest.getTarget().equals("-1") || inBoundRequest.getTarget().equals("1")) {
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
		}*/
	}

	/*private List<PortInfoDto> getPortTaskMinCount(List<PortInfoDto> portlist,List<PortTaskCount> taskCountList) {

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
