package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
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

@Service(CallBackStatus.MCSCallBack_STATUS+2+CallBackStatus.TYPE+3)
@Transactional(rollbackFor = Exception.class,propagation= Propagation.SUPPORTS)
public class MCSCallBackYiKu implements MCSCallBack {
    @Autowired
    private SxStoreMapper sxStoreMapper;
    @Autowired
    private SxStoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
    @Autowired
    private SxStoreTaskFinishService sxStoreTaskFinishService;



    @Autowired
    CallBackService callBackService;

    @Override
    public void container(String containerCode, int targetLayer, int targetX, int targetY, String address) throws Exception {

        //检查是否存在点位
        SxStoreLocation sxStoreLocation = callBackService.getStoreLocation(targetLayer,targetX,targetY);

        if(null != sxStoreLocation) {
            //更新库存
            List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerCode).getMap(),
                    SxStore.class);
            if (sxStores.size() == 1) {
                // 修改库存状态为已上架
                //EIS移位完成
                SxStore sxStore = sxStores.get(0);

                if(null!= sxStore.getSourceLocationId()) {
                    //sxStoreTaskFinishService.moveTaskFinish(sxStore.getSourceLocationId());
                    SxStoreLocation sourceStoreLocation = sxStoreLocationMapper.findById(sxStore.getSourceLocationId(), SxStoreLocation.class);
                    //解锁原货位组升位锁
                    sxStoreLocationGroupMapper.updateMapById(sourceStoreLocation.getStoreLocationGroupId(), MapUtils.put("ascentLockState", 0).getMap(),
                            SxStoreLocationGroup.class);
                }
                sxStoreMapper.updateContainerGround(containerCode);
                SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
                        .findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
                int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
                sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
                        MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);
                sxStoreTaskFinishService.computeLocation(sxStore);
            }else {
                LogServices.logSysBusiness(String.format("托盘%s库存存在多个",containerCode));
                throw new Exception(String.format("托盘%s库存存在多个",containerCode));
            }
        }else {
            LogServices.logSysBusiness("McsInterfaceCallbackError"+ String.format("点位%s不是托盘库货位", address));
        }
    }
}
