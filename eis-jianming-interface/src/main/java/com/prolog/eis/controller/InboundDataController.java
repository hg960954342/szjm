package com.prolog.eis.controller;


import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.EisIdempotentService;
import com.prolog.eis.service.InboundDataService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.PrologApiJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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



    /**
     * 业务入库 推送eis
     *
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "入库任务数据生成", notes = "入库任务数据生成")
    @PostMapping("/inboundtask")
    public JsonResult inboundTaskPush(@RequestBody String str) throws Exception {




        try {
            FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库请求" + str);

            InboundTaskDto inboundTaskDto =  JSONObject.parseObject(str,InboundTaskDto.class);
            String messageId = inboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageId);


            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                List<InboundTask> data = inboundTaskDto.getData();
                for (InboundTask datum : data) {
                    if ((Integer.parseInt(datum.getCeng()) == 3 && datum.getAgvLoc().contains("XY")) || (Integer.parseInt(datum.getCeng()) == 4 && datum.getAgvLoc().contains("AB"))) {

                        try {
                            //判断任务是否重复
                            Integer count = inboundDataService.findByContainerCode(datum.getContainerCode());
                            if (count != 0){

                                rejson.setCode("-1");
                                rejson.setMessage("【入库任务】托盘号："+datum.getContainerCode()+"任务重复");
                                FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库返回" + rejson);
                                return rejson;
                            }

                            datum.setWmsPush(1);
                            datum.setReBack(1);
                            datum.setEmptyContainer(0);
                            long l = System.currentTimeMillis();
                            Date t = new Date(l);
                            java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                            datum.setCreateTime(ctime);
                            datum.setTaskState(0);
                            //TODO 转换单位 KG-->G
                            InboundTask inboundTask=new InboundTask();
                            BeanUtils.copyProperties(datum,inboundTask);
                            inboundTask.setQty(datum.getQty().multiply(new BigDecimal("1000")));
                            inboundDataService.insertInboundTask(inboundTask);

                        } catch (Exception e) {

                            rejson.setCode("-1");
                            rejson.setMessage("false");

                            WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                            wmsEisIdempotent.setMessageId(messageId);
                            long l = System.currentTimeMillis();
                            Date t = new Date(l);
                            java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                            wmsEisIdempotent.setLocDate(ltime);

                            string = JSONObject.toJSONString(rejson);
                            wmsEisIdempotent.setRejson(string);
                            eisIdempotentService.insertReport(wmsEisIdempotent);

                            FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库返回" + rejson);

                            e.printStackTrace();
                            return rejson;
                        }
                    } else {
                        rejson.setCode("-1");
                        rejson.setMessage("agv小车不能跨层!!!");
                        FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库返回" + rejson);
                        return rejson;
                    }
                }

                rejson.setCode("0");
                rejson.setMessage("success");

                WmsEisIdempotent wmsEisIdempotent = new WmsEisIdempotent();
                wmsEisIdempotent.setMessageId(messageId);
                long l = System.currentTimeMillis();
                Date t = new Date(l);
                java.sql.Timestamp ltime = new java.sql.Timestamp(t.getTime());
                wmsEisIdempotent.setLocDate(ltime);
                string = JSONObject.toJSONString(rejson);
                wmsEisIdempotent.setRejson(string);
                eisIdempotentService.insertReport(wmsEisIdempotent);

                FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS入库返回" + rejson);
                return rejson;
            }

        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsInStockTaskError", "入库异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsInStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }

    }

    /**
     * 业务空托入库 推送eis
     *
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "空托入库任务数据生成", notes = "空托入库任务数据生成")
    @PostMapping("/emptyboxinstocktask")
    public JsonResult emptyBoxInStockTask(@RequestBody String str) throws Exception {


        //PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS空托入库请求" + str);
            //InboundTaskDto inboundTaskDto = helper.getObject(InboundTaskDto.class);
            InboundTaskDto inboundTaskDto =  JSONObject.parseObject(str,InboundTaskDto.class);
            String messageID = inboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                List<InboundTask> data = inboundTaskDto.getData();
                for (InboundTask datum : data) {
                    try {

                        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
                        datum.setBillNo(uuid);
                        datum.setWmsPush(1);
                        datum.setReBack(0);
                        datum.setEmptyContainer(1);
                        datum.setTaskState(0);
                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);
                        InboundTask inboundTask=new InboundTask();
                        BeanUtils.copyProperties(datum,inboundTask);
                        inboundDataService.insertEmptyBoxInStockTask(inboundTask);

                    } catch (Exception e) {

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
                        FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS空托入库返回" + rejson);

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

                FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS空托入库返回" + rejson);

                return rejson;
            }
        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsEmptyBoxInStockTaskError", "空托入库异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsEmptyBoxInStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }
}
