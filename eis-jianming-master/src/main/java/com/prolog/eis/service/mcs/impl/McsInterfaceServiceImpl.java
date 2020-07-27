package com.prolog.eis.service.mcs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class McsInterfaceServiceImpl  {

	@Autowired
	private RestTemplate restTemplate;
	@Value("${prolog.mcs.url:}")
	private String mcsUrl;

	@Value("${prolog.mcs.port:}")
	private String mcsPort;
	/*@Autowired
	private MCSTaskMapper mcsTaskMapper;
	@Autowired
	private SxPathPlanningTaskService sxPathPlanningTaskService;
	@Autowired
	private MCSTaskHisotoryMapper mCSTaskHisotoryMapper;
	@Autowired
	private ZtckContainerMapper ztckContainerMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private StationsInfoMapper stationsInfoMapper;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private SxCarAcrossTaskMapper sxCarAcrossTaskMapper;
	//@Autowired
	//private CarAcrossLayerService carAcrossLayerService;
	@Autowired
	private SxCarAcrossMapper sxCarAcrossMapper;

	@Override
	@Transactional
	public String sendMcsTask(int type, String stockId, String source, String target, String weight, int priority)
			throws Exception {
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
		McsSendTaskDto mcsSendTaskDto = new McsSendTaskDto();
		String taskId = PrologTaskIdUtils.getTaskId();
		mcsSendTaskDto.setTaskId(taskId);
		mcsSendTaskDto.setType(type);
		mcsSendTaskDto.setStockId(stockId);
		mcsSendTaskDto.setTarget(target);
		mcsSendTaskDto.setSource(source);
		mcsSendTaskDto.setPriority(priority);
		mcsSendTaskDto.setWeight(weight);
		mcsSendTaskDtos.add(mcsSendTaskDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", mcsSendTaskDtos);
		String data = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/Request");
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS任务："+data);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS任务返回："+restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			MCSTask mcsTask = new MCSTask();
			mcsTask.setTaskId(taskId);
			mcsTask.setPriority(priority);
			mcsTask.setSource(source);
			mcsTask.setStockId(stockId);
			mcsTask.setTarget(target);
			mcsTask.setType(type);
			mcsTask.setWeight(weight);
			mcsTask.setSendCount(1);
			mcsTask.setCreateTime(PrologDateUtils.parseObject(new Date()));
			if (sucssess) {
				List<TaskReturnInBoundRequestResponse> resultData = helper.getObjectList("data",TaskReturnInBoundRequestResponse.class);
				if(resultData.isEmpty()) {
					sxPathPlanningTaskService.createPathMxTaskID(stockId, taskId);
				}else {
					FileLogHelper.WriteLog("sendMcsException","reason:"+ message);	
				}
			} else {
				FileLogHelper.WriteLog("sendMcsException","reason:"+message);
			}

			return taskId;
		} catch (Exception e) {
			FileLogHelper.WriteLog("sendMcsException","reason:"+e.toString());

			return taskId;
		}
	}

	@Override
	@Transactional
	@Async
	public String sendMcsTaskWithOutPathAsyc(int type, String stockId, String source, String target, String weight, int priority)
			throws Exception {
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
		McsSendTaskDto mcsSendTaskDto = new McsSendTaskDto();
		String taskId = PrologTaskIdUtils.getTaskId();
		mcsSendTaskDto.setTaskId(taskId);
		mcsSendTaskDto.setType(type);
		mcsSendTaskDto.setStockId(stockId);
		mcsSendTaskDto.setTarget(target);
		mcsSendTaskDto.setSource(source);
		mcsSendTaskDto.setPriority(priority);
		mcsSendTaskDto.setWeight(weight);
		mcsSendTaskDtos.add(mcsSendTaskDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", mcsSendTaskDtos);
		String data = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/Request");
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS任务："+data);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS返回："+restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			MCSTask mcsTask = new MCSTask();
			mcsTask.setTaskId(taskId);
			mcsTask.setPriority(priority);
			mcsTask.setSource(source);
			mcsTask.setStockId(stockId);
			mcsTask.setTarget(target);
			mcsTask.setType(type);
			mcsTask.setWeight(weight);
			mcsTask.setSendCount(1);
			mcsTask.setCreateTime(PrologDateUtils.parseObject(new Date()));
			if (sucssess) {
				mcsTask.setTaskState(1);
			} else {
				mcsTask.setTaskState(2);
				mcsTask.setErrMsg(message);
				mcsTaskMapper.save(mcsTask);
			}
			
			return taskId;
		} catch (Exception e) {
			MCSTask mcsTask = new MCSTask();
			mcsTask.setTaskId(taskId);
			mcsTask.setPriority(priority);
			mcsTask.setSource(source);
			mcsTask.setStockId(stockId);
			mcsTask.setTarget(target);
			mcsTask.setType(type);
			mcsTask.setWeight(weight);
			mcsTask.setSendCount(1);
			mcsTask.setCreateTime(PrologDateUtils.parseObject(new Date()));
			mcsTask.setTaskState(2);
			mcsTask.setErrMsg(e.getMessage());
			mcsTaskMapper.save(mcsTask);
			return taskId;
		}
	}

	@Override
	public void recall(MCSTask mcsTask) throws Exception {
		List<McsSendTaskDto> mcsSendTaskDtos = new ArrayList<McsSendTaskDto>();
		McsSendTaskDto mcsSendTaskDto = new McsSendTaskDto();
		mcsSendTaskDto.setTaskId(mcsTask.getTaskId());
		mcsSendTaskDto.setType(mcsTask.getType());
		mcsSendTaskDto.setStockId(mcsTask.getStockId());
		mcsSendTaskDto.setTarget(mcsTask.getTarget());
		mcsSendTaskDto.setSource(mcsTask.getSource());
		mcsSendTaskDto.setPriority(mcsTask.getPriority());
		mcsSendTaskDto.setWeight(mcsTask.getWeight());
		mcsSendTaskDtos.add(mcsSendTaskDto);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", mcsSendTaskDtos);
		String data = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/Request");

			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS任务："+data);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
			FileLogHelper.WriteLog("sendMCSTask", "EIS->MCS返回："+restJson);
			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");

			if (sucssess) {
				mcsTask.setTaskState(1);
				mcsTask.setSendCount(mcsTask.getSendCount()+1);

				MCSTaskHisotory mcsTaskHisotory = new MCSTaskHisotory();
				BeanUtils.copyProperties(mcsTask, mcsTaskHisotory);
				mCSTaskHisotoryMapper.save(mcsTaskHisotory);
				mcsTaskMapper.deleteById(mcsTask.getId(), MCSTask.class);
			} else {
				mcsTask.setTaskState(2);
				mcsTask.setErrMsg(message);
				mcsTask.setSendCount(mcsTask.getSendCount()+1);
				mcsTaskMapper.update(mcsTask);
			}
		} catch (Exception e) {
			mcsTask.setSendCount(mcsTask.getSendCount()+1);
			mcsTask.setTaskState(2);
			mcsTask.setErrMsg(e.getMessage());
			mcsTaskMapper.update(mcsTask);
		}
	}

	@Override
	public List<MCSTask> findFailMCSTask() throws Exception {
		List<MCSTask> mcsTasks = mcsTaskMapper.findByMap(MapUtils.put("taskState", 2).getMap(), MCSTask.class);
		return mcsTasks;
	}

	@Override
	public boolean getExitStatus(String position) throws Exception {

		List<Map<String, Object>> coord = new ArrayList<Map<String, Object>>();
		coord.add(MapUtils.put("coord", position).getMap());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("carryList", coord);
		String requestData = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/getExitStatus");

			FileLogHelper.WriteLog("getExitStatus", "EIS->MCS接驳口状态查询，请求参数："+requestData);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(requestData), String.class);
			FileLogHelper.WriteLog("getExitStatus", "EIS->MCS接驳口状态查询，返回参数："+restJson);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");
			List<Map> data = helper.getObjectList("data", Map.class);

			if (sucssess) {
				if(!data.isEmpty()) {
					Map<String, Object> m = data.get(0);
					// isEmpty：Boolean【是否为空 true为空，false 不为空】
					boolean isEmpty = (boolean) m.get("empty");
					return isEmpty;
				}else {
					return false;
				}
				
			}else {
				return true;
			}
			
			if (sucssess && !data.isEmpty()) {
				Map<String, Object> m = data.get(0);
				// isEmpty：Boolean【是否为空 true为空，false 不为空】
				boolean isEmpty = (boolean) m.get("empty");
				return isEmpty;
			}else {
				FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS质检口状态查询，响应失败："+message);
				return false;
			}
		} catch (Exception e) {
			FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS质检口状态查询，接口调用异常："+e.getMessage());
			return false;
		}
	}

	@Override
	public void firstFloorQcPort() throws Exception {
		// 一楼托盘库质检口质检作业
		// 1查询是否有该质检口未开始的入库任务
		// 1.1无入库任务，查询在途是否有托盘已经到达质检口
		// 1.1.1无托盘到达质检口，return
		// 1.1.2有托盘到达质检口，查询质检口状态
		// 1.1.2.1质检口为空(质检托盘被取下)，将在途库存设置成质检等待状态(taskType70)，并清除entryCode信息，解锁质检口，end
		// 1.1.2.2质检口不为空，return
		// 1.2有入库任务，查询这些入库任务，在 在途库存中是否有质检等待状态的在途库存
		// 1.2.1无对应在途，return
		// 1.2.2有对应在途，恢复在途状态为质检作业(taskType20)，绑定entryCode(X060102)，锁定对应port口，end

		// 查询是否有该质检口未开始的入库任务
		List<String> intasks = wmsInboundTaskMapper.getSxkQcPortUndoInboundTaskPalletId();
		if(intasks.isEmpty()) {
			// 查询在途是否有托盘已经到达质检口
			List<ZtckContainer> ztckContainers = ztckContainerMapper.findByMap(MapUtils.put("taskStatus", 20).put("entryCode", "X060102").getMap(), ZtckContainer.class);
			if(ztckContainers.isEmpty()) {
				// 无托盘到达质检口
				return;
			}else {
				// 有托盘到达质检口
				// 查询质检口状态
				boolean isEmpty = this.getExitStatus("0100120065");
				if(isEmpty) {
					// 将在途库存设置成质检等待状态，并清除entryCode信息
					ZtckContainer ztckContainer = ztckContainers.get(0);
					ztckContainerMapper.updateMapById(ztckContainer.getContainerCode(), MapUtils.put("taskType", 70).put("entryCode", "").getMap(), ZtckContainer.class);
					// 解锁质检口
					portInfoMapper.updatePortInfoUnlock(1,12,65);
					FileLogHelper.WriteLog("firstFloorQcPort", "质检口托盘被拿下,母托盘【"+ ztckContainer.getContainerCode() +"】，子托盘【"+ztckContainer.getContainerSubCode()+"】" );
				}
			}
		}else {
			// 查询这些入库任务，在 在途库存中是否有质检等待状态的在途库存
			// intasks
			List<ZtckContainer> ztckContainers = ztckContainerMapper.findByMap(MapUtils.put("taskType", 70).put("entryCode", "").getMap(), ZtckContainer.class);
			for(ZtckContainer ztckContainer : ztckContainers) {
				if(intasks.contains(ztckContainer.getContainerSubCode())) {
					// 有对应在途，恢复在途状态为质检作业(taskType20)，绑定entryCode(X060102)，锁定对应port口
					ztckContainerMapper.updateMapById(ztckContainer.getContainerCode(), MapUtils.put("taskType", 20).put("entryCode", "X060102").getMap(), ZtckContainer.class);
					portInfoMapper.updatePortInfoLock(1,12,65);
				}
			}
		}
	}

	@Override
	public McsHoistStatusDto getHoistStatus(String hoistId) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hoistId", hoistId);
		String requestData = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/getHoistStatus");

			FileLogHelper.WriteLog("getHoistStatus", "EIS->MCS提升机状态查询，请求参数："+requestData);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(requestData), String.class);
			FileLogHelper.WriteLog("getHoistStatus", "EIS->MCS提升机状态查询，返回参数："+restJson);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");

			if (sucssess) {
				McsHoistStatusDto mcsHoistStatusDto = helper.getObject("data", McsHoistStatusDto.class);
				if(StringUtils.isEmpty(mcsHoistStatusDto.getHoistId()) || !hoistId.equals(mcsHoistStatusDto.getHoistId())) {
					FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS提升机状态查询失败：提升机编号错误"+hoistId);
					return null;
				}
				return mcsHoistStatusDto;
			}else {
				FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS提升机状态查询，响应失败："+message);
				return null;
			}
		} catch (Exception e) {
			FileLogHelper.WriteLog("getExitStatusError", "EIS->MCS提升机状态查询，接口调用异常："+e.getMessage());
			return null;
		}
	}

	@Override
	public McsGroupDirectionDto selectDirectionByExist(String coord) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("coord", coord);
		String requestData = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/selectDirectionByExist");

			FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向查询，请求参数："+requestData);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(requestData), String.class);
			FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向查询，返回参数："+restJson);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");

			if (sucssess) {
				McsGroupDirectionDto mcsGroupDirectionDto = helper.getObject("data", McsGroupDirectionDto.class);
				if(StringUtils.isEmpty(mcsGroupDirectionDto.getCoord()) || !coord.equals(mcsGroupDirectionDto.getCoord())) {
					FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向查询失败：groupnumber错误"+coord);
					return null;
				}
				return mcsGroupDirectionDto;
			}else {
				FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向查询，响应失败："+message);
				return null;
			}
		} catch (Exception e) {
			FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向查询，接口调用异常："+e.getMessage());
			return null;
		}
	}

	@Override
	public boolean updatePlcVariableByCoord(String coord, int direction) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("coord", coord);
		map.put("direction", direction);
		String requestData = PrologApiJsonHelper.toJson(map);
		String restJson = "";
		try {
			String postUrl = String.format("%s%s%s", mcsUrl, mcsPort, "Interface/updatePlcVariableByCoord");

			FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向修改，请求参数："+requestData);
			restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(requestData), String.class);
			FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向修改，返回参数："+restJson);

			PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
			Boolean sucssess = helper.getBoolean("ret");
			String message = helper.getString("msg");

			if (sucssess) {
				return sucssess;
			}else {
				FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向修改，响应失败："+message);
				throw new Exception(message);
			}
		} catch (Exception e) {
			FileLogHelper.WriteLog("selectDirectionByGroupNumber", "EIS->MCS提升机方向修改，接口调用异常："+e.getMessage());
			throw new Exception(e.getMessage());
		}
	}


    *//**
     * 小车跨层
     * @param sxCarAcrossTask
     * @param sxCarAcross
     * @return
     * @throws Exception
     *//*
    @Override
    public boolean checkMcsStatus(SxCarAcrossTask sxCarAcrossTask, SxCarAcross sxCarAcross) throws Exception {
        McsHoistStatusDto dto = getHoistStatus(sxCarAcrossTask.getHoistId());

        if (null == dto) {
            FileLogHelper.WriteLog("checkMcsStatus", MessageFormat.format("小车跨层任务发送失败,参数:{0},返回结果:{1}", sxCarAcrossTask.getHoistId(), JSON.toJSONString(dto)));
            return false;
        }
        *//**
         * layer 	int 【提升机当前楼层】
         * lock 【提升机状态 0未锁定  1锁定】
         * status【提升机状态 1：手动，2：自动，3：故障】
         *//*
        int lock = dto.getLock();
        int layer = dto.getLayer();
        int stauts = dto.getStatus();

        if (lock != 1 || stauts != 2) {
            FileLogHelper.WriteLog("checkMcsStatus", MessageFormat.format("小车跨层提升机检测,提升机状态不可用,返回结果:{1}", JSON.toJSONString(dto)));
            return false;
        }
        //判断状态是否可执行
        Integer taskType = sxCarAcrossTask.getTaskType();
        if (taskType == 2) {
            //进入提升机,判断源层
            if (sxCarAcross.getSourceLayer() != layer) {
                FileLogHelper.WriteLog("checkMcsStatus", MessageFormat.format("小车进入提升机任务失败,跨层任务源层：{0},提升机:{1},返回结果:{2}", sxCarAcross.getSourceLayer(), sxCarAcrossTask.getHoistId(), JSON.toJSONString(dto)));
                //层不匹配
                return false;
            }
        } else if (taskType == 3) {
            if (sxCarAcross.getTarLayer() != layer) {
                FileLogHelper.WriteLog("checkMcsStatus", MessageFormat.format("小车出提升机任务失败,跨层任务目标层：{0},提升机:{1},返回结果:{2}", sxCarAcross.getTarLayer(), sxCarAcrossTask.getHoistId(), JSON.toJSONString(dto)));
                //层不匹配
                return false;
            }
        } else {
            FileLogHelper.WriteLog("checkMcsStatus", MessageFormat.format("小车跨层任务发送,小车任务类型错误:{0}", taskType));
            return false;
        }
        return true;
    }

    @Override
    public void sendMcsCarAcrossPush(SxCarAcrossTask sxCarAcrossTask) {
        sxCarAcrossTask.setSendCount(sxCarAcrossTask.getSendCount() + 1);
        try {
            String postUrl = String.format("%s%s/%s", mcsUrl, mcsPort, "Interface/CrossLayer");
            String data = PrologApiJsonHelper.toJson(sxCarAcrossTask);
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(data), String.class);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("ret");
            FileLogHelper.WriteLog("acrossCarTask", MessageFormat.format("提升机跨层任务发送,参数:{0},返回结果:{1}", data, restJson));
            if (ret) {
                sxCarAcrossTask.setSendStatus(1);
                // 发送成功，改变跨层状态
                Integer taskType = sxCarAcrossTask.getTaskType();
                HashMap<String, Object> map = new HashMap<>(1);
                if (taskType == 1) {
                    map.put("acrossStatus", 1);
                } else if (taskType == 2) {
                    map.put("acrossStatus", 7);
                } else if (taskType == 3) {
                    map.put("acrossStatus", 11);
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
        sxCarAcrossTaskMapper.updateMapById(sxCarAcrossTask.getId(),
                MapUtils.put("sendCount", sxCarAcrossTask.getSendCount()).put("taskStatus", 1)
                        .put("sendStatus", sxCarAcrossTask.getSendStatus())
                        .put("sendErrMsg", sxCarAcrossTask.getSendErrMsg()).getMap(),
                SxCarAcrossTask.class);
    }*/

}
