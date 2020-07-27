package com.prolog.eis.service.impl;

import org.springframework.stereotype.Service;

@Service
public class CkCheckServiceImpl {

	/*@Autowired
	private StationsInfoMapper stationsInfoMapper;
	@Autowired
	private QcSxStoreMapper qcSxStoreMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private CkDispatchService ckDispatchService;
	@Autowired
	private SysParameMapper sysParameMapper;
	@Autowired
	private SxPathPlanningConifgHzService sxPathPlanningConifgHzService;

	@Override
	public void buildCkTask(List<WmsOutboundTask> taskList) {

		//获取所有叫料解包区
		List<String> stations = ListHelper.select(taskList, p->p.getStations());
		if(stations.isEmpty())
			return;

		String stationStr = PrologInOfListToStringHelper.getSqlStrByList(stations, "s.wms_station_no");
		//查询port口的暂存口
		List<PortTemsInfoDto> portTemsInfos = stationsInfoMapper.getStationPortTems(stationStr);
		Map<String, List<PortTemsInfoDto>> tenpdic = ListHelper.buildGroupDictionary(portTemsInfos, p->p.getWmsStationNo());

		List<PortInfoDto> portList = stationsInfoMapper.getStationPorts(stationStr);
		Map<String, List<PortInfoDto>> dic = ListHelper.buildGroupDictionary(portList, p->p.getWmsStationNo());

		//可以开始的任务集合
		List<WmsOutboundTask> preparetaskList = new ArrayList<WmsOutboundTask>();

		//所有未开始的任务
		List<WmsOutboundTask> nostartList = ListHelper.where(taskList, p->p.getFinished() == 0);

		//检查应开始了的groupId
		List<WmsOutboundTask> starttaskList = ListHelper.where(taskList, p->p.getGroupId() > 0 && p.getFinished() > 0);
		for (WmsOutboundTask nostart : nostartList) {
			this.addCkTask(nostart,starttaskList,preparetaskList);
		}

		if(preparetaskList.isEmpty()) {

			return;
		}

		//获取所有的子容器
		List<SxStoreDto> sxStoreList = new ArrayList<>();
		List<String> palletList = ListHelper.select(preparetaskList, p->p.getPalletId());
		if(!palletList.isEmpty()) {
			String palletsStr = PrologInOfListToStringHelper.getSqlStrByList(palletList, "t.container_sub_no");

			sxStoreList = qcSxStoreMapper.findSxStoreByContainerSubNo(palletsStr);	
		}

		//查询出各个port口的出库托盘任务数
		List<PortTaskCount> taskCountList = portInfoMapper.getPortCkTaskCount();

		for (WmsOutboundTask wmsOutboundTask : preparetaskList) {
			try {
				//根据任务的叫料解包區 查出所有可用的出库口
				if(!dic.containsKey(wmsOutboundTask.getStations())) {
					continue;
				}
				
				List<PortInfoDto> dicPortInfoList = dic.get(wmsOutboundTask.getStations());
				if(dicPortInfoList.isEmpty()){
					continue;
				}

				SxStoreDto sxStore = null;
				//检查是否为空托
				if(wmsOutboundTask.getTaskType() == 30) {
					//出空托
					//找到空托库存
					//优先找同层库存
					List<SxStoreDto> sxStores = qcSxStoreMapper.findEmptySxStoreByLayer(dicPortInfoList.get(0).getLayer());
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
				}else {
					//出任务托
					//检查子托盘是否存在四项库
					sxStore = ListHelper.firstOrDefault(sxStoreList,p->p.getContainerSubNo().equals(wmsOutboundTask.getPalletId()));
					if(null == sxStore) {
						throw new Exception(String.format("找不到子托盘：%s 出库任务", wmsOutboundTask.getPalletId()));
					}
				}
				List<PortInfoDto> portlist = new ArrayList<>();
				for (PortInfoDto portInfoDto : dic.get(wmsOutboundTask.getStations())){
					if(wmsOutboundTask.getTaskType() != 20 && portInfoDto.getPortType() == 2 && portInfoDto.getTaskType() == 1 && portInfoDto.getPortlock() == 2 && portInfoDto.getTaskLock() == 2) {
						//找到所有出库口
						portlist.add(portInfoDto);
					}else if(wmsOutboundTask.getTaskType() == 20 && (portInfoDto.getPortType() == 2 || portInfoDto.getPortType() == 3) && portInfoDto.getTaskType() == 4 && portInfoDto.getPortlock() == 2 && portInfoDto.getTaskLock() == 2){
						//找到所有质检口
						portlist.add(portInfoDto);
					}
				}

				//根据wms出库任务类型确定出库口类型
				if(portlist.isEmpty() && portTemsInfos.isEmpty()) {
					continue;
				}

				List<PortInfoDto> portInfoDtos = this.getPortTaskMinCount(portlist,taskCountList);
				if(!portInfoDtos.isEmpty()) {
					//String nextPostition = StringUtils.join(ListHelper.select(portlist, p->p.getJunctionPort()), "/");
					// 验证出库任务是否锁定
					for (PortInfoDto portInfoDto : portInfoDtos) {
						try {
							//检查有无出库任务路径
							boolean exist = this.checkPathExist(sxStore.getLayer(), sxStore.getX(),sxStore.getY(),portInfoDto.getJunctionPort(),portInfoDto.getLayer(),portInfoDto.getX(),portInfoDto.getY());
							if(exist) {
								ckDispatchService.xiafaChuKuTask(wmsOutboundTask,portInfoDto,sxStore);	
							}
							break;
						}catch (Exception e) {
							// TODO: handle exception
							//找不到出库port的情况下找下一个路径
							FileLogHelper.WriteLog("xiafaChuKuTask", e.getMessage());
						}
					}
				}else {
					if(tenpdic.containsKey(wmsOutboundTask.getStations())) {
						List<PortTemsInfoDto> portTemsInfoDto = tenpdic.get(wmsOutboundTask.getStations());
						List<PortTemsInfoDto> temPortTemsInfoDto = ListHelper.where(portTemsInfoDto, p->p.getTaskLock() != 1);

						try {
							ckDispatchService.xiafaTenpTask(wmsOutboundTask.getId(),temPortTemsInfoDto,sxStore);
						}catch (Exception e) {
							// TODO: handle exception
							//找不到出库port的情况下找下一个路径
							FileLogHelper.WriteLog("xiafaTenpTask", e.getMessage());
						}
					}
				}

			}catch (Exception e) {
				// TODO: handle exception
				this.printErrorMsg(e.toString(),true);
			}
		}
	}

	private void printErrorMsg(String msg,Boolean writeFile) {
		System.out.println(msg);
		if(writeFile) {
			FileLogHelper.WriteLog("buildCkTaskError", msg);
		}
	}

	private List<PortInfoDto> getPortTaskMinCount(List<PortInfoDto> portlist,List<PortTaskCount> taskCountList) {

		for (PortInfoDto portInfoDto : portlist) {
			PortTaskCount portTaskCount = ListHelper.firstOrDefault(taskCountList, p->portInfoDto.getWmsPortNo().equals(p.getPortNo()));
			if(null!=portTaskCount) {
				portInfoDto.setTaskCount(portTaskCount.getTaskCount());
			}else {
				portInfoDto.setTaskCount(0);

				PortTaskCount p = new PortTaskCount();
				p.setPortNo(portInfoDto.getWmsPortNo());
				p.setTaskCount(0);

				taskCountList.add(p);
			}
		}
		portlist.sort((p1,p2)->{ return p1.getTaskCount() - p2.getTaskCount();});
		return portlist;
	}

	private boolean checkPathExist(int startLayer,int startX,int startY,String junctionPort,int endLayer,int endX,int entY) throws Exception {
		//創建路徑任務
		List<SxPathParameDto> start = new ArrayList<SxPathParameDto>();
		SxPathParameDto startSxPathParameDto = new SxPathParameDto();
		startSxPathParameDto.setParameType(2);
		startSxPathParameDto.setLayer(startLayer);
		startSxPathParameDto.setRegionNo("1");
		startSxPathParameDto.setX(startX);
		startSxPathParameDto.setY(startY);
		start.add(startSxPathParameDto);

		List<SxPathParameDto> end = new ArrayList<SxPathParameDto>();
		SxPathParameDto endSxPathParameDto = new SxPathParameDto();
		endSxPathParameDto.setParameType(1);
		endSxPathParameDto.setConnectionNo(junctionPort);
		endSxPathParameDto.setLayer(endLayer);
		endSxPathParameDto.setRegionNo("1");
		endSxPathParameDto.setX(endX);
		endSxPathParameDto.setY(entY);
		end.add(endSxPathParameDto);

		String startLocation = this.sxLocation(start);
		String endLocation = this.sxLocation(end);

		//没报错就说明找到了路径
		sxPathPlanningConifgHzService.findPlanByLocation(startLocation, endLocation);

		return true;
	}

	*//**
	 * 拼接坐标位置
	 * 
	 * @param sxPathParameDto
	 * @return
	 *//*
	private String sxLocation(List<SxPathParameDto> sxPathParameDtos) throws Exception {
		String location = "";
		StringBuilder sb = new StringBuilder();
		for (SxPathParameDto sxPathParameDto : sxPathParameDtos) {
			if (sxPathParameDto.getParameType() == 1) {
				// 接驳口
				String[] connectionNos = sxPathParameDto.getConnectionNo().split(",");
				for (String connectionNo : connectionNos) {
					if (sb.toString().isEmpty()) {
						sb.append("@");
					}
					sb.append(connectionNo);
					sb.append("@");
				}
				location = sb.toString();
			} else if (sxPathParameDto.getParameType() == 2) {
				// 库内托盘为
				if (sb.toString().isEmpty()) {
					sb.append("@");
				}
				sb.append("S");
				sb.append("0");
				sb.append(sxPathParameDto.getLayer());
				sb.append("0");
				sb.append(sxPathParameDto.getRegionNo());
				sb.append("@");
				location = sb.toString();

			} else {
				throw new Exception("参数类型无效！参数类型为：" + sxPathParameDto.getParameType());
			}
		}
		return location;
	}

	private void addCkTask(WmsOutboundTask nostart,List<WmsOutboundTask> starttaskList,List<WmsOutboundTask> preparetaskList) {

		SysParame sysParame = sysParameMapper.findById("IGNORE_GROUP", SysParame.class);
		int ignoreGroup = 0;
		if(null != sysParame && !StringUtils.isEmpty(sysParame.getParameValue())) {
			ignoreGroup = Integer.parseInt(sysParame.getParameValue());
		}

		if(ignoreGroup == 1) {
			//忽略任务组
			preparetaskList.add(nostart);
		}else {
			//启用任务组
			if(nostart.getGroupId() > 0) {
				WmsOutboundTask task = ListHelper.firstOrDefault(starttaskList, p->p.getGroupId() != nostart.getGroupId() && p.getStations().equals(nostart.getStations()));
				if(null == task) {
					starttaskList.add(nostart);
					preparetaskList.add(nostart);
				}
			}else {
				preparetaskList.add(nostart);

				WmsOutboundTask task = ListHelper.firstOrDefault(starttaskList, p->p.getStations().equals(nostart.getStations()));
				if(null == task) {
					preparetaskList.add(nostart);	
				}
			}
		}
	}*/
}
