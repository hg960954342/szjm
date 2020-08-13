package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.OutboundTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(SCOPE_SINGLETON)
public class SimilarityDataEntityListLoad {


    public  Set<String> currentBillNoList=new HashSet<>(); //当前执行的billNoString
    public  int  maxSize=5; //订单池处理最大数量

    private Set<String> billNoPickCodeList=Collections.synchronizedSet(new HashSet<>());
    private Set<String> billNoList=Collections.synchronizedSet(new HashSet<>());


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;
    //订单超时处理时间默认值 半个小时 单位为min
    public static final long overTime=30;






    public int addOutboundTask(OutboundTask outboundTask) {
         if(billNoList.size()<=maxSize&&outboundTask.getSfReq()==0){
             billNoList.add("'"+outboundTask.getBillNo()+"'");
             return billNoList.size();
          }
        if(billNoPickCodeList.size()<=maxSize&&outboundTask.getSfReq()==1){
            billNoPickCodeList.add("'"+outboundTask.getBillNo()+"'");
            return billNoPickCodeList.size();
        }
        return 0;
    }


    public List<DetailDataBean> getOutDetailList(Class <? extends DefaultOutBoundPickCodeStrategy> classz) {
        List<OutboundTask> listOverTimeBoundTask= outBoundTaskMapper.getOutBoudTaskOverTime(overTime);
        for(OutboundTask outboundTask:listOverTimeBoundTask){
            this.addOutboundTask(outboundTask);
        }


        if((null!=classz.getAnnotation(Component.class))&&
                classz.getAnnotation(Component.class).value().indexOf(OutBoundType.IF_SfReq)!=-1) {
            String value=classz.getAnnotation(Component.class).value();
            if(StringUtils.isNotEmpty(value.substring(value.indexOf(OutBoundType.IF_SfReq) + OutBoundType.IF_SfReq.length(),1))){
                int sfreq=Integer.parseInt(value.substring(value.indexOf(OutBoundType.IF_SfReq) + OutBoundType.IF_SfReq.length(),1));
                if(sfreq==0){
                    currentBillNoList=billNoList;
                  return  outBoundTaskDetailMapper.getOuntBoundDetailAll(String.join(",", currentBillNoList));
                }else if(sfreq==1){
                    currentBillNoList=billNoPickCodeList;
                    return  outBoundTaskDetailMapper.getOuntBoundDetailPickCodeAll(String.join(",", currentBillNoList));
                }


            }

        }

        return new ArrayList<>();



    }


}
