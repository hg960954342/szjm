package com.prolog.eis.service.middle.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.eis.PortInfoMapper;
import com.prolog.eis.dao.eis.WmsCallCarTaskHistoryMapper;
import com.prolog.eis.dao.eis.WmsCallCarTaskMapper;
import com.prolog.eis.dao.eis.WmsInboundTaskHistoryMapper;
import com.prolog.eis.dao.eis.WmsInboundTaskMapper;
import com.prolog.eis.dao.eis.WmsMoveTaskHistoryMapper;
import com.prolog.eis.dao.eis.WmsMoveTaskMapper;
import com.prolog.eis.dao.eis.WmsOutboundTaskHistoryMapper;
import com.prolog.eis.dao.eis.WmsOutboundTaskMapper;
import com.prolog.eis.dao.middle.WmsBaBinstaRMapper;
import com.prolog.eis.dao.middle.WmsDispatchingMapper;
import com.prolog.eis.dao.middle.WmsRawTrkInterfaceMapper;
import com.prolog.eis.dto.eis.InboundTaskPortDto;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.eis.WmsCallCarTask;
import com.prolog.eis.model.eis.WmsInboundTask;
import com.prolog.eis.model.eis.WmsMoveTask;
import com.prolog.eis.model.eis.WmsOutboundTask;
import com.prolog.eis.model.middle.WmsBaBinstaR;
import com.prolog.eis.model.middle.WmsDispatchingInterface;
import com.prolog.eis.service.middle.MiddleTableNoticeService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.ListHelper;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;

@Service
public class MiddleTableNoticeServiceImpl implements MiddleTableNoticeService {

