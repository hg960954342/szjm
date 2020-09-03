package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.dao.wms.InboundTaskMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.CallBackCheckOutService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.store.CallBackStatus;
import com.prolog.eis.service.store.MCSCallBack;
import com.prolog.eis.service.store.QcInBoundTaskService;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(CallBackStatus.MCSCallBack_STATUS+2+CallBackStatus.TYPE+1)
@Transactional(rollbackFor = Exception.class,propagation= Propagation.SUPPORTS)
@SuppressWarnings("all")
public class MCSCallBackRuKu implements MCSCallBack {

    @Autowired
    InboundTaskMapper inboundTaskMapper;
    @Autowired
    CallBackCheckOutService callBackCheckOutService;

    @Autowired
    SxStoreLocationMapper sxStoreLocationMapper;
    @Autowired
    SxStoreTaskFinishService sxStoreTaskFinishService;

    @Autowired
    EisCallbackService eisCallbackService;
    @Autowired
    SxStoreMapper sxStoreMapper;
    @Autowired
    SxStoreLocationGroupMapper sxStoreLocationGroupMapper;

    @Autowired
    private QcInBoundTaskService qcInBoundTaskService;



    @Override
    public void container(String containerCode, int targetLayer, int targetX, int targetY, String address) throws Exception{
        //检查到位的托盘
        SxStoreLocation sxStoreLocation =qcInBoundTaskService.getStoreLocation(targetLayer,targetX,targetY);
        if(null != sxStoreLocation) {
            //检查有无入库库存
            List<InboundTask> inboundTasks = inboundTaskMapper.findByMap(MapUtils.put("containerCode", containerCode).getMap(), InboundTask.class);
            if(inboundTasks.isEmpty()){
                LogServices.logSysBusiness(String.format("托盘%s无入库任务", containerCode));
                return;
            }

            //修改库存 货位组相关属性
            qcInBoundTaskService.rukuSxStore(containerCode);
            //调用回告入库的方法
            eisCallbackService.inBoundReport(containerCode);
            //更新盘点任务状态
            callBackCheckOutService.updateCallBackCheckOut(containerCode);

        }else {
            LogServices.logSysBusiness(String.format("点位%s不是托盘库货位", address));


            return;
        }
    }



}
