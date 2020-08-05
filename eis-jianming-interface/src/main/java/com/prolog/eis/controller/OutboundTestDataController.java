package com.prolog.eis.controller;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.EisIdempotentService;
import com.prolog.eis.service.OutboundTestDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@Api(tags = "测试出库数据接收并推送")
@RequestMapping("/api/v1/outboundTest")
public class OutboundTestDataController {

    @Autowired
    private EisIdempotentService eisIdempotentService;

    @Autowired
    private OutboundTestDataService outboundTestDataService;

    @ApiOperation(value = "出库任务数据生成", notes = "出库任务数据生成")
    @PostMapping("/outboundtestInterface")
    public String outboundTaskPush(@RequestBody String str){

        JSONObject jsStr = JSONObject.parseObject(str);

        OutboundTaskDto outboundTaskDto = JSONObject.toJavaObject(jsStr, OutboundTaskDto.class);

        String messageID = outboundTaskDto.getMessageId();

        List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

        String rejson = "";

        if(!wmsEisIdempotents.isEmpty() || !(wmsEisIdempotents.size() == 0)){
            rejson = wmsEisIdempotents.get(0).getRejson();
            return rejson;
        }else{
            List<OutboundTask> data = outboundTaskDto.getData();
            for (OutboundTask datum : data) {
                try {
                    long l = System.currentTimeMillis();
                    Date t = new Date(l);
                    java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                    datum.setCreateTime(ctime);

                    long ll = System.currentTimeMillis();
                    Date tt = new Date(ll);
                    java.sql.Timestamp etime = new java.sql.Timestamp(tt.getTime());

                    datum.setEndTime(etime);
                    outboundTestDataService.insertOutboundTask(datum);

                    List<OutboundTaskDetail> details = datum.getDetails();
                    for (OutboundTaskDetail detail : details) {

                        String ownerid = datum.getOwnerId();
                        String pickcode = datum.getPickCode();
                        detail.setOwnerId(ownerid);
                        detail.setPickCode(pickcode);

                        long l1 = System.currentTimeMillis();
                        Date t1 = new Date(l1);
                        java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                        detail.setCreateTime(createtime);

                        long l2 = System.currentTimeMillis();
                        Date t2 = new Date(l2);
                        java.sql.Timestamp endtime = new java.sql.Timestamp(t2.getTime());

                        detail.setEndTime(endtime);
                        outboundTestDataService.insertOutboundTaskDetail(detail);
                    }

                }catch (Exception e){

                    rejson+=("code : -1 ,message : false");

                    e.printStackTrace();
                    return rejson;
                }
            }

            rejson+=("code : 0 ,message : success");

            WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
            wmsEisIdempotent.setMessageId(messageID);
            long l = System.currentTimeMillis();
            Date t = new Date(l);
            java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
            wmsEisIdempotent.setLocDate(ltime);
            wmsEisIdempotent.setRejson(rejson);
            eisIdempotentService.insertReport(wmsEisIdempotent);

            return rejson;
        }
    }


    @ApiOperation(value = "移库任务数据生成", notes = "移库任务数据生成")
    @PostMapping("/movetestInterface")
    public String moveTaskPush(@RequestBody String str){

        JSONObject jsStr = JSONObject.parseObject(str);
        OutboundTaskDto moveTaskData = JSONObject.toJavaObject(jsStr, OutboundTaskDto.class);

        String messageID = moveTaskData.getMessageId();

        List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

        String rejson = "";

        if(!wmsEisIdempotents.isEmpty() || !(wmsEisIdempotents.size() == 0)){
            rejson = wmsEisIdempotents.get(0).getRejson();
            return rejson;
        }else{
            List<OutboundTask> data = moveTaskData.getData();
            for (OutboundTask datum : data) {
                try {
                    long l = System.currentTimeMillis();
                    Date t = new Date(l);
                    java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                    datum.setCreateTime(ctime);

                    long ll = System.currentTimeMillis();
                    Date tt = new Date(ll);
                    java.sql.Timestamp etime = new java.sql.Timestamp(tt.getTime());

                    datum.setEndTime(etime);

                    outboundTestDataService.insertMoveTask(datum);

                    List<OutboundTaskDetail> details = datum.getDetails();
                    for (OutboundTaskDetail detail : details) {

                        long l1 = System.currentTimeMillis();
                        Date t1 = new Date(l1);
                        java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                        detail.setCreateTime(createtime);

                        long l2 = System.currentTimeMillis();
                        Date t2 = new Date(l2);
                        java.sql.Timestamp endtime = new java.sql.Timestamp(t2.getTime());

                        detail.setEndTime(endtime);
                        outboundTestDataService.insertMoveTaskDetail(detail);
                    }

                }catch (Exception e){

                    rejson+=("code : -1 ,message : false");

                    e.printStackTrace();
                    return rejson;
                }
            }

            rejson+=("code : 0 ,message : success");

            WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
            wmsEisIdempotent.setMessageId(messageID);
            long l = System.currentTimeMillis();
            Date t = new Date(l);
            java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
            wmsEisIdempotent.setLocDate(ltime);
            wmsEisIdempotent.setRejson(rejson);
            eisIdempotentService.insertReport(wmsEisIdempotent);

            return rejson;
        }

    }
}
