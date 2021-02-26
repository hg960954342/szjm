package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.DeviceJunctionPortMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.dto.eis.SxStoreDto;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.eis.DeviceJunctionPort;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.MCSLineService;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.service.mcs.impl.McsInterfaceServiceSend;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MCSLineServiceImpl implements MCSLineService{

	@Autowired
	private DeviceJunctionPortMapper deviceJunctionPortMapper;
	@Autowired
	private ContainerTaskMapper containerTaskMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;

	@Autowired
	private PortInfoMapper portInfoMapper;
	@Autowired
	private AgvStorageLocationMapper agvStorageLocationMapper;
	@Autowired
	private McsInterfaceServiceSend mcsInterfaceServiceSend;
	@Autowired
	private McsInterfaceService mcsInterfaceService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void splitOutBound(String deviceNo) {
		DeviceJunctionPort deviceJunctionPort = deviceJunctionPortMapper.findById(deviceNo, DeviceJunctionPort.class);
		if(null == deviceJunctionPort) {
			LogServices.logSysBusiness("splitOutBoundInfo"+ "设备不存在" + deviceNo);
			return;
		}

		//获取设备位置
		String target = PrologCoordinateUtils.splicingStr(deviceJunctionPort.getX(), deviceJunctionPort.getY(), deviceJunctionPort.getLayer());
		
		//检查是否已经存在出库任务，否则找到空托盘
		List<ContainerTask> tasks = containerTaskMapper.findByMap(MapUtils.put("target", target).put("targetType", 2).getMap(), ContainerTask.class);
		if(!tasks.isEmpty()) {
			//已经有往拆盘机口送的出库任务了
			LogServices.logSysBusiness("splitOutBoundInfo"+ "已经有往出库口送的任务" + deviceNo);
			return;
		}
        //检查是否已经存在空托盘
        List<ContainerTask> nulltasks = containerTaskMapper.findByMap(MapUtils.put("target", target).put("targetType", 2).getMap(), ContainerTask.class);
        if(!nulltasks.isEmpty()) {
            //已经有往拆盘机口送的送空托盘了
            LogServices.logSysBusiness("splitOutBoundInfo"+ "已经有往出库口送空托盘任务" + deviceNo);
            return;
        }
		
		List<SxStoreDto> list = sxStoreMapper.getEmptyContainerCode();
		if(list.isEmpty()) {
			LogServices.logSysBusiness("splitOutBoundInfo"+ "库内无空托盘");
			return;
		}
		
		//找到距离呼叫点较进，移位数最浅的托盘
		list.sort((p1,p2)->{
			
			if(p1.getDeptNum() - p2.getDeptNum() < 0) {
				return -1;
			} else if(p1.getDeptNum() - p2.getDeptNum() > 0) {
				return 1;
			}else {
				//深度一样的情况需要计算距离
				int length1 = Math.abs(p1.getX() - deviceJunctionPort.getX()) + Math.abs(p1.getY() - deviceJunctionPort.getY());
				int length2 = Math.abs(p2.getX() - deviceJunctionPort.getX()) + Math.abs(p2.getY() - deviceJunctionPort.getY());
				
				return length1 - length2;
			}
		});
		
		SxStoreDto sxStoreDto = list.get(0);
		String source = PrologCoordinateUtils.splicingStr(sxStoreDto.getX(),sxStoreDto.getY(),sxStoreDto.getLayer());

		//生成空托任务，自带出库口
		ContainerTask containerTask = new ContainerTask();
		containerTask.getId();
		containerTask.setContainerCode(sxStoreDto.getContainerNo());
		containerTask.setTaskType(4);
		containerTask.setSource(source);
		containerTask.setSourceType(1);
		containerTask.setTarget(target);
		containerTask.setTargetType(OutBoundEnum.TargetType.SSX.getNumber());
		containerTask.setSourceType(1);
		containerTask.setTaskState(1);
		containerTask.setQty(new BigDecimal("1"));
		containerTask.setCreateTime(new Date());

		containerTaskMapper.save(containerTask);
	}
	
	@Override
	public void buildEmptyContainerSupply() throws Exception {
		
		//获取
		List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("portType", 2).put("taskType", 4).put("workType", 2)
				.put("callCar", 1).put("portlock", 2).put("taskLock", 2).getMap(), PortInfo.class);
		if(portInfos.isEmpty()) {
			return;
		}
		
		//获取补给点位
		PortInfo portInfo = portInfos.get(0);
		//获取agv区域的点位
		List<AgvStorageLocation> agvStorageLocations = agvStorageLocationMapper.findByMap(MapUtils.put("locationType", 2).put("deviceNo", portInfo.getJunctionPort()).getMap(), AgvStorageLocation.class);
		if(agvStorageLocations.isEmpty()) {
			LogServices.logSysBusiness("buildEmptyContainerSupplyInfo"+ "未找到agv区域对应点位");
			return;
		}
		
		AgvStorageLocation agvStorageLocation = agvStorageLocations.get(0);
		
		//检查当前点位是否存在agv任务
		List<ContainerTask> tasks = containerTaskMapper.findByMap(MapUtils.put("source", agvStorageLocation.getRcsPositionCode()).put("sourceType", 2).getMap(), ContainerTask.class);
		if(tasks.isEmpty()) {
			//判断mcs这个点位是否存在托盘
			String source = PrologCoordinateUtils.splicingStr(portInfo.getX(), portInfo.getY(), portInfo.getLayer());
			boolean exit = mcsInterfaceServiceSend.getExitStatus(source);
			mcsInterfaceService.updateBuildEmptyContainerSupply(exit,agvStorageLocation.getRcsPositionCode());

		}
	}
}
