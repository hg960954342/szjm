package com.prolog.eis.service.middle.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.eis.WmsInboundTaskMapper;
import com.prolog.eis.dao.eis.WmsOutboundTaskMapper;
import com.prolog.eis.dao.middle.WmsRawTrkInterfaceMapper;
import com.prolog.eis.model.eis.WmsInboundTask;
import com.prolog.eis.model.eis.WmsOutboundTask;
import com.prolog.eis.model.middle.WmsRawTrkInterface;
import com.prolog.eis.service.middle.MiddleTableSyncService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;

@Service
public class MiddleTableSyncServiceImpl implements MiddleTableSyncService {

	@Value("${spring.datasource2.wmsDatasourceType:}")
	private String wmsDatasourceType;
	
	@Autowired
	private WmsInboundTaskMapper wmsInboundTaskMapper;

	@Autowired
	private WmsOutboundTaskMapper wmsOutboundTaskMapper;

	@Autowired
	private WmsRawTrkInterfaceMapper wmsRawTrkInterfaceMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void syncWmsRawTrkInterface() throws Exception {
		List<WmsInboundTask> inboundTasks = new ArrayList<WmsInboundTask>();
		List<WmsOutboundTask> outboundTasks = new ArrayList<WmsOutboundTask>();
		List<WmsRawTrkInterface> interfaces = wmsRawTrkInterfaceMapper.getUnsyncData();

		List<String> inCode = new ArrayList<String>();
		List<String> outCode = new ArrayList<String>();
		// 质检入库许可标识，同一时间，只允许有一个质检口入库
		// qcFlag true已经存在质检口入库任务  false没有质检口入库任务
		// 获取质检口对应的叫料解包区
		List<String> qcStations = wmsInboundTaskMapper.getQcStations();
		boolean qcFlag = this.validateQcInboundTask(qcStations);

		for (WmsRawTrkInterface i : interfaces) {
			try {
				if ("I".equals(i.getIo())) {
					if (inCode.contains(i.getPalletId())) {
						continue;
					}
					if (i.getWmsBack() == null && i.getWmsBack() != 1) {
						continue;
					}
					WmsInboundTask inboundTask = new WmsInboundTask();
					inboundTask.setWmsDatasourceType(wmsDatasourceType);
					inboundTask.setCommandNo(i.getCommandNo());
					inboundTask.setWmsPush(1);
					inboundTask.setWhNo(i.getWhNo());
					inboundTask.setAreaNo(i.getAreaNo());
					inboundTask.setPalletId(i.getPalletId());
					if (!"C".equals(i.getPalletSize()) && !"P".equals(i.getPalletSize())) {
						wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), "palletSize錯誤");
						continue;
					}

					int lxkcount = wmsInboundTaskMapper.getStackerStoreByBoxNo(i.getPalletId());
					int sxkcount = wmsInboundTaskMapper.getSxkStoreByBoxNo(i.getPalletId());
					if ("PPBOX".equals(i.getTaskType())) {
						
					}else if ("C".equals(i.getPalletSize())) {
						// 料箱库
						if (lxkcount != 0) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存已存在");
							continue;
						}
						if (sxkcount != 0) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存在托盤庫");
							continue;
						}
					} else if ("P".equals(i.getPalletSize())) {
						// 四向库
						if (sxkcount != 0) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存已存在");
							continue;
						}
						if(lxkcount != 0) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存在料箱庫");
							continue;
						}
						
						if(qcFlag) {
							// 已经存在质检口入库任务，不允许继续创建质检口入库任务
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), "已經存在質檢口的入庫任務");
							continue;
						}else {
							// 没有质检口的入库任务，可以继续创建质检口入库任务，当创建一个质检口入库任务之后，则不允许再创建了
							if("IQC".equals(i.getTaskType()) && qcStations.contains(i.getStations())) {
								qcFlag = true;
							}
						}
					}

					inboundTask.setPalletSize(i.getPalletSize());
					if (!StringUtils.isEmpty(i.getPalletId())) {
						List<WmsInboundTask> list = wmsInboundTaskMapper
								.findByMap(MapUtils.put("palletId", i.getPalletId()).getMap(), WmsInboundTask.class);
						if (!list.isEmpty()) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), "palletId重複");
							continue;
						}
					}
					inboundTask.setMatType(i.getMatType());
					inboundTask.setWeight(i.getWeight());
					inboundTask.setFinished(i.getFinished());
					inboundTask.setBinNo(i.getBinNo());
					inboundTask.setErrMsg(i.getErrMsg());
					inboundTask.setCreateTime(new Date());
					inboundTask.setStations(i.getStations());
					inboundTask.setBoxCount(i.getBoxCount() == null ? "" : i.getBoxCount()+"");

					if ("NORMAL".equals(i.getTaskType())) {
						inboundTask.setTaskType(10);
					} else if ("IQC".equals(i.getTaskType())) {
						inboundTask.setTaskType(20);
					} else if ("PPBOX".equals(i.getTaskType())) {
						inboundTask.setTaskType(30);
					} else if ("PACK".equals(i.getTaskType())) {
						inboundTask.setTaskType(40);
					} else {
						wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(),
								String.format("tasktype异常 %s", i.getTaskType()));
						continue;
					}

					String materielNo = StringUtils.isEmpty(i.getProductId())?"":(i.getProductId().length()>201?i.getProductId().substring(0, 200):i.getProductId());
					inboundTask.setMaterielNo(materielNo);
					inboundTask.setFactoryNo(i.getVendorCode());
					inboundTask.setMaterielType(null);
					inboundTask.setMaterielName(null);

					inCode.add(i.getPalletId());
					inboundTasks.add(inboundTask);
				} else if ("O".equals(i.getIo())) {
					if (outCode.contains(i.getPalletId())) {
						continue;
					}
					WmsOutboundTask outboundTask = new WmsOutboundTask();
					outboundTask.setWmsDatasourceType(wmsDatasourceType);
					outboundTask.setGroupId(i.getGroupId());
					outboundTask.setCommandNo(i.getCommandNo());
					outboundTask.setWmsPush(1);
					outboundTask.setWhNo(i.getWhNo());
					outboundTask.setAreaNo(i.getAreaNo());
					outboundTask.setPalletId(i.getPalletId());
					if (!StringUtils.isEmpty(i.getPalletId())) {
						List<WmsOutboundTask> list = wmsOutboundTaskMapper
								.findByMap(MapUtils.put("palletId", i.getPalletId()).getMap(), WmsOutboundTask.class);
						if (!list.isEmpty()) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), "palletId重複");
							continue;
						}
					}
					
					if(StringUtils.isEmpty(i.getPalletSize())) {
						int sxCount = wmsOutboundTaskMapper.countSxStore(i.getPalletId());
						if (sxCount == 0) {
							int stackerCount = wmsOutboundTaskMapper.countStackerStore(i.getPalletId());
							if (stackerCount == 0) {
								wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存不存在");
								continue;
							} else {
								outboundTask.setPalletSize("C");
							}
						} else {
							outboundTask.setPalletSize("P");
						}
					}else if("C".equals(i.getPalletSize())){
						int stackerCount = wmsOutboundTaskMapper.countStackerStore(i.getPalletId());
						if (stackerCount == 0) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存在料箱庫不存在");
							continue;
						}
					}else if("P".equals(i.getPalletSize())){
						int sxCount = wmsOutboundTaskMapper.countSxStore(i.getPalletId());
						if (sxCount == 0) {
							wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), i.getPalletId() + "的庫存在托盤庫不存在");
							continue;
						}
					}else {
						wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), "pallet_size:[" + i.getPalletSize() + "]不正確");
						continue;
					}
					
					
					outboundTask.setPalletSize(i.getPalletSize());
					outboundTask.setEmerge(i.getEmerge());
					if (StringUtils.isEmpty(i.getStations())) {
						wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), "無叫料解包區");
						continue;
					} else {
						outboundTask.setStations(i.getStations());
					}
					outboundTask.setPortNo(i.getPort());
					outboundTask.setFinished(i.getFinished());
					outboundTask.setErrMsg(i.getErrMsg());
					outboundTask.setCreateTime(new Date());

					if ("NORMAL".equals(i.getTaskType())) {
						outboundTask.setTaskType(10);
					} else if ("IQC".equals(i.getTaskType())) {
						outboundTask.setTaskType(20);
					} else if ("PPBOX".equals(i.getTaskType())) {
						outboundTask.setTaskType(30);
					} else if ("PACK".equals(i.getTaskType())) {
						outboundTask.setTaskType(40);
					} else {
						wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(),
								String.format("tasktype異常 %s", i.getTaskType()));
						continue;
					}

					outboundTask.setOutboundTime(null);

					outCode.add(i.getPalletId());
					outboundTasks.add(outboundTask);
				}

				wmsRawTrkInterfaceMapper.hasSync(i.getCommandNo());
			} catch (Exception e) {
				// TODO: handle exception
				FileLogHelper.WriteLog("sync", i.getCommandNo() + "错误" + e.getMessage());
				// wmsRawTrkInterfaceMapper.hasErrorSync(i.getCommandNo(), e.getMessage());
				String errorStack = e.getStackTrace().length > 0 ? e.getStackTrace()[0].getClassName() + e.getStackTrace()[0].getLineNumber() : "";
				FileLogHelper.WriteLog("sync", "MCS->EIS请求" + errorStack);

			}
		}

		if (inboundTasks.size() > 0) {
			wmsInboundTaskMapper.saveBatch(inboundTasks);
		}
		if (outboundTasks.size() > 0) {
			wmsOutboundTaskMapper.saveBatch(outboundTasks);
		}
	}

	private boolean validateQcInboundTask(List<String> qcStations) {
		List<WmsInboundTask> tasks = wmsInboundTaskMapper.findByMap(MapUtils.put("palletSize", "P").put("taskType", 20).put("finished", 0).getMap(), WmsInboundTask.class);
		for(WmsInboundTask task : tasks) {
			if(qcStations.contains(task.getStations())) {
				return true;
			}
		}
		return false;
	}
}