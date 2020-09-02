package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.controller.led.PrologLedController;
import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.CheckOutTaskMapper;
import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.led.LedShowMapper;
import com.prolog.eis.dao.wms.InboundTaskMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.led.LedShow;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
@Transactional(rollbackFor = Exception.class)
public class EndMethod implements AgvMethod {

    @Autowired
    private ContainerTaskService containerTaskService;

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    @Autowired
    private ContainerTaskDetailMapper containerTaskDetailMapper;
    @Autowired
    private CheckOutTaskMapper checkOutTaskMapper;

    @Autowired
    private EisCallbackService eisCallbackService;

    @Autowired
    private QcInBoundTaskService qcInBoundTaskService;
    @Autowired
    private InboundTaskMapper inboundTaskMapper;
    @Autowired
    private LedShowMapper ledShowMapper;

    @Override
    public void doAction(String taskCode, String method) {
        //根据任务号 查询 托盘任务
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
        if (StringUtils.isEmpty(containerTasks)) return;
        if (containerTasks!=null&&containerTasks.size()==0) return;
        ContainerTask containerTask = containerTasks.get(0);

        AgvStorageLocation currentPosition = agvStorageLocationMapper.findByRcs(containerTask.getSource());

        AgvStorageLocation targetPosition = agvStorageLocationMapper.findByRcs(containerTask.getTarget());
        //小车任务结束
        containerTask.setTaskState(1);//设置托盘到位
        containerTask.setEndTime(new Date());//设置小车结束时间
        containerTask.setSource(containerTask.getTarget());//修改当前位置
        containerTask.setSourceType(containerTask.getTargetType());//修改当前位置区域
        containerTask.setTarget("");//设置目的位置
        containerTaskService.update(containerTask);
        //判断托盘到位 区域 agv
        if (containerTask.getTargetType() == 1) {

            //String station = agvStorageLocationMapper.queryPickStationByCode(containerTask.getSource());
            Double pQty = containerTaskDetailMapper.queryPickQtyByConcode(containerTask.getContainerCode());
            if (pQty == null) {
                pQty = 0.0;
            }
            double rQty = containerTask.getQty() - pQty;
            if ("057200AB048300".equals(containerTask.getSource())) {
                LedShow ledShow = ledShowMapper.findById(4, LedShow.class);
                if (ledShow != null) {
                    PrologLedController prologLedController = new PrologLedController();
                    try {
                        prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), pQty, containerTask.getLotId(), rQty,"一站");
                    } catch (Exception e) {
                        LogServices.logSys(e);
                    }
                }
            }

            if ("054320AB048300".equals(containerTask.getSource())) {
                LedShow ledShow = ledShowMapper.findById(5, LedShow.class);
                if (ledShow != null) {
                    PrologLedController prologLedController = new PrologLedController();
                    try {
                        prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), pQty, containerTask.getLotId(), rQty,"二站");
                    } catch (Exception e) {
                        LogServices.logSys(e);
                    }
                }
            }

            if ("051440AB047200".equals(containerTask.getSource())) {
                LedShow ledShow = ledShowMapper.findById(6, LedShow.class);
                if (ledShow != null) {
                    PrologLedController prologLedController = new PrologLedController();
                    try {
                        prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), containerTask.getItemName(), pQty, containerTask.getLotId(), rQty,"三站");
                    } catch (Exception e) {
                        LogServices.logSys(e);
                    }
                }
            }

            if("050000AB051200".equals(containerTask.getSource())){
                LedShow ledShow = ledShowMapper.findById(6,LedShow.class);
                if(ledShow != null){
                    PrologLedController prologLedController = new PrologLedController();
                    try {
                        prologLedController.pick(ledShow.getLedIp(),ledShow.getPort(),containerTask.getItemName(),pQty,containerTask.getLotId(),rQty,"四站");
                    }catch (Exception e){
                        LogServices.logSys(e);
                    }
                }
            }


            //任务类型 业务出库
            if (containerTask.getTaskType() == 1) {
                //出库完成 回告
                eisCallbackService.outBoundReport(containerTask);
            }
            //任务类型 移库出库
            if (containerTask.getTaskType() == 2) {
                eisCallbackService.moveBoundReport(containerTask);
            }
            //锁定拣选站
            targetPosition.setLocationLock(1);
            agvStorageLocationMapper.update(targetPosition);
            //删除容器任务
            containerTaskService.delete(containerTask);
            //删除容器明细
            containerTaskDetailMapper.deleteByMap(MapUtils.put("containerCode", containerTask.getContainerCode()).getMap(), ContainerTaskDetail.class);

        }
        //判断托盘到位 区域 输送线
        if (containerTask.getTargetType() == 2) {

            //小车搬运后当前位置在入库输送线口
            //通知输送线运行
            try {
                qcInBoundTaskService.rcsCompleteForward(containerTask.getContainerCode(), targetPosition.getId());
                //更改目标点位状态
                if (containerTask.getTaskType() == 4) { //空托入库
                    //删除容器任务
                    containerTaskService.delete(containerTask);
                    //删除空托入库任务
                    inboundTaskMapper.deleteByMap(MapUtils.put("containerCode", containerTask.getContainerCode()).getMap(), InboundTask.class);
                }

                if ("T010103".equals(targetPosition.getDeviceNo()) || "T020103".equals(targetPosition.getDeviceNo())){
                    targetPosition.setLocationLock(1);
                }else {
                    targetPosition.setTaskLock(0);
                    targetPosition.setLocationLock(0);
                }
                agvStorageLocationMapper.update(targetPosition);
            } catch (Exception e) {
                LogServices.logSys(e);
            }
        }
    }
}
