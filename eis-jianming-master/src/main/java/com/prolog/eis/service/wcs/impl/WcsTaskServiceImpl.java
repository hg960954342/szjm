package com.prolog.eis.service.wcs.impl;

import org.springframework.stereotype.Service;

@Service
public class WcsTaskServiceImpl {

	/*@Autowired
	private GcsInterfaceService gcsInterfaceService;
	@Autowired
	private McsInterfaceService mcsInterfaceService;
	@Autowired
	private SxPathPlanningTaskMxMapper sxPathPlanningTaskMxMapper;
	@Autowired
	private SxPathPlanningTaskHzMapper sxPathPlanningTaskHzMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private SxConnectionRimMapper sxConnectionRimMapper;
	@Autowired
	private QcSxPathPlanningTaskMxMapper qcSxPathPlanningTaskMxMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendWcsTask(Map<Integer, List<SxPathPlanningTaskDto>> sxPathPlanningTaskMxMap) throws Exception {
		for(Entry<Integer, List<SxPathPlanningTaskDto>> entry : sxPathPlanningTaskMxMap.entrySet()){
			//SxPathPlanningTaskHz hz = sxPathPlanningTaskHzMapper.findById(entry.getKey(), SxPathPlanningTaskHz.class);
			//List<SxPathPlanningTaskMx> tasks = sxPathPlanningTaskMxMapper.queryCurrentTaskMx(hz.getId());
			//hz.setSxPathPlanningTaskMx(tasks);
			//List<SxPathPlanningTaskHz> otherTasks = this.findAll(hz);
			//boolean bl = sxPathPlanningTaskService.avoidCheck(hz, otherTasks);
			//if(bl) {
				List<SxPathPlanningTaskDto> vs = entry.getValue();
				// 按照序号排序
				vs = vs.stream().sorted(Comparator.comparing(SxPathPlanningTaskDto::getSortIndex).reversed()).collect(Collectors.toList());
				if(vs.size() > 1) {
					if(vs.get(0).getTransportationEquipment() == 2) {
						this.sendGcs(vs);
					}else if(vs.get(0).getTransportationEquipment() == 1){
						this.sendMcs(vs);
					}
				}
			//}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<SxPathPlanningTaskHz> findAll(SxPathPlanningTaskHz hz)throws Exception{
		List<SxPathPlanningTaskMx> sxPathPlanningTaskMxs = sxPathPlanningTaskMxMapper.queryOtherTaskMx(hz.getId());
		List<SxPathPlanningTaskHz> sxPathPlanningTaskHzs = sxPathPlanningTaskHzMapper.findOtherhTask(hz.getId());
		for(SxPathPlanningTaskHz sxPathPlanningTaskHz : sxPathPlanningTaskHzs) {
			List<SxPathPlanningTaskMx> list = new ArrayList<SxPathPlanningTaskMx>();
			for(SxPathPlanningTaskMx sxPathPlanningTaskMx : sxPathPlanningTaskMxs) {
				if(sxPathPlanningTaskHz.getId() == sxPathPlanningTaskMx.getTaskHzId()) {
					list.add(sxPathPlanningTaskMx);
				}
			}
			if(list.size() > 0) {
				Collections.sort(list,Comparator.comparing(SxPathPlanningTaskMx::getSortIndex));
				sxPathPlanningTaskHz.setSxPathPlanningTaskMx(list);
			}
		}
		return sxPathPlanningTaskHzs;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendGcs(Integer taskHzId, List<SxPathPlanningTaskDto> vs,int priority) throws Exception{

		String  locIdTo = "";
		String locIdFrom = "";
		Integer layer = null;

		String locIdToNode = "";
		String locIdFromNode = "";

		for(SxPathPlanningTaskDto v:vs) {
			if(v.getIsComplete() == 10) {
				layer = Integer.valueOf(v.getLayer());
				locIdTo = PrologCoordinateUtils.splicingStr(v.getX(),v.getY(),layer);
				locIdToNode = v.getNode();
			}else {
				locIdFrom = PrologCoordinateUtils.splicingStr(v.getX(),v.getY(),Integer.valueOf(v.getLayer()));
				locIdFromNode = v.getNode();
			}
		}
		
		if(!locIdToNode.contains("X07") && !locIdToNode.contains("X08") && !locIdToNode.contains("X09")) {
			List<SxConnectionRim> sxList = sxConnectionRimMapper.findByMap(MapUtils.put("entryCode",locIdToNode).getMap(), SxConnectionRim.class);
			if(!sxList.isEmpty()) {
				boolean isEmpty = mcsInterfaceService.getExitStatus(locIdTo);
				if(!isEmpty) {
					return;
				}
			}	
		}
		
		if(locIdToNode.contains("T")) {
			String tsj = locIdToNode.substring(0, 3);
			
			McsHoistStatusDto mcsHoistStatusDto = mcsInterfaceService.getHoistStatus(tsj);
			if(null == mcsHoistStatusDto) {
				return;
			}else {
				if(mcsHoistStatusDto.getStatus() != 2) {
					return;
				}
			}
		}
		
		List<SxPathPlanningTaskMx> tasks1 = sxPathPlanningTaskMxMapper.
				findByMap(MapUtils.put("node", locIdToNode).put("isComplete", 20).getMap(), SxPathPlanningTaskMx.class);
		List<SxPathPlanningTaskMx> tasks2 = sxPathPlanningTaskMxMapper.
				findByMap(MapUtils.put("node", locIdToNode).put("isComplete", 30).getMap(), SxPathPlanningTaskMx.class);
		
		if(tasks1.size() == 0 && tasks2.size() == 0) {
			//查询在途里面是否有入库在途的接驳口

			Coordinate coordinate = PrologCoordinateUtils.analysis(locIdTo);
			int sourceLayer = coordinate.getLayer();
			int sourceX = coordinate.getX();
			int sourceY = coordinate.getY();

			int pathType = 0;
			List<SxConnectionRim> sourceRims = sxConnectionRimMapper.findByMap(MapUtils.put("entryCode", locIdFromNode).getMap(), SxConnectionRim.class);
			if(!sourceRims.isEmpty()) {
				//如果是X07  X08 则给gcs发出库
				if(locIdFromNode.contains("X07") || locIdFromNode.contains("X08")) {
					pathType = 2;
				}else {
					pathType = 1;	
				}
			}else {
				if(locIdFromNode.contains("X07") || locIdFromNode.contains("X08")) {
					pathType = 3;
				}else {
					List<SxConnectionRim> targetRims = sxConnectionRimMapper.findByMap(MapUtils.put("entryCode", locIdToNode).getMap(), SxConnectionRim.class);
					if(!targetRims.isEmpty()) {
						pathType = 2;
					}else {
						pathType = 3;
					}
				}
			}

			List<SxConnectionRim> rims = sxConnectionRimMapper.findByMap(MapUtils.put("layer", sourceLayer).put("x", sourceX).put("y", sourceY).getMap(), SxConnectionRim.class);
			//如果是接驳口，判断是否有在途要入
			if(!rims.isEmpty()) {
				List<ZtckContainer> ztTaskList = ztckContainerMapper.findByMap(MapUtils.put("taskType", 50).put("entryCode", rims.get(0).getEntryCode()).getMap(),ZtckContainer.class);
				if(ztTaskList.size() == 0) {
					gcsInterfaceService.sendGcsTaskPush(vs.get(0).getContainerNo(), layer,pathType, priority, locIdFrom, locIdTo);	
				}
			}else {
				//不是解蔽口则不需要判断
				gcsInterfaceService.sendGcsTaskPush(vs.get(0).getContainerNo(), layer,pathType, priority, locIdFrom, locIdTo);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendMcs(List<SxPathPlanningTaskDto> vs) throws Exception{
		String source = "";
		String target = "";

		String sourceNode = "";
		String targetNode = "";
		for(SxPathPlanningTaskDto v:vs) {
			if(v.getIsComplete() == 40) {
				source = PrologCoordinateUtils.splicingStr(v.getX(),v.getY(), v.getLayer());
				sourceNode = v.getNode();
			}else {
				target =  PrologCoordinateUtils.splicingStr(v.getX(),v.getY(), v.getLayer());
				targetNode = v.getNode();
			}
		};

		List<SxPathPlanningTaskMx> tasks1 = sxPathPlanningTaskMxMapper.
				findByMap(MapUtils.put("transportationEquipment", 2).put("node", targetNode).put("isComplete", 20).getMap(), SxPathPlanningTaskMx.class);
		List<SxPathPlanningTaskMx> tasks2 = sxPathPlanningTaskMxMapper.
				findByMap(MapUtils.put("transportationEquipment", 2).put("node", targetNode).put("isComplete", 30).getMap(), SxPathPlanningTaskMx.class);

		if(!tasks1.isEmpty() || !tasks2.isEmpty())
			return;

		if(targetNode.contains("T")) {
			String tsj = targetNode.substring(0, 3);
			int pathType = 0;
			if(sourceNode.contains("T")) {
				//从提升机到提升机
				int sourceJ1 = Integer.valueOf(sourceNode.substring(5, 7));
				int sourceJ2 = Integer.valueOf(targetNode.substring(5, 7));
				
				//T01 T02提升机从左侧到右侧写入库 右侧到左侧写出库
				//T03 T04 T05 提升机从右侧到左侧写入库，从左侧到右侧写出库
				//同侧写跨层  3
				if("T01".equals(tsj) || "T02".equals(tsj)) {
					if(sourceJ1 > sourceJ2) {
						pathType = 2;
					}else if(sourceJ1 < sourceJ2){
						pathType = 1;
					}else {
						pathType = 3;
					}
				}else if("T03".equals(tsj) || "T04".equals(tsj) || "T05".equals(tsj)){
					if(sourceJ1 > sourceJ2) {
						pathType = 1;
					}else if(sourceJ1 < sourceJ2) {
						pathType = 2;
					}else {
						pathType = 3;
					}
				}
			}
			
			//检查提升机有任务的不允许发
			List<SxPathPlanningTaskMx> mxTasks = qcSxPathPlanningTaskMxMapper.getSxPathPlanningTaskMxSameHoister(vs.get(0).getTaskHzId(),tsj + "%");
			if(!mxTasks.isEmpty()) {
				//看是否包含目标点
				String temtargetNode = targetNode;
				SxPathPlanningTaskMx temMx = ListHelper.firstOrDefault(mxTasks, p->temtargetNode.equals(p.getNode()) && p.getIsComplete() > 10);
				if(null != temMx) {
					return;
				}else {
					this.sendMcsTask(pathType,vs.get(0).getContainerNo(), source, target);
				}
			}else {
				this.sendMcsTask(pathType,vs.get(0).getContainerNo(), source, target);
			}
			
			this.sendMcsTask(pathType,vs.get(0).getContainerNo(), source, target);
		}else {
			if(sourceNode.contains("T")){
				//从提升机到非提升机
				//目前群创mcs只有T01会直接发到外侧
				String leftOrRigjt = sourceNode.substring(5, 7);
				if("01".equals(leftOrRigjt)) {
					mcsInterfaceService.sendMcsTask(3, vs.get(0).getContainerNo(), source, target, "", 99);
				}else {
					mcsInterfaceService.sendMcsTask(2, vs.get(0).getContainerNo(), source, target, "", 99);
				}
			}else {
				this.sendMcsTask(vs,source,target);	
			}
		}
	}
	
	private void sendMcsTask(int type, String containerNo,String source,String target) throws Exception {
		mcsInterfaceService.sendMcsTask(type, containerNo, source, target, "", 99);
	}

	private void sendMcsTask(List<SxPathPlanningTaskDto> vs,String source,String target) throws Exception {
		if(vs.get(0).getPathType() == 1) {
			mcsInterfaceService.sendMcsTask(1, vs.get(0).getContainerNo(), source, target, "", 99);
		}else if(vs.get(0).getPathType() == 2){
			mcsInterfaceService.sendMcsTask(2, vs.get(0).getContainerNo(), source, target, "", 99);
		}else {
			mcsInterfaceService.sendMcsTask(3, vs.get(0).getContainerNo(), source, target, "", 99);
		}
	}*/
}
