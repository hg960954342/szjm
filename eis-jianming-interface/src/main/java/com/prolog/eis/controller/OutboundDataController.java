package com.prolog.eis.controller;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.EisIdempotentService;
import com.prolog.eis.service.OutboundDataService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
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
@RequestMapping("/api/v1/outbound")
public class OutboundDataController {

    @Autowired
    private EisIdempotentService eisIdempotentService;

    @Autowired
    private OutboundDataService outboundDataService;

    /**
     * 业务出库 推送eis
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "出库任务数据生成", notes = "出库任务数据生成")
    @PostMapping("/outboundtask")
    public JsonResult outboundTaskPush(@RequestBody String str) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsOutStockTask", "WMS->EIS出库请求" + str);
            OutboundTaskDto outboundTaskDto = helper.getObject(OutboundTaskDto.class);

            String messageID = outboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string= "";

            if(wmsEisIdempotents.size() != 0){
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            }else{
                List<OutboundTask> data = outboundTaskDto.getData();
                for (OutboundTask datum : data) {
                    try {

                        datum.setWmsPush(1);
                        datum.setReBack(1);
                        datum.setEmptyContainer(0);
                        datum.setTaskType(1);
                        datum.setTaskState(0);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);

                        outboundDataService.insertOutboundTask(datum);

                        List<OutboundTaskDetail> details = datum.getDetails();
                        for (OutboundTaskDetail detail : details) {

                            String billno = datum.getBillNo();
                            String ownerid = datum.getOwnerId();
                            String pickcode = datum.getPickCode();

                            detail.setBillNo(billno);
                            detail.setOwnerId(ownerid);
                            detail.setPickCode(pickcode);
                            detail.setCtReq(0);
                            detail.setFinishQty(0);

                            long l1 = System.currentTimeMillis();
                            Date t1 = new Date(l1);
                            java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                            detail.setCreateTime(createtime);

                            outboundDataService.insertOutboundTaskDetail(detail);
                        }

                    }catch (Exception e){

                        rejson.setCode("-1");
                        rejson.setMessage("false");

                        WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                        wmsEisIdempotent.setMessageId(messageID);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                        wmsEisIdempotent.setLocDate(ltime);
                        string = JSONObject.toJSONString(rejson);
                        wmsEisIdempotent.setRejson(string);
                        eisIdempotentService.insertReport(wmsEisIdempotent);

                        FileLogHelper.WriteLog("WmsOutStockTask", "WMS->EIS出库返回" + rejson);

                        e.printStackTrace();
                        return rejson;
                    }
                }

                rejson.setCode("0");
                rejson.setMessage("success");

                WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                wmsEisIdempotent.setMessageId(messageID);
                long l = System.currentTimeMillis();
                Date t = new Date(l);
                java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                wmsEisIdempotent.setLocDate(ltime);
                string = JSONObject.toJSONString(rejson);
                wmsEisIdempotent.setRejson(string);
                eisIdempotentService.insertReport(wmsEisIdempotent);

                FileLogHelper.WriteLog("WmsOutStockTask", "WMS->EIS出库返回" + rejson);

                return rejson;
            }

        }catch (Exception e){
            FileLogHelper.WriteLog("WmsOutStockTaskError", "出库异常，错误信息：\n" + e.toString());
            JsonResult resultStr =new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsOutStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }

    }


    /**
     * 业务移库 推送eis
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "移库出库任务数据生成", notes = "移库出库任务数据生成")
    @PostMapping("/movestocktask")
    public JsonResult moveTaskPush(@RequestBody String str) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS移库请求" + str);
            OutboundTaskDto moveTaskData = helper.getObject(OutboundTaskDto.class);

            String messageID = moveTaskData.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string= "";

            if(wmsEisIdempotents.size() != 0){
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            }else{
                List<OutboundTask> data = moveTaskData.getData();
                for (OutboundTask datum : data) {
                    try {

                        datum.setWmsPush(1);
                        datum.setReBack(1);
                        datum.setEmptyContainer(0);
                        datum.setTaskType(2);
                        datum.setTaskState(0);

                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);

                        outboundDataService.insertMoveTask(datum);

                        List<OutboundTaskDetail> details = datum.getDetails();
                        for (OutboundTaskDetail detail : details) {

                            String billno = datum.getBillNo();
                            detail.setCtReq(1);
                            detail.setFinishQty(0);

                            detail.setBillNo(billno);

                            long l1 = System.currentTimeMillis();
                            Date t1 = new Date(l1);
                            java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                            detail.setCreateTime(createtime);

                            outboundDataService.insertMoveTaskDetail(detail);
                        }

                    }catch (Exception e){

                        rejson.setCode("-1");
                        rejson.setMessage("false");

                        WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                        wmsEisIdempotent.setMessageId(messageID);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                        wmsEisIdempotent.setLocDate(ltime);
                        string = JSONObject.toJSONString(rejson);
                        wmsEisIdempotent.setRejson(string);
                        eisIdempotentService.insertReport(wmsEisIdempotent);

                        FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS移库返回" + rejson);

                        e.printStackTrace();
                        return rejson;
                    }
                }

                rejson.setCode("0");
                rejson.setMessage("success");

                WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                wmsEisIdempotent.setMessageId(messageID);
                long l = System.currentTimeMillis();
                Date t = new Date(l);
                java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                wmsEisIdempotent.setLocDate(ltime);
                string = JSONObject.toJSONString(rejson);
                wmsEisIdempotent.setRejson(string);
                eisIdempotentService.insertReport(wmsEisIdempotent);
                FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS移库返回" + rejson);

                return rejson;
            }
        }catch (Exception e){
            FileLogHelper.WriteLog("WmsMoveStockTaskError", "移库异常，错误信息：\n" + e.toString());
            JsonResult resultStr =new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }

    /**
     * 业务盘点 推送eis
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "盘点出库任务数据生成", notes = "盘点出库任务数据生成")
    @PostMapping("/checkouttask")
    public JsonResult checkOutTaskPush(@RequestBody String str) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsCheckOutTask", "WMS->EIS盘库请求" + str);
            OutboundTaskDto outboundTaskDto = helper.getObject(OutboundTaskDto.class);

            String messageID = outboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string= "";

            if(wmsEisIdempotents.size() != 0){
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            }else{
                List<OutboundTask> data = outboundTaskDto.getData();
                for (OutboundTask datum : data) {
                    try {

                        datum.setWmsPush(1);
                        datum.setReBack(1);
                        datum.setEmptyContainer(0);
                        datum.setTaskType(3);
                        datum.setTaskState(0);

                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);

                        outboundDataService.insertMoveTask(datum);

                        List<OutboundTaskDetail> details = datum.getDetails();
                        for (OutboundTaskDetail detail : details) {

                            String billno = datum.getBillNo();
                            detail.setBillNo(billno);
                            detail.setCtReq(0);

                            long l1 = System.currentTimeMillis();
                            Date t1 = new Date(l1);
                            java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                            detail.setCreateTime(createtime);

                            outboundDataService.insertCheckOutTaskDetail(detail);
                        }

                    }catch (Exception e){

                        rejson.setCode("-1");
                        rejson.setMessage("false");

                        WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                        wmsEisIdempotent.setMessageId(messageID);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                        wmsEisIdempotent.setLocDate(ltime);
                        string = JSONObject.toJSONString(rejson);
                        wmsEisIdempotent.setRejson(string);
                        eisIdempotentService.insertReport(wmsEisIdempotent);
                        FileLogHelper.WriteLog("WmsCheckOutTask", "WMS->EIS盘库返回" + rejson);

                        e.printStackTrace();
                        return rejson;
                    }
                }

                rejson.setCode("0");
                rejson.setMessage("success");

                WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                wmsEisIdempotent.setMessageId(messageID);
                long l = System.currentTimeMillis();
                Date t = new Date(l);
                java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                wmsEisIdempotent.setLocDate(ltime);
                string = JSONObject.toJSONString(rejson);
                wmsEisIdempotent.setRejson(string);
                eisIdempotentService.insertReport(wmsEisIdempotent);
                FileLogHelper.WriteLog("WmsCheckOutTask", "WMS->EIS盘库返回" + rejson);

                return rejson;
            }
        }catch (Exception e){
            FileLogHelper.WriteLog("WmsCheckOutTaskError", "盘库异常，错误信息：\n" + e.toString());
            JsonResult resultStr =new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsCheckOutTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }

    /**
     * 业务空托出库 推送eis
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "空托出库任务数据生成", notes = "空托出库任务数据生成")
    @PostMapping("/emptyboxoutstocktask")
    public JsonResult emptyBoxOutStockTask(@RequestBody String str) throws Exception {

        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsEmptyBoxOutStockTask", "WMS->EIS空托出库请求" + str);
            OutboundTaskDto outboundTaskDto = helper.getObject(OutboundTaskDto.class);

            String messageID = outboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string= "";

            if(wmsEisIdempotents.size() != 0){
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            }else{
                List<OutboundTask> data = outboundTaskDto.getData();
                for (OutboundTask datum : data) {
                    try {

                        datum.setWmsPush(1);
                        datum.setReBack(0);
                        datum.setEmptyContainer(1);
                        datum.setTaskType(4);
                        datum.setTaskState(0);

                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);

                        outboundDataService.insertEmptyBoxOutStockTask(datum);

                        OutboundTaskDetail detail = new OutboundTaskDetail();

                        String billno = datum.getBillNo();
                        detail.setBillNo(billno);
                        detail.setCtReq(0);
                        detail.setSeqNo("1");

                        detail.setQty(datum.getQty());
                        detail.setFinishQty(0);
                        String pickcode = datum.getPickCode();
                        detail.setPickCode(pickcode);

                        long l1 = System.currentTimeMillis();
                        Date t1 = new Date(l1);
                        java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                        detail.setCreateTime(createtime);

                        outboundDataService.insertEmptyBoxOutStockTaskDetail(detail);

                    }catch (Exception e){

                        rejson.setCode("-1");
                        rejson.setMessage("false");

                        WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                        wmsEisIdempotent.setMessageId(messageID);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                        wmsEisIdempotent.setLocDate(ltime);
                        string = JSONObject.toJSONString(rejson);
                        wmsEisIdempotent.setRejson(string);
                        eisIdempotentService.insertReport(wmsEisIdempotent);
                        FileLogHelper.WriteLog("WmsEmptyBoxOutStockTask", "WMS->EIS空托出库返回" + rejson);

                        e.printStackTrace();
                        return rejson;
                    }
                }

                rejson.setCode("0");
                rejson.setMessage("success");

                WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                wmsEisIdempotent.setMessageId(messageID);
                long l = System.currentTimeMillis();
                Date t = new Date(l);
                java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                wmsEisIdempotent.setLocDate(ltime);
                string = JSONObject.toJSONString(rejson);
                wmsEisIdempotent.setRejson(string);
                eisIdempotentService.insertReport(wmsEisIdempotent);
                FileLogHelper.WriteLog("WmsEmptyBoxOutStockTask", "WMS->EIS空托出库返回" + rejson);

                return rejson;
            }
        }catch (Exception e){
            FileLogHelper.WriteLog("WmsEmptyBoxOutStockTaskError", "空托出库异常，错误信息：\n" + e.toString());
            JsonResult resultStr =new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsEmptyBoxOutStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }

}
