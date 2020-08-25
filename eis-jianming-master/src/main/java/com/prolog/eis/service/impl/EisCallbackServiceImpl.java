package com.prolog.eis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dao.InBoundTaskHistoryMapper;
import com.prolog.eis.dao.RepeatReportMapper;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.ContainerTaskDetailService;
import com.prolog.eis.service.ContainerTaskService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.InBoundTaskService;
import com.prolog.eis.service.impl.unbound.entity.CheckOutResponse;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.NameAndSimplePropertyPreFilter;
import com.prolog.eis.util.PrologStringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    private RepeatReportMapper repeatReportMapper;


    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;

    @Autowired
    private InBoundTaskHistoryMapper inBoundTaskHistoryMapper;


    @Override
    public void updateResport(boolean  isSuccess,RepeatReport repeatReport){

            if(isSuccess){
                repeatReportMapper.deleteById(repeatReport.getId(),RepeatReport.class);
            }else{
                repeatReport.setSendTime(new Date());
                 repeatReportMapper.update(repeatReport);
            }


    }

    /**
     * 入库 回告 wms 写入回告重发表
     *
     * @param containerCode
     * @throws Exception
     */
    @Override
    public void inBoundReport(String containerCode) {
        List<InboundTask> inboundTasks = inBoundTaskService.selectByContainerCode(containerCode);
        if (inboundTasks != null && inboundTasks.size() > 0) {
            InboundTask inboundTask = inboundTasks.get(0);

            //判断是否需要回告wms
            if (inboundTask.getReBack() == 1) {
                //封装入库回告数据
                String json = this.inBoundReportData(inboundTask);
                String url = null;
                if (inboundTask.getTaskType() == 2) {//订单入库
                    url = String.format("http://%s:%s/api/v1/StockMove/POPutaway", wmsIp, wmsPort);
                } else {//移库入库
                    url = String.format("http://%s:%s/api/v1/StockMove/InPutaway", wmsIp, wmsPort);
                }

                //写入重发表
                RepeatReport repeatReport = new RepeatReport();
                repeatReport.setReportData(json);
                repeatReport.setReportUrl(url);
                repeatReport.setReportState(0);
                repeatReport.setCreateTime(new Date());
                repeatReportMapper.save(repeatReport);
            }

            inBoundTaskService.delete(inboundTask);
            inboundTask.setTaskState(4);
            inboundTask.setEndTime(new Date());
            InBoundTaskHistory history = new InBoundTaskHistory();
            org.springframework.beans.BeanUtils.copyProperties(inboundTask, history);
            //加入历史表
            inBoundTaskHistoryMapper.save(history);

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
        String json = this.outBoundReportData(containerTask);
        String url = String.format("http://%s:%s/api/v1/StockMove/OutPull", wmsIp, wmsPort);

        RepeatReport repeatReport = new RepeatReport();
        repeatReport.setReportData(json);
        repeatReport.setReportUrl(url);
        repeatReport.setReportState(0);
        repeatReport.setCreateTime(new Date());
        repeatReportMapper.save(repeatReport);
    }

    /**
     * eis 移库出库 回告 wms
     *
     * @param containerTask
     * @throws Exception
     */
    @Override
    public void moveBoundReport(ContainerTask containerTask) {
        String url = String.format("http://%s:%s/api/v1/StockMove/InPull", wmsIp, wmsPort);
        String json = this.moveBoundReportData(containerTask);

        RepeatReport repeatReport = new RepeatReport();
        repeatReport.setReportData(json);
        repeatReport.setReportUrl(url);
        repeatReport.setReportState(0);
        repeatReport.setCreateTime(new Date());
        repeatReportMapper.save(repeatReport);

    }

    /**
     * eis 盘点出库 回告 wms
     *
     * @param billNo 单据号
     * @throws Exception
     */
    @Override
    public void checkBoundReport(String billNo) {
        String url = String.format("http://%s:%s/api/v1/StockMove/PDPull", wmsIp, wmsPort);
        String json = this.checkBoundReportData(billNo);

        RepeatReport repeatReport = new RepeatReport();
        repeatReport.setReportData(json);
        repeatReport.setReportUrl(url);
        repeatReport.setCreateTime(new Date());
        repeatReport.setReportState(0);
        repeatReportMapper.save(repeatReport);
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
            for (Map<String, Object> reportDatum : reportData) {
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
            for (Map<String, Object> reportDatum : reportData) {
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
        try {

            List<CheckOutResponse.DataBean> list = containerTaskDetailService.getCheckReportData(billNo);
            CheckOutResponse container = new CheckOutResponse();
            container.setData(list);
            container.setSize(list.size());
            container.setMessageID(PrologStringUtils.newGUID());
            String jsonString = JSON.toJSONString(container);
            Map map = JSON.parseObject(jsonString, Map.class);
            return JSON.toJSONString(map, new NameAndSimplePropertyPreFilter(), SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            FileLogHelper.WriteLog("GetReportDataErr", "封装数据失败：" + e.getMessage());
        }

        return null;
    }

}
