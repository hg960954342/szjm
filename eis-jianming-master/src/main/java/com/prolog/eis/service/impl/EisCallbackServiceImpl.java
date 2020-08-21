package com.prolog.eis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.InBoundTaskHistoryMapper;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.*;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@Transactional
public class EisCallbackServiceImpl implements EisCallbackService {

    @Value("${prolog.wms.url:}")
    private String wmsIp;
    @Value("${prolog.wms.port:}")
    private String wmsPort;

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

    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    @Autowired
    private InBoundTaskHistoryMapper inBoundTaskHistoryMapper;
    @Autowired
    RestTemplate restTemplate;

    /**
     * 回告wms
     *
     * @param containerCode
     * @throws Exception
     */
    @Override
    public void inBoundReport(String containerCode) {
        String url = null;
        String json = null;
        InboundTask inboundTask = null;
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;
            List<InboundTask> inboundTasks = inBoundTaskService.selectByContainerCode(containerCode);
            if (inboundTasks != null && inboundTasks.size() > 0) {
                inboundTask = inboundTasks.get(0);

                //判断是否需要回告wms
                if (inboundTask.getReBack() == 1) {
                    //封装入库回告数据
                    json = this.inBoundReportData(inboundTask);
                    String msg = "EIS->WMS [WMSInterface] 入库回告请求JSON：[message]:" + json;
                    FileLogHelper.WriteLog("WMSRequest", msg);
                    if (inboundTask.getTaskType() == 2) {//订单入库
                        url = String.format("http://%s:%s/api/v1/StockMove/POPutaway", wmsIp, wmsPort);
                    } else {//移库入库
                        url = String.format("http://%s:%s/api/v1/StockMove/InPutaway", wmsIp, wmsPort);
                    }
                    String restJson = HttpUtils.post(url, json, token);

                    String resultMsg = "EIS->WMS [WMSInterface] 入库回告返回JSON：[message]:" + restJson;
                    FileLogHelper.WriteLog("WMSRequest", resultMsg);

                    //判断回告结果
                    this.getWmsResponseData(json, restJson, url, inboundTask.getTaskType());
                }

                inboundTask.setTaskState(4);
                inboundTask.setEndTime(new Date());
//            inBoundTaskService.update(inboundTask);
                inBoundTaskService.delete(inboundTask);
                InBoundTaskHistory history = new InBoundTaskHistory();
                org.springframework.beans.BeanUtils.copyProperties(inboundTask, history);
                //加入历史表
                inBoundTaskHistoryMapper.save(history);

            }
        } catch (IOException e) {
            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败："+e.getMessage();
            FileLogHelper.WriteLog("WMSRequestErr", resultMsg);
            RepeatReport repeatReport = new RepeatReport();
            repeatReport.setReportData(json);
            repeatReport.setReportType(inboundTask.getTaskType());
            repeatReport.setReportUrl(url);
            repeatReport.setMessage(resultMsg);
            repeatReport.setReportCount(1);
            repeatReport.setReportState(0);
            repeatReport.setCreateTime(new Date());
            repeatReportService.insert(repeatReport);
        }

    }

    /**
     * eis 业务出库 回告 wms
     *
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void outBoundReport(ContainerTask containerTask) {

        String json = null;
        String url = null;
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;
            json = this.outBoundReportData(containerTask);
            url = String.format("http://%s:%s/api/v1/StockMove/OutPull", wmsIp, wmsPort);
            String msg = "EIS->WMS [WMSInterface] 出库回告请求JSON：[message]:" + json;
            FileLogHelper.WriteLog("WMSRequest", msg);

            String restJson = HttpUtils.post(url, json, token);

            String resultMsg = "EIS->WMS [WMSInterface] 出库回告返回JSON：[message]:" + restJson;
            FileLogHelper.WriteLog("WMSRequest", resultMsg);
            //判断回告结果
            this.getWmsResponseData(json, restJson, url, containerTask.getTaskType());
        } catch (IOException e) {
            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败："+e.getMessage();
            FileLogHelper.WriteLog("WMSRequestErr", resultMsg);
            RepeatReport repeatReport = new RepeatReport();
            repeatReport.setReportData(json);
            repeatReport.setReportType(containerTask.getTaskType());
            repeatReport.setReportUrl(url);
            repeatReport.setMessage(resultMsg);
            repeatReport.setReportCount(1);
            repeatReport.setReportState(0);
            repeatReport.setCreateTime(new Date());
            repeatReportService.insert(repeatReport);
        }
    }

    /**
     * eis 移库出库 回告 wms
     *
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void moveBoundReport(ContainerTask containerTask) {
        String url = null;
        String json = null;
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;

            url = String.format("http://%s:%s/api/v1/StockMove/InPull", wmsIp, wmsPort);
            json = this.moveBoundReportData(containerTask);
            String msg = "EIS->WMS [WMSInterface] 移库回告请求JSON：[message]:" + json;
            FileLogHelper.WriteLog("WMSRequest", msg);

            String restJson = HttpUtils.post(url, json, token);
            String resultMsg = "EIS->WMS [WMSInterface] 移库回告返回JSON：[message]:" + restJson;
            FileLogHelper.WriteLog("WMSRequest", resultMsg);
            this.getWmsResponseData(json, restJson, url, containerTask.getTaskType());
        } catch (Exception e) {
            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败："+e.getMessage();
            FileLogHelper.WriteLog("WMSRequestErr", resultMsg);
            RepeatReport repeatReport = new RepeatReport();
            repeatReport.setReportData(json);
            repeatReport.setReportType(containerTask.getTaskType());
            repeatReport.setReportUrl(url);
            repeatReport.setMessage(resultMsg);
            repeatReport.setReportCount(1);
            repeatReport.setReportState(0);
            repeatReport.setCreateTime(new Date());
            repeatReportService.insert(repeatReport);
        }

    }

    /**
     * eis 盘点出库 回告 wms
     *
     * @param billNo 单据号
     * @throws Exception
     */
    @Override
    public void checkBoundReport(String billNo) {
        String url = null;
        String json = null;
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;

            url = String.format("http://%s:%s/api/v1/StockMove/PDPull", wmsIp, wmsPort);
            json = this.checkBoundReportData(billNo);
            String msg = "EIS->WMS [WMSInterface] 盘点回告请求JSON：[message]:" + json;
            FileLogHelper.WriteLog("WMSRequest", msg);
            String restJson = HttpUtils.post(url, json, token);
            String resultMsg = "EIS->WMS [WMSInterface] 盘点回告返回JSON：[message]:" + restJson;
            FileLogHelper.WriteLog("WMSRequest", resultMsg);
            this.getWmsResponseData(json, restJson, url, 3);
        } catch (IOException e) {
            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败："+e.getMessage();
            FileLogHelper.WriteLog("WMSRequestErr", resultMsg);
            RepeatReport repeatReport = new RepeatReport();
            repeatReport.setReportData(json);
            repeatReport.setReportType(3);
            repeatReport.setReportUrl(url);
            repeatReport.setMessage(resultMsg);
            repeatReport.setReportCount(1);
            repeatReport.setReportState(0);
            repeatReport.setCreateTime(new Date());
            repeatReportService.insert(repeatReport);
        }
    }

    /**
     * 重发 回告
     *
     * @param repeatReport
     */
    @Override
    public void recall(RepeatReport repeatReport) {
        try {
            wmsLoginService.loginWms();
            String token = LoginWmsResponse.accessToken;

            String url = repeatReport.getReportUrl();
            String json = repeatReport.getReportData();

            String restJson = HttpUtils.post(url, json, token);
            repeatReport.setSendTime(new Date());
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);

            if ("0".equals(helper.getString("stateCode"))) {
                repeatReport.setMessage(restJson);
                repeatReport.setReportCount(repeatReport.getReportCount() + 1);
                repeatReport.setReportState(2);
                repeatReport.setEndTime(new Date());
            } else {
                repeatReport.setReportCount(repeatReport.getReportCount() + 1);
                repeatReport.setEndTime(new Date());
                repeatReport.setMessage(restJson);
                if (repeatReport.getReportCount() > 10) {
                    repeatReport.setReportState(3);
                }
            }
            repeatReportService.update(repeatReport);
        } catch (IOException e) {
            String resultMsg = "EIS->WMS [WMSInterface] 连接wms 失败：" + e.getMessage();
            FileLogHelper.WriteLog("WMSRequestErr", resultMsg);
        }

    }


    /**
     * 订单入库回告数据封装
     *
     * @param inboundTask
     * @return
     * @throws Exception
     */
    private String inBoundReportData(InboundTask inboundTask) {

        try {
            String[] str = {"id", "wmsPush", "reBack", "emptyContainer", "ceng", "agvLoc", "lotId", "taskState", "qty", "createTime", "startTime", "rukuTime", "endTime", "class"};
            NameAndSimplePropertyPreFilter nameAndSimplePropertyPreFilter = new NameAndSimplePropertyPreFilter();
            nameAndSimplePropertyPreFilter.getExcludes().addAll(Arrays.asList(str));
            Map describe = BeanUtils.describe(inboundTask);
            List<Map<String, Object>> data = new ArrayList<>();
            describe.put("consignor", describe.get("ownerId"));
            describe.remove("ownerId");
            data.add(describe);
            Map<String, Object> map = new HashMap<>();
            map.put("data", data);
            map.put("size", data.size());
            map.put("messageID", UUID.randomUUID().toString().replaceAll("-", ""));
            return JSONObject.toJSONString(map, nameAndSimplePropertyPreFilter);
        } catch (Exception e) {
            FileLogHelper.WriteLog("GetReportDataErr", "封装数据失败：" + e.getMessage());
        }
        return null;

    }


    /**
     * 订单出库回告数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String outBoundReportData(ContainerTask containerTask) {
        try {
            List<Map<String, Object>> reportData = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
            String[] str = {"id", "lotId", "create_time", "end_time"};
            NameAndSimplePropertyPreFilter nameAndSimplePropertyPreFilter = new NameAndSimplePropertyPreFilter();
            nameAndSimplePropertyPreFilter.getExcludes().addAll(Arrays.asList(str));
//        AgvStorageLocation currentPosition = agvStorageLocationMapper.findByRcs(containerTask.getSource());
//        String strAgvLoc = PrologCoordinateUtils.splicingStr(currentPosition.getX(), currentPosition.getY(), currentPosition.getCeng());
            for (Map<String, Object> reportDatum : reportData) {
                //            reportDatum.put("agvLoc",strAgvLoc);
                reportDatum.put("type", reportDatum.get("type").toString());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("data", reportData);
            map.put("size", reportData.size());
            map.put("messageID", UUID.randomUUID().toString().replaceAll("-", ""));
            return JSONObject.toJSONString(map, nameAndSimplePropertyPreFilter);
        } catch (Exception e) {
            FileLogHelper.WriteLog("GetReportDataErr", "封装数据失败：" + e.getMessage());
        }

        return null;
    }


    /**
     * 移库出库数据封装
     *
     * @param containerTask
     * @return
     * @throws Exception
     */
    private String moveBoundReportData(ContainerTask containerTask) {
        try {
            List<Map<String, Object>> reportData = containerTaskDetailService.selectByContainerCode(containerTask.getContainerCode());
            String[] str = {"id", "consignor", "create_time", "end_time"};
            NameAndSimplePropertyPreFilter nameAndSimplePropertyPreFilter = new NameAndSimplePropertyPreFilter();
            nameAndSimplePropertyPreFilter.getExcludes().addAll(Arrays.asList(str));
//        AgvStorageLocation currentPosition = agvStorageLocationMapper.findByRcs(containerTask.getSource());
//        String strAgvLoc = PrologCoordinateUtils.splicingStr(currentPosition.getX(), currentPosition.getY(), currentPosition.getCeng());
            for (Map<String, Object> reportDatum : reportData) {
                //            reportDatum.put("agvLoc",strAgvLoc);
                reportDatum.put("type", reportDatum.get("type").toString());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("data", reportData);
            map.put("size", reportData.size());
            map.put("messageID", UUID.randomUUID().toString().replaceAll("-", ""));
            return JSONObject.toJSONString(map, nameAndSimplePropertyPreFilter);
        } catch (Exception e) {
            FileLogHelper.WriteLog("GetReportDataErr", "封装数据失败：" + e.getMessage());
        }

        return null;
    }

    /**
     * 盘点出库数据封装
     *
     * @param billNo
     * @return
     * @throws Exception
     */
    private String checkBoundReportData(String billNo) {
        //TODO Auto-generated method stub
        try {
            List<ResultContainer.DataBean> list = containerTaskDetailService.getCheckReportData(billNo);
            ResultContainer container = new ResultContainer();
            container.setData(list);
            container.setSize(list.size());
            container.setMessageID(UUID.randomUUID().toString().replaceAll("-", ""));
            String jsonString = JSON.toJSONString(container);
            Map map = JSON.parseObject(jsonString, Map.class);
            return JSON.toJSONString(map, new NameAndSimplePropertyPreFilter(), SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            FileLogHelper.WriteLog("GetReportDataErr", "封装数据失败：" + e.getMessage());
        }

        return null;
    }


    /**
     * 判断回告wms 结果
     *
     * @param restJson
     */
    private void getWmsResponseData(String json, String restJson, String url, int type) {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
        String stateCode = helper.getString("stateCode");
        if (!"0".equals(stateCode)) {//回告失败
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
