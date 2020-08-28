package com.prolog.eis.service.rcs.impl;

import com.prolog.eis.controller.led.PrologLedController;
import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.CheckOutTaskMapper;
import com.prolog.eis.dao.ContainerTaskDetailMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dao.led.LedShowMapper;
import com.prolog.eis.dao.wms.InboundTaskMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.led.LedShow;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.enums.OutBoundEnum;
import com.prolog.eis.service.impl.unbound.OutBoundType;
import com.prolog.eis.service.impl.unbound.entity.CheckOutTask;
import com.prolog.eis.service.rcs.AgvCallbackService;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AgvCallbackServiceImpl implements AgvCallbackService {

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
    @Transactional(rollbackFor = Exception.class)
    public void agvCallback(String taskCode, String method) {

        //agv 回告 method ：star : 任务开始、outbin : 走出储位、end : 任务结束


        //根据任务号 查询 托盘任务
        List<ContainerTask> containerTasks = containerTaskService.selectByTaskCode(taskCode);
        if (StringUtils.isEmpty(containerTasks)) return;
        if (containerTasks!=null&&containerTasks.size()==0) return;
        ContainerTask containerTask = containerTasks.get(0);

        AgvStorageLocation currentPosition = agvStorageLocationMapper.findByRcs(containerTask.getSource());

        AgvStorageLocation targetPosition = agvStorageLocationMapper.findByRcs(containerTask.getTarget());
        //判断小车状态
        if ("star".equals(method)) {
            //小车任务开始
            containerTask.setTaskState(3);//设置下游设备回告开始
            containerTask.setStartTime(new Date());
            containerTaskService.update(containerTask);

        }
        if ("outbin".equals(method)) {
            //小车离开原存储位
            containerTask.setTaskState(4);//设置下游设备离开原存储位
            containerTask.setMoveTime(new Date());
            containerTaskService.update(containerTask);
            //更新点位状态
            currentPosition.setTaskLock(0);
            currentPosition.setLocationLock(0);
            agvStorageLocationMapper.update(currentPosition);


        }
        if ("end".equals(method)) {
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
                            prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), "根板蓝", pQty, containerTask.getLotId(), rQty,"一站");
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
                            prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), "蓝根板", pQty, containerTask.getLotId(), rQty,"二站");
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
                            prologLedController.pick(ledShow.getLedIp(), ledShow.getPort(), "板根蓝", pQty, containerTask.getLotId(), rQty,"三站");
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
                            prologLedController.pick(ledShow.getLedIp(),ledShow.getPort(),"板根蓝",pQty,containerTask.getLotId(),rQty,"四站");
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
                    if (containerTask.getTaskType() ==3) { //盘点出库

                        //更新入库口的状态
                        Criteria criteria=Criteria.forClass(CheckOutTask.class);
                        criteria.setRestriction(Restrictions.eq("containerCode",containerTask.getContainerCode()));
                        List<CheckOutTask> listCheckOut=checkOutTaskMapper.findByCriteria(criteria);
                        if(listCheckOut.size()>1) {LogServices.logSysBusiness("一个托盘同时被两个盘点任务盘点！"); return;}
                        CheckOutTask checkOutTask=listCheckOut.get(0);
                        checkOutTask.setState("2");
                        checkOutTaskMapper.update(checkOutTask);
                        //转换容器任务
                        String target =containerTask.getTarget();
                        Coordinate coordinate=PrologCoordinateUtils.analysis(target);
                        String agvTarget=PrologLocationUtils.splicingXYStr(coordinate.getLayer(), coordinate.getX(), coordinate.getY());
                        containerTask.setSource(agvTarget);
                        containerTaskService.update(containerTask);
                     }
                    targetPosition.setTaskLock(0);
                    targetPosition.setLocationLock(0);
                    agvStorageLocationMapper.update(targetPosition);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}



