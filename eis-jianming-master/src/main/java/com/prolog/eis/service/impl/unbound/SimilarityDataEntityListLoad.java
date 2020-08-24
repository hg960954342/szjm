package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.OutBoundTaskDetailMapper;
import com.prolog.eis.dao.OutBoundTaskMapper;
import com.prolog.eis.model.wms.OutboundTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component(OutBoundType.IF_SfReq+0)
@Scope(SCOPE_SINGLETON)
@SuppressWarnings("all")
public class SimilarityDataEntityListLoad implements SimilarityDataEntityLoadInterface {


    public  Set<String> currentBillNoList=Collections.synchronizedSet(new HashSet<>());//当前执行的billNoString
    public  int  maxSize=1; //订单池处理最大数量

  //  private Set<String> billNoPickCodeList=Collections.synchronizedSet(new HashSet<>());
    private Set<String> billNoList=Collections.synchronizedSet(new HashSet<>());


    @Autowired
    OutBoundTaskMapper outBoundTaskMapper;

    @Autowired
    OutBoundTaskDetailMapper outBoundTaskDetailMapper;
    //订单超时处理时间默认值 半个小时 单位为min
    public static final long overTime=1;


    public Set<String> getCrrentBillNoList(){
        return currentBillNoList;
    }
    public int getMaxSize(){
        return maxSize;
    }



    /**
     * 超时任务优先
     * 1.订单池未满
     * @param outboundTask
     * @return
     */
    public void addOutboundTask(OutboundTask outboundTask) {
         if(billNoList.size()<=maxSize&&outboundTask.getSfReq()==0) {
             //billNoList.addAll(outBoundTaskMapper.getOutBoudTaskBillNoOverTimeStringList(overTime));
             billNoList.add("'"+outboundTask.getBillNo()+"'");
             getCrrentBillNoList().add("'"+outboundTask.getBillNo()+"'");
          }
          /*
        if(billNoPickCodeList.size()<=maxSize&&outboundTask.getSfReq()==1){
            billNoPickCodeList.addAll(outBoundTaskMapper.getOutBoudTaskBillNoOverTime(overTime,outboundTask.getSfReq()));
            billNoPickCodeList.add("'"+outboundTask.getBillNo()+"'");
            currentBillNoList=billNoPickCodeList;
        }
        */

    }


    public List<DetailDataBean> getOutDetailList() {
       /* List<OutboundTask> listOverTimeBoundTask= outBoundTaskMapper.getOutBoudTaskOverTime(overTime);
        for(OutboundTask outboundTask:listOverTimeBoundTask){
            this.addOutboundTask(outboundTask);
        }*/


        return  outBoundTaskDetailMapper.getOuntBoundDetailAll(String.join(",", getCrrentBillNoList()));
        /*
        if((null!=classz.getAnnotation(Component.class))&&
                classz.getAnnotation(Component.class).value().indexOf(OutBoundType.IF_SfReq)!=-1) {
            String value=classz.getAnnotation(Component.class).value();
            value=value.substring(value.indexOf(OutBoundType.IF_SfReq) + OutBoundType.IF_SfReq.length(),value.length());
            if(StringUtils.isNotEmpty(value)){
                int sfreq=Integer.parseInt(value);
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
    */


    }

    /**
     * 获取相识度最高的出库任务
     * @return
     */
    private OutboundTask getSimilarityDataList(){
        List<OutboundTask> outboundTaskList=outBoundTaskMapper.getListOutboundTask();
        List<SimilarityDataEntity> list=new ArrayList<SimilarityDataEntity>();
        float count=outBoundTaskDetailMapper.getPoolItemCount(String.join(",", billNoList));
        for (OutboundTask outboundTask:outboundTaskList) {
            float  countSame= outBoundTaskDetailMapper.getPoolSameItemCount(String.join(",", billNoList),outboundTask.getBillNo());
            float currentCount=outBoundTaskDetailMapper.getPoolItemCount("'"+outboundTask.getBillNo()+"'");
            count=currentCount>=count?currentCount:count;
            float similarity=countSame/count;
            SimilarityDataEntity similarityDataEntity=new SimilarityDataEntity();
            similarityDataEntity.setOutboundTask(outboundTask);
            similarityDataEntity.setSimilarity(similarity);
            list.add(similarityDataEntity);
        }
       return list.stream().max(Comparator.comparing(SimilarityDataEntity::getSimilarity)).orElse(null).getOutboundTask();


    }

}
