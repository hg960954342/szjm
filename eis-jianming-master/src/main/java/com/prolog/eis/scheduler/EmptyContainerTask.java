package com.prolog.eis.scheduler;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.MCSLineService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class EmptyContainerTask {

    @Autowired
    private MCSLineService mcsLineService;

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    @Autowired
    private ContainerTaskService containerTaskService;

    /**
     * 检查3楼空托盘补给位是否存在托盘,创建托盘补给
     *
     * @throws Exception
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void buildEmptyContainerSupply()   {

        try {
            mcsLineService.buildEmptyContainerSupply();

        } catch (Exception e) {
            LogServices.logSys(new RuntimeException("buildEmptyContainerSupplyError"));
        }


    }


    /**
     * 补空托托盘
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void replenishContainer()   {
         Map<String, Object> map = MapUtils.put("ceng", 3).put("locationType", 1).put("taskLock", 0).put("locationLock", 0).getMap();
        List<AgvStorageLocation> agvStorageLocations = agvStorageLocationMapper.findByMap(map, AgvStorageLocation.class);

        //判断是否需要补空托盘
        if (agvStorageLocations != null && agvStorageLocations.size() > 0) {
            for (AgvStorageLocation agvStorageLocation : agvStorageLocations) {
                //判断是否有空托盘

                List<ContainerTask> containerTasks = containerTaskService.selectByTaskType("6");
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


}
