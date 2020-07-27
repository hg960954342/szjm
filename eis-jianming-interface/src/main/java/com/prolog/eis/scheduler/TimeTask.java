package com.prolog.eis.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prolog.eis.service.middle.MiddleTableNoticeService;
import com.prolog.eis.service.middle.MiddleTableSyncService;
import com.prolog.eis.util.FileLogHelper;

@Component
public class TimeTask {
	
	@Autowired
	private MiddleTableSyncService middleTableSyncService;
	
	@Autowired
	private MiddleTableNoticeService middleTableNoticeService;
	
	/**
	 * 定时同步出入库资料
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void syncWmsRawTrkInterface() throws Exception {	
		try {
			middleTableSyncService.syncWmsRawTrkInterface();
		} catch (Exception e) {
			FileLogHelper.WriteLog("syncWmsRawTrkInterface", " -->同步出入库资料错误,错误信息: "+ e.getMessage());
		}
	}
	
	/**
	 * 定时回告出入库资料中间表
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void noticeWmsRawTrkInterface() throws Exception {	
		try {
			middleTableNoticeService.noticeWmsRawTrkInterfaceInbound();
			middleTableNoticeService.noticeWmsRawTrkInterfaceOutbound();
		} catch (Exception e) {
			FileLogHelper.WriteLog("noticeWmsRawTrkInterface", " -->回告出入库资料错误,错误信息: "+ e.getMessage());
		}
	}
	
	/**
	 * 定时回告移库中间表
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void noticeWmsBaBinstaR() throws Exception {	
		try {
			middleTableNoticeService.noticeWmsBaBinstaR();
		} catch (Exception e) {
			FileLogHelper.WriteLog("noticeWmsBaBinstaR", " -->回告移库错误,错误信息: "+ e.getMessage());
		}
	}
	
	/**
	 *定时回告手动派车表
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void noticeWmsDispatchingInterface() throws Exception {	
		try {
			middleTableNoticeService.noticeWmsDispatchingInterface();
		} catch (Exception e) {
			FileLogHelper.WriteLog("noticeWmsDispatchingInterface", " -->发送派车任务,错误信息: "+ e.getMessage());
		}
	}
	
	/**
	 *agv入库任务分配port口
	 * @throws Exception
	 */
	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void noticeWmsSetPortInterface() throws Exception {	
		try {
			middleTableNoticeService.noticeWmsSetPort();
		} catch (Exception e) {
			FileLogHelper.WriteLog("noticeWmsSetPortInterface", " -->写入agv入库port口: "+ e.getMessage());
		}
	}
}