	@Value("${spring.datasource2.wmsDatasourceType:}")
	private String wmsDatasourceType;
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;
	@Autowired
	private WmsInboundTaskHistoryMapper wmsInboundTaskHistoryMapper;
	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;
	@Autowired
	private WmsOutboundTaskHistoryMapper wmsOutboundTaskHistoryMapper;
	@Autowired
	private WmsMoveTaskMapper wmsMoveTaskMapper;
	@Autowired
	private WmsMoveTaskHistoryMapper wmsMoveTaskHistoryMapper;
	@Autowired
	private WmsRawTrkInterfaceMapper wmsRawTrkInterfaceMapper;
	@Autowired
	private WmsBaBinstaRMapper wmsBaBinstaRMapper;
	@Autowired
	private WmsDispatchingMapper wmsDispatchingMapper;
	@Autowired
	private WmsCallCarTaskMapper wmsCallCarTaskMapper;
	@Autowired
	private WmsCallCarTaskHistoryMapper wmsCallCarTaskHistoryMapper;
	@Autowired
	private PortInfoMapper portInfoMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void noticeWmsRawTrkInterfaceInbound() {
		List<WmsInboundTask> inboundTasks = wmsInboundTaskMapper.getTaskProgressData();
		for(WmsInboundTask t : inboundTasks) {
			try {
				if(!StringUtils.isEmpty(t.getWmsDatasourceType()) && !wmsDatasourceType.equals(t.getWmsDatasourceType())) {
					// 只回传对应系统的数据,标识为空也可以处理
					continue;
				}
				
				//当任务类型为20的时候需要写入port口编号
				if(t.getReport() == 1){
					if(t.getFinished() == 9) {
						//写入port口
						wmsRawTrkInterfaceMapper.noticeInboundPort(t.getFinished(), t.getPortNo(),t.getCommandNo());
						
						wmsInboundTaskMapper.updateMapById(t.getId(), MapUtils.put("report", 0).getMap(), WmsInboundTask.class);
						
						FileLogHelper.WriteLog("noticeWmsRawTrkInterface", "写入port口"+t.getCommandNo()+"  " + t.getPortNo());
					}else if(t.getFinished() > 9) {
						//写入binNo
						wmsRawTrkInterfaceMapper.noticeInboundBin(t.getBinNo(),t.getCommandNo());
						
						wmsInboundTaskMapper.updateMapById(t.getId(), MapUtils.put("report", 0).getMap(), WmsInboundTask.class);

						FileLogHelper.WriteLog("noticeWmsRawTrkInterface", "写入binNo"+t.getCommandNo()+"  " + t.getBinNo());
					}
				}
				if(t.getFinished() >= 90) {
					if(t.getReport() == 1) {
						if(t.getFinished() == 92) {
							// 强制取消，err_code赋值92
							wmsRawTrkInterfaceMapper.forceFinish(t.getFinished(), t.getCommandNo());
						}else {
							wmsRawTrkInterfaceMapper.noticeInboundFinished(t.getBinNo(), t.getWeight(), t.getFinished(), t.getCommandNo());
						}
					}
					// 转历史
					wmsInboundTaskHistoryMapper.backupById(t.getId()+"");
					wmsInboundTaskMapper.deleteById(t.getId(), WmsInboundTask.class);
				}
			} catch (Exception e) {
				FileLogHelper.WriteLog("noticeWmsRawTrkInterface", "回告入库错误[" + t.getCommandNo() + "][" + t.getPalletId()
						+ "][" + t.getContainerCode() + "],错误信息: " + e.getMessage());
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void noticeWmsRawTrkInterfaceOutbound() {
		List<WmsOutboundTask> outboundTasks = wmsOutboundTaskMapper.getTaskProgressData();
		for(WmsOutboundTask t : outboundTasks) {
			try {
				if(!StringUtils.isEmpty(t.getWmsDatasourceType()) && !wmsDatasourceType.equals(t.getWmsDatasourceType())) {
					// 只回传对应系统的数据,标识为空也可以处理
					continue;
				}
				if(t.getReport() == 1){
					// 回告中间表
					if(t.getFinished() == 92) {
						// 强制取消
						wmsRawTrkInterfaceMapper.forceFinish(t.getFinished(), t.getCommandNo());
					}else {
						wmsRawTrkInterfaceMapper.noticeOutboundFinished(t.getPortNo(), t.getFinished(), t.getCommandNo());
					}
					
					wmsOutboundTaskMapper.updateMapById(t.getId(), MapUtils.put("report", 0).getMap(), WmsOutboundTask.class);
				}
				if(t.getFinished() >= 90) {
					// 转历史
					wmsOutboundTaskHistoryMapper.backupById(t.getId()+"");
					wmsOutboundTaskMapper.deleteById(t.getId(), WmsOutboundTask.class);
				}
			} catch (Exception e) {
				FileLogHelper.WriteLog("noticeWmsRawTrkInterface", "回告出库错误[" + t.getCommandNo() + "][" + t.getPalletId()
						+ "][" + t.getContainerCode() + "],错误信息: " + e.getMessage());
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void noticeWmsBaBinstaR() {
		List<WmsMoveTask> moveTasks = wmsMoveTaskMapper.findByMap(MapUtils.put("finished", 90).getMap(), WmsMoveTask.class);
		for(WmsMoveTask t : moveTasks) {
			try {
				if(!StringUtils.isEmpty(t.getWmsDatasourceType()) && !wmsDatasourceType.equals(t.getWmsDatasourceType())) {
					// 只回传对应系统的数据,标识为空也可以处理
					continue;
				}
				if(t.getReport() == 1) {
					WmsBaBinstaR wmsBaBinstaR = new WmsBaBinstaR();
					wmsBaBinstaR.setWhNo(t.getWhNo());
					wmsBaBinstaR.setAreaNo(t.getAreaNo());
					wmsBaBinstaR.setBinNo(t.getBinNo());
					wmsBaBinstaR.setPalletId(t.getPalletId());
					wmsBaBinstaR.setFinished(0);
					wmsBaBinstaR.setErrMsg(t.getErrMsg());
					wmsBaBinstaR.setErrCode("200");
					wmsBaBinstaR.setCreateTime(t.getCreateTime());
					wmsBaBinstaR.setFlag("N");
					// 回告中间表
					wmsBaBinstaRMapper.save(wmsBaBinstaR);
				}
				// 转历史
				wmsMoveTaskHistoryMapper.backupById(t.getId()+"");
				wmsMoveTaskMapper.deleteById(t.getId(), WmsMoveTask.class);
			} catch (Exception e) {
				FileLogHelper.WriteLog("noticeWmsBaBinstaR", "回告移库错误["+t.getPalletId()+"],错误信息: "+ e.getMessage());
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void noticeWmsDispatchingInterface() {
		List<WmsCallCarTask> callCarTasks = wmsCallCarTaskMapper.findByMap(MapUtils.put("finished", 90).getMap(), WmsCallCarTask.class);
		for(WmsCallCarTask t : callCarTasks) {
			try {
				if(!StringUtils.isEmpty(t.getWmsDatasourceType()) && !wmsDatasourceType.equals(t.getWmsDatasourceType())) {
					// 只回传对应系统的数据,标识为空也可以处理
					continue;
				}
				WmsDispatchingInterface in = new WmsDispatchingInterface();
				String palletId = StringUtils.isEmpty(t.getPalletId())?System.currentTimeMillis()+"":t.getPalletId();
				in.setWhNo(t.getWhNo());
				in.setAreaNo(t.getAreaNo());
				in.setPalletId(palletId);
				in.setProductId(t.getProductId());
				in.setFactoryNo(t.getFactoryNo());
				in.setMaterielType(t.getMaterielType());
				in.setMaterielName(t.getMaterielName());
				in.setIo(t.getIo());
				in.setStations(t.getStations());
				in.setPort(t.getPort());
				in.setFinished(0);
				in.setErrCode(t.getErrCode());
				in.setErrMsg(t.getErrMsg());
				in.setFlag("N");
				in.setCreatTime(new Date());
				
				wmsCallCarTaskMapper.updateMapById(t.getId(), MapUtils.put("palletId", palletId).getMap(), WmsCallCarTask.class);
				
				// 回告中间表
				wmsDispatchingMapper.save(in);
				// 转历史
				wmsCallCarTaskHistoryMapper.backupById(t.getId()+"");
				wmsCallCarTaskMapper.deleteById(t.getId(), WmsCallCarTask.class);
			} catch (Exception e) {
				FileLogHelper.WriteLog("noticeWmsDispatchingInterface", "发送派车任务["+t.getPalletId()+"]["+t.getIo()+"]["+t.getStations()+"],错误信息: "+ e.getMessage());
			}
		}
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void noticeWmsSetPort() {
		List<WmsInboundTask> inboundTasks = wmsInboundTaskMapper.findByMap(new HashMap<>(), WmsInboundTask.class);
		
		// 构建port和任务数的对应关系
		Map<String, Integer> portTaskCount = new HashMap<String, Integer>();
		for(WmsInboundTask task : inboundTasks) {
			if(StringUtils.isEmpty(task.getPortNo())) {
				continue;
			}
			Integer count = portTaskCount.get(task.getPortNo());
			if(count == null) {
				portTaskCount.put(task.getPortNo(), 1);
			}else {
				count++;
			}
		}
		
		// 所有未执行的任务,已经分配port口的任务不重新分配
		List<WmsInboundTask> tasks = inboundTasks.stream().filter(t -> (t.getFinished() == 0 && StringUtils.isEmpty(t.getPortNo()))).collect(Collectors.toList());
		
		for(WmsInboundTask task : tasks) {
			// 根据任务类型和stations找对应的port口
			String taskType = "0";
			if(task.getTaskType() == 10) {
				// 一般任务
				taskType = "1";
			}else if(task.getTaskType() == 30) {
				// 空托作业
				taskType = "1, 3";
			}else if(task.getTaskType() == 40) {
				// 包材
				taskType = "2";
			}else {
				// 其他（质检）
				continue;
			}
			List<PortInfo> portInfos = portInfoMapper.getInboundPortInfoByStationsAndTaskType(task.getStations(), taskType);
			
			for(PortInfo port : portInfos) {
				if(port.getPortType() != 1) {
					continue;
				}
				Integer count = portTaskCount.get(port.getWmsPortNo()) == null ? 0 : portTaskCount.get(port.getWmsPortNo());
				int maxRkCount = port.getMaxRkCount();
				if(count < maxRkCount) {
					// port口任务数未满，将任务分配至port口
					wmsInboundTaskMapper.updateMapById(task.getId(), 
							MapUtils.put("portNo", port.getWmsPortNo()).put("finished", 9).put("report", task.getWmsPush()).getMap(), WmsInboundTask.class);
					
					// 任务数加1
					count++;
					if(task.getWmsPush() == 0) {
						// 手动入库任务，分配port口后呼叫agv
						WmsCallCarTask wmsCallCarTask = new WmsCallCarTask();
						wmsCallCarTask.setWhNo("HA_WH");
						wmsCallCarTask.setAreaNo("HAC_ASRS");
						wmsCallCarTask.setPalletId(task.getPalletId());
						wmsCallCarTask.setProductId(task.getMaterielNo());
						wmsCallCarTask.setFactoryNo(task.getFactoryNo());
						wmsCallCarTask.setMaterielType(task.getMaterielType());
						wmsCallCarTask.setMaterielName(task.getMaterielName());
						wmsCallCarTask.setIo("I");
						wmsCallCarTask.setStations(task.getStations());
						wmsCallCarTask.setPort(port.getWmsPortNo());
						wmsCallCarTask.setFinished(90);
						wmsCallCarTask.setErrCode(200);
						wmsCallCarTask.setCreatTime(new Date());

						wmsCallCarTaskMapper.save(wmsCallCarTask);
					}
					
					break;
				}
			}
		}
	}
}
