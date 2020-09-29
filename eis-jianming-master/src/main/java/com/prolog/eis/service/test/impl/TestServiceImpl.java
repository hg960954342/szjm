package com.prolog.eis.service.test.impl;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.dao.RcsLogMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.service.test.TestService;
import com.prolog.eis.service.test.ViewMCSResultHandler;
import com.prolog.eis.service.test.ViewRCSResultHandler;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private SxStoreMapper sxStoreMapper;
    @Autowired
    private SxStoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private SxStoreTaskFinishService sxStoreTaskFinishService;
    @Autowired
    private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;

    @Resource
    com.prolog.eis.dao.LogMapper logMapper;
    @Resource
    RcsLogMapper rcsLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStore(String containerNo) throws Exception {

        clearSxStore(containerNo);
    }

    //判断有无库存
    private SxStore clearSxStore(String containerNo) throws Exception {
        List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
                SxStore.class);
        if (sxStores.size() == 1) {
            SxStore sxStore = sxStores.get(0);
            Integer storeLocationId = sxStore.getStoreLocationId();
            SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
            // 根据出库任务类型转换
            sxStoreMapper.deleteByContainer(containerNo);
            sxStoreLocationMapper.updateMapById(storeLocationId, MapUtils.put("actualWeight", 0).getMap(),
                    SxStoreLocation.class);
            sxStoreTaskFinishService.computeLocation(sxStore);
            sxStoreLocationGroupMapper.updateMapById(cksxStoreLocation.getStoreLocationGroupId(),
                    MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);

            return sxStore;
        } else {
            return null;
        }
    }

    @Override
    public Integer updateIsLockByLayer(int isLock, int layer) {
        return sxStoreLocationGroupMapper.updateIsLockByLayer(isLock, layer);
    }

    @Override
    public List<SxStoreViewDto> getSxStoreViewDto(int layer) {
        return sxStoreMapper.getSxStoreViewDtoByLayer(layer);
    }

    @Override
    public List<SxStoreViewSimpleDto> getSxStoreViewDtoSimpleDto(int layer) {
        return sxStoreMapper.getSxStoreViewDtoSimpleByLayer(layer);
    }


    @Override
    public SxStoreViewMapDto getSxStoreViewMapDtoByLayer(int layer) {

        return sxStoreMapper.getSxStoreViewMapDtoByLayer(layer);
    }

    @Override
    public String getSxStoreContainerNo(int layer, int x, int y) {

        return sxStoreMapper.getSxStoreContainerNo(layer, x, y);
    }

    @Override
    public List<String> getSxStoreList(String itemName, String itemValue) {
        return sxStoreMapper.listSxStore(itemName, itemValue);
    }

    @Override
   public Object listSxStoreQuery(String itemId, String lotId, String ownerId,String itemName,String lot, Integer pqCurpage, Integer pqRpp){
        Long count = sxStoreMapper.countSxStoreQuery(itemId, lotId, ownerId,itemName,lot);
        int start = (pqRpp * (pqCurpage - 1));
        if (start >= count) {
            pqCurpage = (int) Math.ceil(((double) count) / pqRpp);
            start = (pqRpp * (pqCurpage - 1));
        }
        if (start < 0) {
            start = 0;
        }

        List<Map<String, Object>> list = sxStoreMapper.listSxStoreQuery(itemId, lotId, ownerId,itemName,lot, start, pqRpp);
        return MapUtils.put("totalRecords", count).put("curPage", pqCurpage).put("data", list).getMap();
    }


    @Override
    public Object getLogViewMCSData(int pq_curpage, int pq_rpp) {

        Long countMcs = logMapper.coutMcsLog();

        int start = (pq_rpp * (pq_curpage - 1));
        if (start >= countMcs) {
            pq_curpage = (int) Math.ceil(((double) countMcs) / pq_rpp);
            start = (pq_rpp * (pq_curpage - 1));
        }
        if (start < 0) {
            start = 0;
        }
        int end = pq_rpp;
        ViewMCSResultHandler handler=new ViewMCSResultHandler();
        logMapper.getPager("CONCAT(id,'') id,interface_address,params,result,DATE_FORMAT(create_time, \"%Y-%m-%d %H:%i:%S\") create_time", "mcs_log", "and interface_address like '%Request'", "order by create_time desc", start, end,handler);
        List<Map<String, Object>> list =handler.getList();

        return MapUtils.put("totalRecords", countMcs).put("curPage", pq_curpage).put("data", list).getMap();


    }

    @Override
    public Object getLogViewRCSData(int pq_curpage, int pq_rpp) {
        Long coutLog = rcsLogMapper.coutLog();
        int start = (pq_rpp * (pq_curpage - 1));
        if (start >= coutLog) {
            pq_curpage = (int) Math.ceil(((double) coutLog) / pq_rpp);
            start = (pq_rpp * (pq_curpage - 1));
        }
        if (start < 0) {
            start = 0;
        }
        int end = pq_rpp;
        ViewRCSResultHandler handler=new ViewRCSResultHandler();
        rcsLogMapper.getPager("CONCAT(id,'') id,interface_address,params,result,DATE_FORMAT(create_time, \"%Y-%m-%d %H:%i:%S\") create_time", "rcs_log", "and interface_address like '%genAgvSchedulingTask'", "order by create_time desc", start, end,handler);
        List<Map<String, Object>> list =handler.getList();
        return MapUtils.put("totalRecords", coutLog).put("curPage", pq_curpage).put("data", list).getMap();
    }

}



