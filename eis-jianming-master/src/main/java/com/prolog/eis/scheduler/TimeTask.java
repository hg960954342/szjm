package com.prolog.eis.scheduler;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.model.mcs.MCSTask;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.*;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.service.mcs.McsInterfaceService;
import com.prolog.eis.service.sxk.SxStoreCkService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
public class TimeTask {

    @Autowired
    InBoundTaskService inBoundTaskService;
    @Autowired
    OutBoundTaskService outBoundTaskService;
    @Autowired
    private McsInterfaceService mcsInterfaceService;
    @Autowired
    private SxStoreCkService sxStoreCkService;
    @Autowired
    private MCSLineService mcsLineService;

    /**
     * 定时处理入库任务
     *
     * @throws Exception
     */
//    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildCkTask() throws Exception {
        inBoundTaskService.inboundTask();
    }


    /**
     * 定时出库任务
     *
     * @throws Exception
     */
//    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildUnTask() throws Exception {
        outBoundTaskService.unboundTask();

    }

    //    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildAndSendSxCkTask() throws Exception {

        try {
            synchronized ("kucun".intern()) {
                sxStoreCkService.buildSxCkTask();
            }
            sxStoreCkService.sendSxCkTask();
        } catch (Exception e) {
            // TODO: handle exception
            FileLogHelper.WriteLog("生成四向库出库任务错误", e.toString());
        }
    }



	/*@Scheduled(initialDelay = 3000, fixedDelay = 8000)
	public void sendWcsTask() throws Exception {

		synchronized ("sendwcstask".intern()) {
			wcsTaskPriorityService.sendWcsTask();
		}
	}*/

    //	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void resendMcsTask() throws Exception {
        List<MCSTask> mcsTasks = mcsInterfaceService.findFailMCSTask();
        for (MCSTask mcsTask : mcsTasks) {
            try {
                mcsInterfaceService.recall(mcsTask);
            } catch (Exception e) {
                FileLogHelper.WriteLog("resendMcsTask", "MCS重发异常:" + e.toString());
            }
        }
    }

	/*@Scheduled(initialDelay = 3000, fixedDelay = 5000)
	public void resendGcsTask()throws Exception{
		List<GcsTask> gcsTasks = gcsInterfaceService.findRecallGCSTask();
		for (GcsTask gcsTask : gcsTasks) {
			try {
				gcsInterfaceService.recall(gcsTask);
			}catch (Exception e) {
				FileLogHelper.WriteLog("resendGcsTask", "GCS重发异常:"+e.toString());
			}
		}
	}*/

    @Autowired
    private ContainerTaskService containerTaskService;

    @Autowired
    private EisSendRcsTaskService eisSendRcsTaskService;

    @Autowired
    private RepeatReportService repeatReportService;

    @Autowired
    private EisCallbackService eisCallbackService;

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    //定时给agv小车下分任务
//	@Scheduled(initialDelay = 3000,fixedDelay = 5000)
    public void sendTask2Rcs() throws Exception {
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskStateAndSourceType("1", "2");
        if (!containerTasks.isEmpty() && containerTasks.size() > 0) {
            eisSendRcsTaskService.sendTask(containerTasks);
        }
    }

    /**
     * 回告wms未成功 重复回告
     *
     * @throws Exception
     */
//	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void resendReport() throws Exception {
        List<RepeatReport> repeatReports = repeatReportService.findByState(0);
        if (repeatReports != null && repeatReports.size() > 0) {
            for (RepeatReport repeatReport : repeatReports) {
                eisCallbackService.recall(repeatReport);
            }
        }
    }

    /**
     * 补空托托盘
     */
//	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void replenishContainer() throws Exception {
        Map<String, Object> map = MapUtils.put("ceng", 3).put("locationType", 1).put("taskLock", 0).put("locationLock", 0).getMap();
        List<AgvStorageLocation> agvStorageLocations = agvStorageLocationMapper.findByMap(map, AgvStorageLocation.class);

        //判断是否需要补空托盘
        if (agvStorageLocations != null && agvStorageLocations.size() > 0) {
            for (AgvStorageLocation agvStorageLocation : agvStorageLocations) {
                //判断是否有空托盘
                List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode("6");
                if (containerTasks != null && containerTasks.size() > 0) {
                    //生成容器任务container_task 托盘号uuid,task_type 待定，source....
                    ContainerTask containerTask = containerTasks.get(0);
                    containerTask.setTarget(agvStorageLocation.getRcsPositionCode());
                    containerTask.setTargetType(1);
                    containerTaskService.update(containerTask);
                }
            }

        }
    }

    /**
     * 检查3楼空托盘补给位是否存在托盘,创建托盘补给
     *
     * @throws Exception
     */
//	@Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildEmptyContainerSupply() throws Exception {
        try {
            mcsLineService.buildEmptyContainerSupply();
        } catch (Exception e) {
            FileLogHelper.WriteLog("buildEmptyContainerSupplyError", e.toString());
        }
    }


    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void testReport() throws Exception {
        ContainerTask containerTask = new ContainerTask();
//		containerTask.setContainerCode("800011");
//        containerTask.setContainerCode("800012");//出库
//        containerTask.setSource("057200AB054000");
//		containerTask.setContainerCode("700010");//移库
//		containerTask.setTaskType(2);
//		containerTask.setItemId("SPH00001363");
//		containerTask.setOwnerId("008");
//		eisCallbackService.inBoundReport("800027");//入库
//		eisCallbackService.inBoundReport("800045");//移入回告
//        eisCallbackService.outBoundReport(containerTask);
        AgvStorageLocation byRcs = agvStorageLocationMapper.findByRcs("060080AB054000");

//		eisCallbackService.moveBoundReport(containerTask);
//		eisCallbackService.checkBoundReport("PDC00000101");


    }

    @Autowired
    private WmsLoginService wmsLoginService;
/*

    //刷新token
    @Scheduled(cron = "0/1 * * * * ? ")
    public void getToken() {
        long currnetTime = System.currentTimeMillis() / 1000;
        long deviationTime = currnetTime - LoginWmsResponse.getTokenTime;
        if (StringUtils.isEmpty(LoginWmsResponse.accessToken) || StringUtils.isEmpty(LoginWmsResponse.expiresIn) || deviationTime >= Integer.parseInt(LoginWmsResponse.expiresIn)) {
            wmsLoginService.loginWms();
        }
    }
*/


}
