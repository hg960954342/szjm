package com.prolog.eis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.*;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.NameAndSimplePropertyPreFilter;
import com.prolog.eis.util.PrologApiJsonHelper;
import org.apache.commons.lang.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.util.*;

@Service
public class EisCallbackServiceImpl implements EisCallbackService {

    @Autowired
    private InBoundTaskService inBoundTaskService;

    @Autowired
    private ContainerTaskService containerTaskService;

    @Autowired
    private ContainerTaskDetailService containerTaskDetailService;

    @Autowired
    private RepeatReportService repeatReportService;

    @Autowired
    private WmsLoginService wmsLoginService;

    /**
     * 回告wms
     *
     * @param containerCode
     * @throws Exception
     */
    @Override
    public void inBoundReport(String containerCode) throws Exception {
       /* wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;*/

        String url = "http://127.0.0.1:8095/api/v1/eis/eisInterface/inBoundReport";
        List<InboundTask> inboundTasks = inBoundTaskService.selectByContainerCode(containerCode);
        if(inboundTasks != null && inboundTasks.size()>0) {
            InboundTask inboundTask = inboundTasks.get(0);

            //判断是否需要回告wms
            if (inboundTask.getReBack()==1){
                //封装入库回告数据
                String json = this.inBoundReportData(inboundTask);
                String msg = "EIS->WMS [WMSInterface] 入库回告请求JSON：[message]:" + json;
                FileLogHelper.WriteLog("WMSRequest", msg);
                String restJson = HttpUtils.post(url, json);
                String resultMsg = "EIS->WMS [WMSInterface] 入库回告返回JSON：[message]:" + restJson;
                FileLogHelper.WriteLog("WMSRequest", resultMsg);

                //判断回告结果
                this.getWmsResponseData(json, restJson, url, inboundTask.getTaskType());
            }

            inboundTask.setTaskState(4);
            inboundTask.setEndTime(new Date());
            inBoundTaskService.update(inboundTask);
        }

    }

    /**
     * eis 业务出库 回告 wms
     *
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void outBoundReport(ContainerTask containerTask) throws Exception {
       /* wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;*/

