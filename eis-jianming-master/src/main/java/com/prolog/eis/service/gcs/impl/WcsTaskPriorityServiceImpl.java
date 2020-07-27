package com.prolog.eis.service.gcs.impl;

import org.springframework.stereotype.Service;

@Service
public class WcsTaskPriorityServiceImpl {

	/*@Autowired
	private SxPathPlanningTaskMxMapper sxPathPlanningTaskMxMapper;
	@Autowired
	private SxPathPlanningTaskHzMapper sxPathPlanningTaskHzMapper;
	@Autowired
	private WcsTaskService wcsTaskService;
	@Autowired
	private SxQcCheckService sxQcCheckService;
	@Autowired
	private QcSxStoreMapper qcSxStoreMapper;
	@Autowired
	private QcSxPathPlanningTaskMxMapper qcSxPathPlanningTaskMxMapper;
	@Autowired
	private SxConnectionRimMapper sxConnectionRimMapper;

	@Override
	public void sendWcsTask() {
		try {
			synchronized ("sendwcstask".intern()) {
				List<String> emptyContainerNoList = qcSxStoreMapper.getAllEmptyContainer();

				List<QcSxPathPlanningTaskDto> sxPathPlanningTaskMxs = qcSxPathPlanningTaskMxMapper.findSxPathTask();
				
				List<SxConnectionRim> rims = sxConnectionRimMapper.findByMap(new HashMap<String, Object>(), SxConnectionRim.class);
				
				if(!sxPathPlanningTaskMxs.isEmpty()) {
					Map<Integer, List<QcSxPathPlanningTaskDto>> sxPathPlanningTaskMxMap = sxPathPlanningTaskMxs.stream().
							collect(Collectors.groupingBy(QcSxPathPlanningTaskDto :: getTaskHzId));

					List<SxPathPlanningTaskPriorityDto> prioritylist = new ArrayList<SxPathPlanningTaskPriorityDto>();
					for(Entry<Integer, List<QcSxPathPlanningTaskDto>> entry : sxPathPlanningTaskMxMap.entrySet()){
						SxPathPlanningTaskHz hz = sxPathPlanningTaskHzMapper.findById(entry.getKey(), SxPathPlanningTaskHz.class);
						List<SxPathPlanningTaskMx> tasks = sxPathPlanningTaskMxMapper.queryCurrentTaskMx(hz.getId());
						hz.setSxPathPlanningTaskMx(tasks);
						//List<SxPathPlanningTaskHz> otherTasks = wcsTaskService.findAll(hz);
					
						prioritylist.add(setPriority(hz,entry.getValue(),emptyContainerNoList));
					}

					if(!prioritylist.isEmpty()) {
						prioritylist.sort((p1,p2)->
						{ 
							int priority = p1.getPriority() - p2.getPriority();
							if(priority != 0) {
								return priority;
							}else {
								long time = p1.getCreateTime().getTime() - p2.getCreateTime().getTime();
								if(time > 0) {
									return 1;
								}
								else if(time < 0) {
									return -1;
								}
								else {
									return 0;
								}
							}
						});
						
						for (SxPathPlanningTaskPriorityDto sxPathPlanningTaskPriorityDto : prioritylist) {
							List<SxPathPlanningTaskHz> otherTasks = wcsTaskService.findAll(sxPathPlanningTaskPriorityDto.getHz());
							
							boolean bl = sxQcCheckService.avoidQcCheck(sxPathPlanningTaskPriorityDto.getHz(),
									otherTasks,rims,SxValidatePolicy.Policy_Base);
							if(bl) {
								List<SxPathPlanningTaskDto> vs = changeToSxPathPlanningTaskDtoList(sxPathPlanningTaskPriorityDto.getVs());
								// 按照序号排序
								vs = vs.stream().sorted(Comparator.comparing(SxPathPlanningTaskDto::getSortIndex).reversed()).collect(Collectors.toList());
								if(vs.size() > 1) {
									if(vs.get(0).getTransportationEquipment() == 2) {
										//出库优先级比入库优先级高
										synchronized ("sxkchuku".intern()) {
											wcsTaskService.sendGcs(vs.get(0).getTaskHzId(),vs,sxPathPlanningTaskPriorityDto.getPriority());
										}
									}else if(vs.get(0).getTransportationEquipment() == 1){
										wcsTaskService.sendMcs(vs);
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			FileLogHelper.WriteLog("sendWcsTaskError", e.getMessage());
		}
	}
	
	private List<SxPathPlanningTaskDto> changeToSxPathPlanningTaskDtoList(List<QcSxPathPlanningTaskDto> tasks){
		
		List<SxPathPlanningTaskDto> list = new ArrayList<>();
		for (QcSxPathPlanningTaskDto qcSxPathPlanningTaskDto : tasks) {
			SxPathPlanningTaskDto obj = new SxPathPlanningTaskDto();
			obj.setId(qcSxPathPlanningTaskDto.getId());
			obj.setSortIndex(qcSxPathPlanningTaskDto.getSortIndex());
			obj.setTaskHzId(qcSxPathPlanningTaskDto.getTaskHzId());
			obj.setNodeType(qcSxPathPlanningTaskDto.getNodeType());
			obj.setTransportationEquipment(qcSxPathPlanningTaskDto.getTransportationEquipment());
			obj.setNode(qcSxPathPlanningTaskDto.getNode());
			obj.setLineDirection(qcSxPathPlanningTaskDto.getLineDirection());
			obj.setX(qcSxPathPlanningTaskDto.getX());
			obj.setY(qcSxPathPlanningTaskDto.getY());
			obj.setLayer(qcSxPathPlanningTaskDto.getLayer());
			obj.setIsComplete(qcSxPathPlanningTaskDto.getIsComplete());
			obj.setTaskId(qcSxPathPlanningTaskDto.getTaskId());
			obj.setContainerNo(qcSxPathPlanningTaskDto.getContainerNo());
			obj.setPathType(qcSxPathPlanningTaskDto.getPathType());
			
			list.add(obj);
		}
		
		return list;
	}

	private SxPathPlanningTaskPriorityDto setPriority(SxPathPlanningTaskHz hz,List<QcSxPathPlanningTaskDto> vs,List<String> emptyContainerCode) {

		SxPathPlanningTaskPriorityDto result = new SxPathPlanningTaskPriorityDto();
		result.setVs(vs);
		result.setHz(hz);
		result.setCreateTime(hz.getCreateTime());

		result.setPriority(99);
		if(hz.getPathType() == 2){
			//普通出库改为和入库优先级一样
			//result.setPriority(80);
		}

		String locIdFromNode = "";

		for(QcSxPathPlanningTaskDto v:vs) {
			if(v.getIsComplete() != 10) {
				locIdFromNode = v.getNode();
			}
		}

		//如果是提升机的出库任务，那么优先级调高
		if(locIdFromNode.contains("T")) {
			//起点在提升机，那么该GCS任务将最高
			//如果是出库任务那么优先级调高
			if(hz.getPathType() == 2) {
				result.setPriority(70);	
			}
		}

		//空托任务优先级最高
		String containerNo = vs.get(0).getContainerNo();
		if(emptyContainerCode.contains(containerNo)) {
			result.setPriority(60);
		}
		
		if(vs.get(0).getEmerge() > 0) {
			result.setPriority(50);
		}
		
		if(vs.get(0).getEmerge() > 1) {
			result.setPriority(40);
		}

		return result;
	}*/
}
