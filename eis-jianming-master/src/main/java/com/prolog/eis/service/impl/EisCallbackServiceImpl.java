package com.prolog.eis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.ContainerTaskDetailService;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.RepeatReportService;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.HttpUtils;
import com.prolog.eis.util.PrologApiJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class EisCallbackServiceImpl implements EisCallbackService {

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
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void inBoundReport(ContainerTask containerTask) throws Exception {
        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;

        String url = "";

        //封装入库回告数据
        String json = this.inBoundReportData(containerTask);
        String msg = "EIS->WMS [WMSInterface] 入库回告请求JSON：[message]:" + json;
        FileLogHelper.WriteLog("WMSRequest", msg);
        String restJson = HttpUtils.post(url, json);
        String resultMsg = "EIS->WMS [WMSInterface] 入库回告返回JSON：[message]:" + restJson;
        FileLogHelper.WriteLog("WMSRequest", resultMsg);

        //判断回告结果
        this.getWmsResponseData(json, restJson, url, containerTask.getTaskType());
    }

    /**
     * eis 业务出库 回告 wms
     *
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void outBoundReport(ContainerTask containerTask) throws Exception {
        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;

        String url = "";
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
        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;

        String url = "";
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
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void checkBoundReport(ContainerTask containerTask) throws Exception {
        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;

        String url = "";
        String json = this.checkBoundReportData(containerTask);
        String msg = "EIS->WMS [WMSInterface] 盘点回告请求JSON：[message]:" + json;
        FileLogHelper.WriteLog("WMSRequest", msg);

        String restJson = HttpUtils.post(url, json);

        String resultMsg = "EIS->WMS [WMSInterface] 盘点回告返回JSON：[message]:" + restJson;
        FileLogHelper.WriteLog("WMSRequest", resultMsg);
        this.getWmsResponseData(json, restJson, url, containerTask.getTaskType());
    }

    /**
     * 重发 回告
     *
     * @param repeatReport
     */
    @Override
    public void recall(RepeatReport repeatReport) throws IOException {
        wmsLoginService.loginWms();
        String token = LoginWmsResponse.accessToken;

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
    }


    /**
     * 订单入库回告数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String inBoundReportData(ContainerTask containerTask) throws Exception {
        List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
        List<Map<String, Object>> data = new ArrayList<>();
        for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {
            //封装
            Map<String, Object> map = new HashMap<>();
            map.put("billno", containerTaskDetail.getBillNo());
            map.put("containercode", containerTask.getContainerCode());
            map.put("tasktype", containerTask.getTaskType());
            map.put("itemid", containerTask.getItemId());
            map.put("ownerid", containerTask.getOwnerId());


            data.add(map);
        }
        return PrologApiJsonHelper.toJson(data);
    }


    /**
     * 订单出库回告数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String outBoundReportData(ContainerTask containerTask) throws Exception {
        List<Map<String, Object>> data = new ArrayList<>();
        List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
        for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {
            //封装
            Map<String, Object> map = new HashMap<>();
            map.put("containercode", containerTaskDetail.getContainerCode());//容器
            map.put("billno", containerTaskDetail.getBillNo());//单据号
            map.put("tasktype", containerTask.getTaskType());//类型
            map.put("ownerid", containerTaskDetail.getOwnerId());//货主
            map.put("seqno", containerTaskDetail.getSeqNo());//行号
            map.put("itemid", containerTaskDetail.getItemId());//商品id
            map.put("qty", containerTaskDetail.getQty());//分摊数量
            map.put("agvloc", containerTask.getSource());//agv点位

            data.add(map);
        }

        return PrologApiJsonHelper.toJson(data);
    }


    /**
     * 移库出库数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String moveBoundReportData(ContainerTask containerTask) throws Exception {
        List<Map<String, Object>> data = new ArrayList<>();
        List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
        for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {
            //封装
            Map<String, Object> map = new HashMap<>();
            map.put("containercode", containerTaskDetail.getContainerCode());//容器
            map.put("billno", containerTaskDetail.getBillNo());//单据号
            map.put("tasktype", containerTask.getTaskType());//类型
            map.put("seqno", containerTaskDetail.getSeqNo());//行号
            map.put("itemid", containerTaskDetail.getItemId());//商品id
            map.put("lotid", containerTaskDetail.getLotId());//批号
            map.put("qty", containerTaskDetail.getQty());//分摊数量
            map.put("agcloc", containerTask.getSource());//agv点位

            data.add(map);
        }

        return PrologApiJsonHelper.toJson(data);
    }

    /**
     * 盘点出库数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String checkBoundReportData(ContainerTask containerTask) throws Exception {
        //TODO Auto-generated method stub
        List<Map<String,Object>> data = new ArrayList<>();
        List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
        for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {
            //封装
            Map<String,Object> map =new HashMap<>();
            map.put("billno",containerTaskDetail.getBillNo());//单据号
            map.put("tasktype",containerTask.getTaskType());//类型

            //封装明细
            List<Map<String,Object>> details = new ArrayList<>();
            Map<String,Object> detail = new HashMap<>();
            detail.put("seqno",containerTaskDetail.getSeqNo());//行号
            detail.put("itemid",containerTaskDetail.getItemId());//商品id
            detail.put("lotid",containerTaskDetail.getLotId());//批号
            detail.put("containercode",containerTaskDetail.getContainerCode());//容器
            detail.put("qty",containerTaskDetail.getQty());//分摊数量

            details.add(map);
            map.put("details",details);
            data.add(map);
        }
        return PrologApiJsonHelper.toJson(data);

        /*String billNo = "";
        List<Map<String,Object>> data = new ArrayList<>();
        List<ContainerTaskDetail> containerTaskDetails = containerTaskDetailService.selectByBillNo(billNo);
        for (ContainerTaskDetail containerTaskDetail : containerTaskDetails) {
            //封装
            Map<String, Object> map = new HashMap<>();
            map.put("billno", containerTaskDetail.getBillNo());//单据号

            List<ContainerTask> containerTasks = containerTaskService.selectByContainerCode(containerTaskDetail.getContainerCode());

            //封装明细
            List<Map<String,Object>> details = new ArrayList<>();
            Map<String,Object> detail = new HashMap<>();

            for (ContainerTask task : containerTasks) {
                map.put("tasktype",task.getTaskType());
                detail.put("seqno",containerTaskDetail.getSeqNo());//行号
                detail.put("itemid",task.getItemId());//商品id
                detail.put("lotid",task.getLotId());//批号
                detail.put("containercode",task.getContainerCode());//容器
                detail.put("qty",task.getQty());//分摊数量
            }
            details.add(map);
            map.put("details",details);
            data.add(map);
        }
        return PrologApiJsonHelper.toJson(data);*/

        /**
         * 封装
         *  {
         *  billno---单据号
         *  tasktype---任务类型
         *  details：（明细）
         *     [{
         *          seqno---行号
         *          itemid---商品id
         *          lotid---批号
         *          containercode---托盘
         *          qty---称重数量
         *      }]
         *  }
         */

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
