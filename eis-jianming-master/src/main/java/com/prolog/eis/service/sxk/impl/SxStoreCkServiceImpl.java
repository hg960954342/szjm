package com.prolog.eis.service.sxk.impl;

import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dao.mcs.MCSTaskMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.dto.eis.CkContainerTaskDto;
import com.prolog.eis.dto.sxk.SxStoreGroupDto;
import com.prolog.eis.dto.sxk.SxStoreLock;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.sxk.SxInStoreService;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.eis.util.PrologTaskIdUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SxStoreCkServiceImpl implements SxStoreCkService{

	@Autowired
	private ContainerTaskMapper containerTaskMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxInStoreService sxInStoreService;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	@Autowired
	private MCSTaskMapper mcsTaskMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void buildSxCkTask() throws Exception {
		//获取需要出库的托盘
		List<ContainerTask> containerTasks = containerTaskMapper.findByMap(MapUtils.put("sourceType", 1).put("taskState", 1).getMap(), ContainerTask.class);

		//检查需要出库的托盘
		//获取托盘号，
		for (ContainerTask containerTask : containerTasks) {
			if(!StringUtils.isEmpty(containerTask.getTaskCode())) {
				//已经写过一次任务号，可能是超时等什么原因没发成功
				continue;
			}

			SxStoreLock sxStoreLock = sxStoreMapper.findSxStoreLock(containerTask.getContainerCode());
			if(null != sxStoreLock) {
				if(sxStoreLock.getStoreState() == 20 || sxStoreLock.getStoreState() == 31) {
					//到位状态可以出库
					//检查货位和货位组锁
					if (sxStoreLock.getAscentGroupLockState() == 1 || sxStoreLock.getAscentLockState() == 1 || sxStoreLock.getIsLock() == 1) {
						continue;
					}else {
						//判断是直接出库还是移位出库
						if(sxStoreLock.getDeptNum() == 0) {
							containOutLogic(containerTask);
						}else {
							containMoveLogic(sxStoreLock,containerTask);
						}
					}
				}
			}else {
				LogServices.logSysBusiness("buildSxCkTaskError"+ String.format("托盘出库无库存%s",containerTask.getContainerCode()));
				continue;
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void sendSxCkTask() {

		//健民只有一个任务出库口
		List<PortInfo> portList = portInfoMapper.findByMap(MapUtils.put("portType", 2).put("taskType", 1).put("portlock", 2).put("taskLock", 2).getMap(), PortInfo.class);
		if(portList.isEmpty()) {
			return;
		}
		PortInfo ckPortInfo = portList.get(0);

		//获取需要出库的托盘
		List<CkContainerTaskDto> ckContainerTasks = containerTaskMapper.getCkTask();
		for (CkContainerTaskDto ckContainerTask : ckContainerTasks) {
			//需要给mcs发送任务
			//往mcs重发表里写一条重发数据
			String targetPosiion = ckContainerTask.getTarget();
			if(ckContainerTask.getTargetType() == 1) {
				targetPosiion = PrologCoordinateUtils.splicingStr(ckPortInfo.getX(), ckPortInfo.getY(), ckPortInfo.getLayer());
			}

			String address = PrologCoordinateUtils.splicingStr(ckContainerTask.getX(), ckContainerTask.getY(), ckContainerTask.getLayer());

			MCSTask mcsTask = new MCSTask();
			mcsTask.setTaskId(ckContainerTask.getTaskCode());
			mcsTask.setBankId(1);
			mcsTask.setPriority("99");
			mcsTask.setAddress(address);
			mcsTask.setContainerNo(ckContainerTask.getContainerCode());
			mcsTask.setTarget(targetPosiion);
			mcsTask.setType(2);
			mcsTask.setWeight("0");
			mcsTask.setStatus(0);
			mcsTask.setSendCount(1);
			mcsTask.setCreateTime(new Date());
			mcsTask.setTaskState(2);
			mcsTask.setErrMsg("");
			mcsTaskMapper.save(mcsTask);
			
			//修改容器任务
			containerTaskMapper.updateMapById(ckContainerTask.getContainerTaskId(), MapUtils.put("taskState", 2).put("sendTime", new Date()).getMap(), ContainerTask.class);
		}
	}

	private void containOutLogic(ContainerTask containerTask) {
		// 修改库存状态
		sxStoreMapper.updateStoreState(containerTask.getContainerCode(), 30);
		// 修改货位组锁
		sxStoreLocationGroupMapper.updateState(1, containerTask.getContainerCode());
		// 修改容器任务
		// 生成mcs任务
		String taskCode = PrologStringUtils.newGUID();
		containerTask.setTaskCode(taskCode);

		containerTaskMapper.updateMapById(containerTask.getId(), MapUtils.put("taskCode", taskCode).getMap(), ContainerTask.class);
	}

	private void containMoveLogic(SxStoreLock sxStoreLock,ContainerTask containerTask) throws Exception {
		List<SxStoreGroupDto> sxStoreGroupDtos = sxStoreMapper.findStoreGroup(containerTask.getContainerCode());
		SxStore readyCkSxStore = sxStoreMapper.findById(sxStoreLock.getStoreId(), SxStore.class);
		SxStoreLocation presxStoreLocation = sxStoreLocationMapper.findById(sxStoreLock.getLocationId(), SxStoreLocation.class);
		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
				.findById(presxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);

		// 找出移库数为0的货位，并且库存状态是已上架
		List<SxStoreGroupDto> dtos = sxStoreGroupDtos.stream()
				.filter(t -> t.getDeptNum() == 0 && t.getStoreState() == 20).collect(Collectors.toList());

		sxStoreLocationGroupMapper.updateMapById(presxStoreLocation.getStoreLocationGroupId(),
				MapUtils.put("ascentLockState", 1).getMap(), SxStoreLocationGroup.class);

		Integer newStoreLocationId = 0;
		Integer newStoreId = 0;
		int layer = dtos.get(0).getLayer();
		//Integer newSourceLocationGroupId = 0;
		if (dtos.size() == 1) {
			SxStoreGroupDto dto = dtos.get(0);
			newStoreId = dto.getSotreId();
			SxStore sxStore = sxStoreMapper.findById(newStoreId, SxStore.class);
			SxStoreLocation sxStoreLocation3 = sxStoreLocationMapper.findById(sxStore.getStoreLocationId(),
					SxStoreLocation.class);
			newStoreLocationId = sxInStoreService.getInStoreDetail(dto.getContainerNo(), layer, dto.getTaskProperty1(),
					dto.getTaskProperty2(), dto.getX(), dto.getY(), 1, sxStoreLocationGroup.getBelongArea(),
					sxStoreLocation3.getActualWeight(), 3);
		}else if (dtos.size() == 2){
			SxStoreGroupDto dto1 = dtos.get(0);
			SxStoreGroupDto dto2 = dtos.get(1);
			int numOne = Math.abs(presxStoreLocation.getLocationIndex() - dto1.getLocationIndex());
			int numTwo = Math.abs(presxStoreLocation.getLocationIndex() - dto2.getLocationIndex());
			if (numTwo > numOne) {
				newStoreId = dto1.getSotreId();
				SxStore sxStore = sxStoreMapper.findById(newStoreId, SxStore.class);
				SxStoreLocation sxStoreLocation3 = sxStoreLocationMapper.findById(sxStore.getStoreLocationId(),
						SxStoreLocation.class);
				newStoreLocationId = sxInStoreService.getInStoreDetail(dto1.getContainerNo(), layer,
						dto1.getTaskProperty1(), dto1.getTaskProperty2(), dto1.getX(), dto1.getY(), 1,
						sxStoreLocationGroup.getBelongArea(), sxStoreLocation3.getActualWeight(), 3);
			} else {
				newStoreId = dto2.getSotreId();
				SxStore sxStore = sxStoreMapper.findById(newStoreId, SxStore.class);
				SxStoreLocation sxStoreLocation3 = sxStoreLocationMapper.findById(sxStore.getStoreLocationId(),
						SxStoreLocation.class);
				newStoreLocationId = sxInStoreService.getInStoreDetail(dto2.getContainerNo(), layer,
						dto2.getTaskProperty1(), dto2.getTaskProperty2(), dto2.getX(), dto2.getY(), 1,
						sxStoreLocationGroup.getBelongArea(), sxStoreLocation3.getActualWeight(), 3);
			}
		}else {
			throw new Exception("货位【"+presxStoreLocation.getStoreLocationGroupId()+"】资料有误！同货位组有库存托盘移位数为0的货位大于2个！");
		}
		SxStore sxStore = sxStoreMapper.findById(newStoreId, SxStore.class);
		SxStoreLocation targetLocation = sxStoreLocationMapper.findById(newStoreLocationId, SxStoreLocation.class);
		SxStoreLocation sourceLocation = sxStoreLocationMapper.findById(sxStore.getStoreLocationId(),
				SxStoreLocation.class);

		sxStoreMapper.updateMapById(readyCkSxStore.getId(), MapUtils.put("storeState", 31).getMap(), SxStore.class);
		sxStoreLocationMapper.updateMapById(sourceLocation.getId(), MapUtils.put("actualWeight", 0).getMap(),
				SxStoreLocation.class);

		// 修改目的货位的各种锁
		sxStoreMapper.updateMapById(newStoreId, MapUtils.put("storeLocationId", newStoreLocationId).put("storeState", 40)
				.put("sourceLocationId", sxStore.getStoreLocationId()).getMap(), SxStore.class);
		sxStoreLocationGroupMapper.updateMapById(targetLocation.getStoreLocationGroupId(),
				MapUtils.put("ascentLockState", 1).getMap(), SxStoreLocationGroup.class);
		sxStoreLocationMapper.updateMapById(newStoreLocationId,
				MapUtils.put("actualWeight", sourceLocation.getActualWeight())
				.put("limitWeight", sourceLocation.getLimitWeight()).getMap(),
				SxStoreLocation.class);

		sxStoreTaskFinishService.computeLocation(sxStore);

		String address = PrologCoordinateUtils.splicingStr(sourceLocation.getX(), sourceLocation.getY(), sourceLocation.getLayer());
		String target = PrologCoordinateUtils.splicingStr(targetLocation.getX(), targetLocation.getY(), targetLocation.getLayer());

		//往mcs重发表里写一条重发数据
		String taskId = PrologTaskIdUtils.getTaskId();
		MCSTask mcsTask = new MCSTask();
		mcsTask.setTaskId(taskId);
		mcsTask.setBankId(1);
		mcsTask.setPriority("99");
		mcsTask.setAddress(address);
		mcsTask.setContainerNo(sxStore.getContainerNo());
		mcsTask.setTarget(target);
		mcsTask.setType(3);
		mcsTask.setWeight("0");
		mcsTask.setStatus(0);
		mcsTask.setSendCount(1);
		mcsTask.setCreateTime(new Date());
		mcsTask.setTaskState(2);
		mcsTask.setErrMsg("");
		mcsTaskMapper.save(mcsTask);
	}
}
