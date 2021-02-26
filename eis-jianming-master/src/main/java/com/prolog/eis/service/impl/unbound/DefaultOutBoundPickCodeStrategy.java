package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 订单出库必须继承此类 名称必须符合 OutBoundType.TASK_TYPE+数据库中配置的类型数字
 *  * IF_PICKCODE_EXISTS 是否指定拣选站 1指定 0不指定
 */
@Component(OutBoundType.TASK_TYPE+1)
@Primary
public class DefaultOutBoundPickCodeStrategy implements UnBoundStragtegy {

    @Autowired
    AgvStorageLocationMapper agvStorageLocationMapper;
    @Autowired
    PickStationMapper pickStationMapper;

    @Autowired
    ContainerTaskMapper containerTaskMapper;


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;

    @Autowired
    ContainerTaskDetailMapper containerTaskDetailMapperMapper;



    @Autowired
    QcSxStoreMapper qcSxStoreMapper;

    @Autowired
    PortInfoMapper portInfoMapper;


    @Autowired
   private  Map<String, SimilarityDataEntityLoadInterface> similarityDataEntityListLoadMap  ;



    @Autowired
    private  Map<String, DefaultOutBoundPickCodeStrategy> strategyMap  ;

    public SimilarityDataEntityLoadInterface getsimilarityDataEntityListLoad(OutboundTask OutBoundTask){
        int isPickCode=OutBoundTask.getSfReq();
        return similarityDataEntityListLoadMap.get(OutBoundType.IF_SF_REQ +isPickCode);
    }

    public DefaultOutBoundPickCodeStrategy getDefaultOutBoundPickCodeStrategy(OutboundTask OutBoundTask){
        int isPickCode=OutBoundTask.getSfReq();
        return strategyMap.get(OutBoundType.TASK_TYPE+OutBoundTask.getTaskType()+OutBoundType.IF_SF_REQ +isPickCode);
    }

    @Override
     public synchronized void unbound(OutboundTask outboundTask) {
        DefaultOutBoundPickCodeStrategy defaultOutBoundPickCodeStrategy=this.getDefaultOutBoundPickCodeStrategy(outboundTask);
        SimilarityDataEntityLoadInterface similarityDataEntityLoadStrategy=getsimilarityDataEntityListLoad(outboundTask);
        if(null!=defaultOutBoundPickCodeStrategy&&similarityDataEntityLoadStrategy!=null){
            similarityDataEntityLoadStrategy.addOutboundTask(outboundTask);
             if(similarityDataEntityLoadStrategy.getCrrentBillNoList().size()!=0
                    &&similarityDataEntityLoadStrategy.getCrrentBillNoList().size()==similarityDataEntityLoadStrategy.getMaxSize())
             {defaultOutBoundPickCodeStrategy.unbound(outboundTask);
             }
             else if(similarityDataEntityLoadStrategy.getCrrentBillNoList().size()!=0
              &&similarityDataEntityLoadStrategy.getCrrentBillNoList().size()>similarityDataEntityLoadStrategy.getMaxSize()){
                 similarityDataEntityLoadStrategy.getCrrentBillNoList().clear();
             }else{}
        }

    }


    public void removeCompleteOrderAndUpdate(List<String> listBillNos,SimilarityDataEntityLoadInterface similarityDataEntityLoadInterface){
       HashSet<String> setList=new HashSet<String>();
       setList.addAll(listBillNos);
        for(String billNoString:setList) {
            boolean isComplete= outBoundTaskDetailMapper.isOrderComplete(billNoString);
            if(isComplete){
                billNoString=  String.format("'%s'", billNoString);
                outBoundTaskMapper.updateOutBoundTaskBySQL(billNoString);
                similarityDataEntityLoadInterface.getCrrentBillNoList().remove(billNoString);
            }
        }

    }



















}
