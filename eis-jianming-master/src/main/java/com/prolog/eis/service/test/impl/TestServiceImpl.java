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
import com.prolog.framework.utils.MapUtils;
import org.apache.ibatis.annotations.Param;
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
        List<Map<String, Object>> list = logMapper.getPager("CONCAT(id,'') id,interface_address,params,result,DATE_FORMAT(create_time, \"%Y-%m-%d %H:%i:%S\") create_time", "mcs_log", "and interface_address like '%Request'", "order by create_time desc", start, end);

        list.parallelStream().forEach(x -> {
            String JSON = (String) x.get("params");
            String typeOld = (String) x.get("type") == null ? "" : (String) x.get("type");
            String containerNoOld = (String) x.get("containerNo") == null ? "" : (String) x.get("containerNo");
            EisParams params = JSONObject.parseObject(JSON, EisParams.class);
            List<EisParams.CarryListBean> listEisParams = params.getCarryList();
            listEisParams.parallelStream().forEach(y -> {
                int Type = y.getType();
                if (Type == 1) {
                    x.put("type", String.format("%s 入库", typeOld));
                } else if (Type == 2) {
                    x.put("type", String.format("%s 出库", typeOld));
                } else if (Type == 3) {
                    x.put("type", String.format("%s 同层移位", typeOld));
                } else if (Type == 4) {
                    x.put("type", String.format("%s 输送线前进", typeOld));
                } else {
                    x.put("type", String.format("%s 未知状态", typeOld));
                }
                String containerNo = y.getContainerNo();
                x.put("containerNo", String.format("%s %s", containerNoOld, containerNo));
            });


        });
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
        List<Map<String, Object>> list = rcsLogMapper.getPager("CONCAT(id,'') id,interface_address,params,result,DATE_FORMAT(create_time, \"%Y-%m-%d %H:%i:%S\") create_time", "rcs_log", "and interface_address like '%genAgvSchedulingTask'", "order by create_time desc", start, end);
        list.parallelStream().forEach(x -> {
            String params = (String) x.get("params");
            RcsParams rcsParams = JSONObject.parseObject(params, RcsParams.class);
            List<RcsParams.PositionCodePathBean> listRCS = rcsParams.getPositionCodePath();
            String startPoint = "位置异常";
            String endPoint = "位置异常";
            if (listRCS.size() == 2) {
                startPoint = listRCS.get(0).getPositionCode();
                endPoint = listRCS.get(1).getPositionCode();
            }
            x.put("start", startPoint);
            x.put("end", endPoint);
        });
        return MapUtils.put("totalRecords", coutLog).put("curPage", pq_curpage).put("data", list).getMap();
    }

}

class RcsParams {

    /**
     * positionCodePath : [{"positionCode":"057200AB054000","type":"00"},{"positionCode":"054320AB048300","type":"00"}]
     * tokenCode :
     * podDir :
     * agvCode :
     * data :
     * reqTime : 2020 09 23 14:09:37
     * priority : 1
     * wbCode :
     * reqCode : 42368bf18c714ead806c5b6a197ad73d
     * taskCode : 42368bf18c714ead806c5b6a197ad73d
     * clientCode :
     * podCode :
     * taskTyp : F01
     * interfaceName : genAgvSchedulingTask
     */

    private String tokenCode;
    private String podDir;
    private String agvCode;
    private String data;
    private String reqTime;
    private String priority;
    private String wbCode;
    private String reqCode;
    private String taskCode;
    private String clientCode;
    private String podCode;
    private String taskTyp;
    private String interfaceName;
    private List<PositionCodePathBean> positionCodePath;

    public String getTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    public String getPodDir() {
        return podDir;
    }

    public void setPodDir(String podDir) {
        this.podDir = podDir;
    }

    public String getAgvCode() {
        return agvCode;
    }

    public void setAgvCode(String agvCode) {
        this.agvCode = agvCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWbCode() {
        return wbCode;
    }

    public void setWbCode(String wbCode) {
        this.wbCode = wbCode;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getTaskTyp() {
        return taskTyp;
    }

    public void setTaskTyp(String taskTyp) {
        this.taskTyp = taskTyp;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<PositionCodePathBean> getPositionCodePath() {
        return positionCodePath;
    }

    public void setPositionCodePath(List<PositionCodePathBean> positionCodePath) {
        this.positionCodePath = positionCodePath;
    }

    public static class PositionCodePathBean {
        /**
         * positionCode : 057200AB054000
         * type : 00
         */

        private String positionCode;
        private String type;

        public String getPositionCode() {
            return positionCode;
        }

        public void setPositionCode(String positionCode) {
            this.positionCode = positionCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

class EisParams {

    private List<CarryListBean> carryList;

    public List<CarryListBean> getCarryList() {
        return carryList;
    }

    public void setCarryList(List<CarryListBean> carryList) {
        this.carryList = carryList;
    }

    public static class CarryListBean {
        /**
         * taskId : 71c91cfb5c44462795c0b874ecf88d38
         * type : 2
         * bankId : 0
         * containerNo : 802990
         * address : 0100230022
         * target : 0200210014
         * weight : 0
         * priority : 99
         * status : 0
         */

        private String taskId;
        private int type;
        private int bankId;
        private String containerNo;
        private String address;
        private String target;
        private String weight;
        private String priority;
        private int status;


        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getBankId() {
            return bankId;
        }

        public void setBankId(int bankId) {
            this.bankId = bankId;
        }

        public String getContainerNo() {
            return containerNo;
        }

        public void setContainerNo(String containerNo) {
            this.containerNo = containerNo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}