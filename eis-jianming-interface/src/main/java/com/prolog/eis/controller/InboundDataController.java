package com.prolog.eis.controller;


import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.InboundTaskDto;
import com.prolog.eis.model.wms.WmsEisIdempotent;
import com.prolog.eis.service.EisIdempotentService;
import com.prolog.eis.service.InboundDataService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Api(tags = "测试入库数据接收并推送")
@RequestMapping("/api/v1/inbound")
public class InboundDataController {

    @Autowired
    private InboundDataService inboundDataService;

    @Autowired
    private EisIdempotentService eisIdempotentService;

    @ApiOperation(value = "入库任务数据生成", notes = "入库任务数据生成")
    @PostMapping("/inBoundTaskInterface")
    public String inboundTaskPush(@RequestBody String str) throws Exception {


        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库请求" + str);
            InboundTaskDto inboundTaskDto = helper.getObject(InboundTaskDto.class);

            String messageId = inboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageId);

            String rejson = "";

            if(wmsEisIdempotents.size() != 0){
                rejson = wmsEisIdempotents.get(0).getRejson();
                return rejson;
            }else{
                List<InboundTask> data = inboundTaskDto.getData();
                for (InboundTask datum : data) {
                    try {

                        datum.setWmsPush(1);
                        datum.setReBack(1);
                        datum.setEmptyContainer(0);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);
                        datum.setTaskState(0);

                        inboundDataService.insertInboundTask(datum);

                    }catch (Exception e){

                        rejson+=("code : -1 ,message : false");

                        WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                        wmsEisIdempotent.setMessageId(messageId);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                        wmsEisIdempotent.setLocDate(ltime);
                        wmsEisIdempotent.setRejson(rejson);
                        eisIdempotentService.insertReport(wmsEisIdempotent);

                        FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库返回" + rejson);

                        e.printStackTrace();
                        return rejson;
                    }
                }

                rejson+=("code : 0 ,message : success");

                WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                wmsEisIdempotent.setMessageId(messageId);
                long l = System.currentTimeMillis();
                Date t = new Date(l);
                java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                wmsEisIdempotent.setLocDate(ltime);
                wmsEisIdempotent.setRejson(rejson);
                eisIdempotentService.insertReport(wmsEisIdempotent);

                FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库返回" + rejson);
                return rejson;
            }

        }catch (Exception e){
            FileLogHelper.WriteLog("WmsInStockTaskError", "入库异常，错误信息：\n" + e.toString());
            String resultStr ="code : -1 ,message : false";
            FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }

    }

    @ApiOperation(value = "空托入库任务数据生成", notes = "空托入库任务数据生成")
    @PostMapping("/emptyBoxInStockTaskInterface")
    public String emptyBoxInStockTask(@RequestBody String str) throws Exception {


        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS空托入库请求" + str);
            InboundTaskDto inboundTaskDto = helper.getObject(InboundTaskDto.class);

            String messageID = inboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            String rejson = "";

            if(wmsEisIdempotents.size() != 0){
                rejson = wmsEisIdempotents.get(0).getRejson();
                return rejson;
            }else{
                List<InboundTask> data = inboundTaskDto.getData();
                for (InboundTask datum : data) {
                    try {

                        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,10);
                        datum.setBillNo(uuid);
                        datum.setWmsPush(1);
                        datum.setReBack(0);
                        datum.setEmptyContainer(1);
                        datum.setTaskState(0);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);

                        inboundDataService.insertEmptyBoxInStockTask(datum);

                    }catch (Exception e){

                        rejson+=("code : -1 ,message : false");

                        WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                        wmsEisIdempotent.setMessageId(messageID);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                        wmsEisIdempotent.setLocDate(ltime);
                        wmsEisIdempotent.setRejson(rejson);
                        eisIdempotentService.insertReport(wmsEisIdempotent);
                        FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS空托入库返回" + rejson);

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

                FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS空托入库返回" + rejson);

                return rejson;
            }
        }catch (Exception e){
            FileLogHelper.WriteLog("WmsEmptyBoxInStockTaskError", "空托入库异常，错误信息：\n" + e.toString());
            String resultStr ="code : -1 ,message : false";
            FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }
}