        String url = "http://127.0.0.1:8095/api/v1/eis/eisInterface/outBoundReport";
        String json = this.outBoundReportData(containerTask);
        String msg = "EIS->WMS [WMSInterface] 出库回告请求JSON：[message]:" + json;
        FileLogHelper.WriteLog("WMSRequest", msg);
        String restJson = HttpUtils.post(url, json);
        String resultMsg = "EIS->WMS [WMSInterface] 出库回告返回JSON：[message]:" + restJson;
        FileLogHelper.WriteLog("WMSRequest", resultMsg);
        //判断回告结果
        this.getWmsResponseData(json, restJson, url, containerTask.getTaskType());
    }

    /**
     * eis 移库出库 回告 wms
     *
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void moveBoundReport(ContainerTask containerTask) throws Exception {
       /* wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;*/

        String url = "http://127.0.0.1:8095/api/v1/eis/eisInterface/moveBoundReport";
        String json = this.moveBoundReportData(containerTask);
        String msg = "EIS->WMS [WMSInterface] 移库回告请求JSON：[message]:" + json;
        FileLogHelper.WriteLog("WMSRequest", msg);
        String restJson = HttpUtils.post(url, json);
        String resultMsg = "EIS->WMS [WMSInterface] 移库回告返回JSON：[message]:" + restJson;
        FileLogHelper.WriteLog("WMSRequest", resultMsg);
        this.getWmsResponseData(json, restJson, url, containerTask.getTaskType());
    }

    /**
     * eis 盘点出库 回告 wms
     *
     * @param billNo 单据号
     * @throws Exception
     */
    @Override
    public void checkBoundReport(String billNo) throws Exception {
        /*wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;*/

        String url = "http://127.0.0.1:8095/api/v1/eis/eisInterface/checkBoundReport";
        String json = this.checkBoundReportData(billNo);
        String msg = "EIS->WMS [WMSInterface] 盘点回告请求JSON：[message]:" + json;
        FileLogHelper.WriteLog("WMSRequest", msg);

        String restJson = HttpUtils.post(url, json);

        String resultMsg = "EIS->WMS [WMSInterface] 盘点回告返回JSON：[message]:" + restJson;
        FileLogHelper.WriteLog("WMSRequest", resultMsg);
        this.getWmsResponseData(json, restJson, url,3);
    }

    /**
     * 重发 回告
     *
     * @param repeatReport
     */
    @Override
    public void recall(RepeatReport repeatReport) throws IOException {
    /*    wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;*/

        String url = repeatReport.getReportUrl();
        String json = repeatReport.getReportData();

        String restJson = HttpUtils.post(url, json);
        repeatReport.setSendTime(new Date());
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);

        if (helper.getString("code").equals("0")) {
            repeatReport.setReportCount(repeatReport.getReportCount() + 1);
            repeatReport.setReportState(2);
            repeatReport.setEndTime(new Date());
        } else {
            repeatReport.setReportCount(repeatReport.getReportCount() + 1);
            repeatReport.setEndTime(new Date());
            repeatReport.setMessage(helper.getString("message"));
            if (repeatReport.getReportCount() > 10) {
                repeatReport.setReportState(3);
            }
        }
        repeatReportService.update(repeatReport);
    }


    /**
     * 订单入库回告数据封装
     *
     * @param inboundTask
     * @return
     * @throws Exception
     */
    private String inBoundReportData(InboundTask inboundTask) throws Exception {

        String[] str={"id","wmsPush","reBack","emptyContainer","ceng","agvLoc","lotId","taskState","qty","createTime","startTime","rukuTime","endTime"};
        NameAndSimplePropertyPreFilter nameAndSimplePropertyPreFilter = new NameAndSimplePropertyPreFilter();
        nameAndSimplePropertyPreFilter.getExcludes().addAll(Arrays.asList(str));

        Map map1 = JSON.parseObject(JSONObject.toJSONString(inboundTask), Map.class);
        List<Map<String,Object>> data = new ArrayList<>();
        data.add(map1);
        Map<String,Object> map = new HashMap<>();
        map.put("data",data);
        map.put("size",data.size());
        map.put("messageID",UUID.randomUUID().toString().replaceAll("-",""));
       return JSONObject.toJSONString(map,nameAndSimplePropertyPreFilter);

    }


    /**
     * 订单出库回告数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String outBoundReportData(ContainerTask containerTask) throws Exception {
        List<Map<String, Object>> reportData = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
        String[] str={"id","lotId","create_time","end_time"};
        NameAndSimplePropertyPreFilter nameAndSimplePropertyPreFilter=new NameAndSimplePropertyPreFilter();
        nameAndSimplePropertyPreFilter.getExcludes().addAll(Arrays.asList(str));
        Map<String,Object> map = new HashMap<>();
        map.put("data",reportData);
        map.put("size",reportData.size());
        map.put("messageID",UUID.randomUUID().toString().replaceAll("-",""));
        return JSONObject.toJSONString(map,nameAndSimplePropertyPreFilter);

    }


    /**
     * 移库出库数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String moveBoundReportData(ContainerTask containerTask) throws Exception {
        List<Map<String, Object>> reportData = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
        String[] str={"id","ownerId","create_time","end_time"};
        NameAndSimplePropertyPreFilter nameAndSimplePropertyPreFilter=new NameAndSimplePropertyPreFilter();
        nameAndSimplePropertyPreFilter.getExcludes().addAll(Arrays.asList(str));
        Map<String,Object> map = new HashMap<>();
        map.put("data",reportData);
        map.put("size",reportData.size());
        map.put("messageID",UUID.randomUUID().toString().replaceAll("-",""));
        return JSONObject.toJSONString(map,nameAndSimplePropertyPreFilter);

    }

    /**
     * 盘点出库数据封装
     *
     * @param billNo
     * @return
     * @throws Exception
     */
    private String checkBoundReportData(String billNo) throws Exception {
        /**
         * 封装
         * {
         * data:[{
         *      billno---单据号
         *      tasktype---任务类型
         *      details：（明细）
         *          [{
         *              seqno---行号
         *              itemid---商品id
         *              lotid---批号
         *              containercode---托盘
         *              qty---称重数量
         *          }]
         *  }],
         *  size:
         *  messageID:
         *  }
         */

        //TODO Auto-generated method stub

       /* EisReportDto eisReportDto = new EisReportDto();
        List<EisReport> datas= new ArrayList<>();
        //按订单封装
        EisReport data = new EisReport();
        //封装明细
        List<ReportDateil> dateils = new ArrayList<>();

        data.setBillNo(billNo);//单据号
        List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByBillNo(billNo);
        for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {

            List<ContainerTask> containerTasks = containerTaskService.selectByContainerCode(containerTaskDetail.getContainerCode());

            ReportDateil dateil = new ReportDateil();

            for (ContainerTask task : containerTasks) {
                data.setTaskType(task.getTaskType()+"");//类型
                dateil.setSeqNo(containerTaskDetail.getSeqNo());//行号
                dateil.setItemId(task.getItemId());//商品id
                dateil.setLotId(task.getLotId());//批号
                dateil.setContainerCode(task.getContainerCode());//托盘
                dateil.setQty(task.getQty());//称重
                dateils.add(dateil);
            }
        }
        data.setDateils(dateils);
        datas.add(data);
        eisReportDto.setData(datas);
        eisReportDto.setSize(datas.size());
        eisReportDto.setMessageID(UUID.randomUUID().toString().replaceAll("-",""));
        return JSONObject.toJSONString(eisReportDto,new NameAndSimplePropertyPreFilter());*/

        List<ResultContainer.DataBean> list=containerTaskDetailService.getCheckReportData(billNo);
        ResultContainer container=new ResultContainer();
        container.setData(list);
        container.setSize(list.size());
        container.setMessageID(UUID.randomUUID().toString().replaceAll("-",""));
        return JSON.toJSONString(container,new NameAndSimplePropertyPreFilter(), SerializerFeature.DisableCircularReferenceDetect);




    }


    /**
     * 判断回告wms 结果
     *
     * @param restJson
     */
    private void getWmsResponseData(String json, String restJson, String url, int type) {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
        String code = helper.getString("code");
        if (!code.equals("0")) {//回告失败
            //添加到重复回告表中
            //封装数据
            RepeatReport repeatReport = new RepeatReport();
            repeatReport.setReportData(json);
            repeatReport.setReportType(type);
            repeatReport.setReportUrl(url);
            repeatReport.setMessage(helper.getString("message"));
            repeatReport.setReportCount(1);
            repeatReport.setReportState(0);
            repeatReport.setCreateTime(new Date());

            repeatReportService.insert(repeatReport);

        }
    }
}
