package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallBackService {

    @Autowired
    SxStoreLocationMapper sxStoreLocationMapper;
    @Autowired
    SxStoreMapper sxStoreMapper;
    @Autowired
    SxStoreTaskFinishService sxStoreTaskFinishService;
    @Autowired
    SxStoreLocationGroupMapper sxStoreLocationGroupMapper;

    //判断有无库存

    public SxStore rukuSxStore(String containerNo) throws Exception {
        List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
                SxStore.class);
        if (sxStores.size() == 1) {
            SxStore sxStore = sxStores.get(0);
            if(sxStore.getStoreState() != 10) {
                LogServices.logSysBusiness("McsInterfaceCallbackError"+ String.format("托盘%s入库库存状态异常%s", containerNo,String.valueOf(sxStore.getStoreState())));
            }
            //修改库存为已上架
            sxStore.setStoreState(20);
            sxStoreMapper.update(sxStore);

            Integer storeLocationId = sxStore.getStoreLocationId();
            SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
            // 根据出库任务类型转换
            sxStoreLocationMapper.updateMapById(storeLocationId, MapUtils.put("actualWeight", 0).getMap(),
                    SxStoreLocation.class);
            sxStoreTaskFinishService.computeLocation(sxStore);
            sxStoreLocationGroupMapper.updateMapById(cksxStoreLocation.getStoreLocationGroupId(),
                    MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);

            return sxStore;
        }else {
            return null;
        }
    }


    public  SxStoreLocation getStoreLocation(int layer,int x,int y) {
        List<SxStoreLocation> list = sxStoreLocationMapper.findByMap(MapUtils.put("layer", layer).put("x", x).put("y", y).getMap(), SxStoreLocation.class);
        if(list.isEmpty()) {
            return null;
        }else if(list.size() > 1) {
            LogServices.logSysBusiness("查询出多个货位");
            return null;
        }else {
            return list.get(0);
        }
    }

}
