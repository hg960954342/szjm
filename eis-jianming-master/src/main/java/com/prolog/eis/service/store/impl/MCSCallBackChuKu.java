package com.prolog.eis.service.store.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.CheckOutTaskMapper;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.dao.wms.InboundTaskMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.impl.inbound.InBoundContainerService;
import com.prolog.eis.service.impl.unbound.entity.CheckOutTask;
import com.prolog.eis.service.store.CallBackStatus;
import com.prolog.eis.service.store.MCSCallBack;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.util.PrologLocationUtils;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.core.restriction.Criteria;
import com.prolog.framework.core.restriction.Restrictions;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service(CallBackStatus.MCS_CALL_BACK_STATUS +2+CallBackStatus.TYPE+2)
@Transactional(rollbackFor = Exception.class,propagation= Propagation.SUPPORTS)
@SuppressWarnings("all")
public class MCSCallBackChuKu implements MCSCallBack {

    @Autowired
    private PortInfoMapper portInfoMapper;
    @Autowired
    private ContainerTaskMapper containerTaskMapper;

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;


    @Autowired
    private CheckOutTaskMapper checkOutTaskMapper;

    @Autowired
    private InBoundContainerService inBoundContainerService;
    @Autowired
    private InboundTaskMapper inboundTaskMapper;
    @Autowired
    private SxStoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private SxStoreTaskFinishService sxStoreTaskFinishService;
    @Autowired
    private SxStoreMapper sxStoreMapper;
    @Autowired
    private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;


    @Override
    public void container(String containerCode, int targetLayer, int targetX, int targetY, String address)throws Exception {

        //????????????
        // ???port???
        List<PortInfo> portInfos = portInfoMapper.findByMap(MapUtils.put("layer", targetLayer).put("x", targetX).put("y", targetY).getMap(), PortInfo.class);
        if(portInfos.isEmpty()) {
            LogServices.logSysBusiness("McsInterfaceCallbackError"+ String.format("??????%s????????????????????????", address));

            return;
        }

        ContainerTask containerTask = containerTaskMapper.selectStartTaskByContainerCode(containerCode);
        if(null == containerTask) {
            //????????????????????????????????????????????????
            LogServices.logSysBusiness("McsInterfaceCallbackError"+ String.format("??????%s?????????????????????", containerCode));
           return;
        }

        PortInfo portInfo = portInfos.get(0);
        if(portInfo.getCallCar() == 1) {
            //??????????????????
            //??????agv????????????
            List<AgvStorageLocation> agvStorageLocations = agvStorageLocationMapper.findByMap(MapUtils.put("locationType", 2).put("deviceNo",portInfo.getJunctionPort()).getMap(), AgvStorageLocation.class);
            if(agvStorageLocations.isEmpty()) {

                LogServices.logSysBusiness("McsInterfaceCallbackError"+ String.format("Agv??????????????????????????????%s", portInfo.getJunctionPort()));
                return;
            }



            //?????????????????????
            SxStore sxStore = this.clearSxStore(containerCode);

            containerTask.setSource(agvStorageLocations.get(0).getRcsPositionCode());
            containerTask.setSourceType(2);
            containerTask.setTaskState(1);
            containerTask.setItemId(sxStore.getItemId());
            containerTask.setLotId(sxStore.getLotId());
            containerTask.setOwnerId(sxStore.getOwnerId());
            containerTask.setQty(sxStore.getQty());
            containerTask.setCreateTime(new Date());
            containerTask.setStartTime(null);
            containerTask.setMoveTime(null);
            containerTask.setEndTime(null);


            if(containerTask.getTaskType()==3){

                //??????????????????
                Criteria criteria=Criteria.forClass(CheckOutTask.class);
                criteria.setRestriction(Restrictions.eq("containerCode",containerTask.getContainerCode()));
                List<CheckOutTask> listCheckOut=checkOutTaskMapper.findByCriteria(criteria);
                if(listCheckOut.size()>1) {LogServices.logSysBusiness("????????????????????????????????????????????????"); return;}
                CheckOutTask checkOutTask=listCheckOut.get(0);
                checkOutTask.setState("2");
                checkOutTaskMapper.update(checkOutTask);
                //?????????????????????????????????
                Coordinate Coordinate= PrologLocationUtils.analysis(agvStorageLocations.get(0).getRcsPositionCode());
                Coordinate.setLayer(4);
                AgvStorageLocation agvStorageLocation=inBoundContainerService.getInBound(Coordinate);
                containerTask.setTarget(agvStorageLocation.getRcsPositionCode());
                containerTask.setTargetType(2);
                containerTask.setTaskState(1);
                //????????????InBoundTask
                InboundTask inboundTask=new InboundTask();
                inboundTask.setTaskState(1);
                inboundTask.setCreateTime(new Date());
                inboundTask.setEmptyContainer(0);
                inboundTask.setReBack(0); //?????????WMS
                inboundTask.setTaskType(2);//????????????
                inboundTask.setBillNo(PrologStringUtils.newGUID());
                inboundTask.setWmsPush(0);
                inboundTask.setContainerCode(containerTask.getContainerCode());
                inboundTask.setItemId(containerTask.getItemId());
                inboundTask.setLotId(containerTask.getLotId());
                inboundTask.setOwnerId(containerTask.getOwnerId());
                inboundTask.setQty(containerTask.getQty());
                if(containerTask.getSourceType()== 2) //Agv??????
                {
                    String sourceAgv=containerTask.getSource();
                    Coordinate CoordinateAgv=PrologLocationUtils.analysis(sourceAgv);
                    inboundTask.setCeng(CoordinateAgv.getLayer()+"");
                }
                inboundTaskMapper.save(inboundTask);

            }



            containerTaskMapper.update(containerTask);
        }else {
            //???agv??????????????????
            //?????????????????????
            this.clearSxStore(containerCode);

            containerTaskMapper.deleteById(containerTask.getId(), ContainerTask.class);
        }
    }


    //??????????????????
    private SxStore clearSxStore(String containerNo) throws Exception {
        List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
                SxStore.class);
        if (sxStores.size() == 1) {
            SxStore sxStore = sxStores.get(0);
            Integer storeLocationId = sxStore.getStoreLocationId();
            SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
            // ??????????????????????????????
            sxStoreMapper.deleteByContainer(containerNo);
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
}
