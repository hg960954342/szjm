package com.prolog.eis.controller;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.dao.ContainerTaskMapper;
import com.prolog.eis.dao.SxStockTaskMapper;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.service.CheckContainerTaskService;
import com.prolog.eis.service.EisIdempotentService;
import com.prolog.eis.service.OutboundDataService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "测试出库数据接收并推送")
@RequestMapping("/api/v1/outbound")
public class OutboundDataController {

    @Autowired
    private EisIdempotentService eisIdempotentService;

    @Autowired
    private OutboundDataService outboundDataService;

    @Autowired
    private CheckContainerTaskService checkContainerTaskService;

    @Autowired
    private ContainerTaskMapper containerTaskMapper;

    @Autowired
    private SxStockTaskMapper sxStockTaskMapper;



    /**
     * 业务出库 推送eis
     *
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "出库任务数据生成", notes = "出库任务数据生成")
    @PostMapping("/outboundtask")
    public JsonResult outboundTaskPush(@RequestBody String str) throws Exception {

       // PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsOutStockTask", "WMS->EIS出库请求" + str);
            OutboundTaskDto outboundTaskDto =  JSONObject.parseObject(str,OutboundTaskDto.class);

            String messageID = outboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                //List<OutboundTask> data = outboundTaskDto.getData();
                List<Outbt> data = outboundTaskDto.getData();
                for (Outbt datum : data) {
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

                        List<OutboundTaskDetailDto> details = datum.getDetails();
                        for (OutboundTaskDetailDto detail : details) {

                            String billno = datum.getBillNo();
                            String ownerid = datum.getOwnerId();
                            String pickcode = datum.getPickCode();

                            detail.setBillNo(billno);
                            detail.setOwnerId(ownerid);
                            detail.setPickCode(pickcode);
                            detail.setCtReq(0);
                            detail.setFinishQty(BigDecimal.ZERO);
                            if(detail.getStandard().equals(BigDecimal.ZERO)){
                                throw new Exception("商品数量规格未传值，无法生成出库任务！");
                            }

                            long l1 = System.currentTimeMillis();
                            Date t1 = new Date(l1);
                            java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                            detail.setCreateTime(createtime);

                            //TODO 转换单位KG-》G
                            OutboundTaskDetailDto outboundTaskDetailDto=new OutboundTaskDetailDto();
                            BeanUtils.copyProperties(detail,outboundTaskDetailDto);
                            outboundTaskDetailDto.setStandard(detail.getStandard().multiply(new BigDecimal("1000")));
                            outboundTaskDetailDto.setQty(detail.getQty().multiply(new BigDecimal("1000")));
                            outboundDataService.insertOutboundTaskDetail(outboundTaskDetailDto);
                        }

                    } catch (Exception e) {

                        rejson.setCode("-1");
                        rejson.setMessage(e.getMessage());

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

        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsOutStockTaskError", "出库异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsOutStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }

    }


    /**
     * 业务移库 推送eis
     *
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "移库出库任务数据生成", notes = "移库出库任务数据生成")
    @PostMapping("/movestocktask")
    public JsonResult moveTaskPush(@RequestBody String str) throws Exception {

      //  PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS移库请求" + str);
           // OutboundTaskDto moveTaskData = helper.getObject(OutboundTaskDto.class);
            OutboundTaskDto moveTaskData =  JSONObject.parseObject(str,OutboundTaskDto.class);
            String messageID = moveTaskData.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                //List<OutboundTask> data = moveTaskData.getData();
                List<Outbt> data = moveTaskData.getData();
                List<String> containerCodeList = new ArrayList<>();
                for (Outbt datum : data) {
                    try {

                        datum.setWmsPush(1);
                        datum.setReBack(1);
                        datum.setEmptyContainer(0);
                        datum.setTaskType(0);
                        datum.setTaskState(0);

                        long l = System.currentTimeMillis();
                        Date t = new Date(l);
                        java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                        datum.setCreateTime(ctime);

                        List<OutboundTaskDetailDto> details = datum.getDetails();
                        List<String> containerCodes = checkContainerTaskService.findByContainerCode(details);
                        if (containerCodes.size() == 0) {
                            for (OutboundTaskDetailDto detail : details) {
                                String billno = datum.getBillNo();
                                detail.setCtReq(1);
                                detail.setFinishQty(BigDecimal.ZERO);

                                detail.setBillNo(billno);

                                long l1 = System.currentTimeMillis();
                                Date t1 = new Date(l1);
                                java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                                detail.setCreateTime(createtime);
                                //TODO 转换单位KG-》G
                                OutboundTaskDetailDto outboundTaskDetail=new OutboundTaskDetailDto();
                                BeanUtils.copyProperties(detail,outboundTaskDetail);
                                outboundTaskDetail.setStandard(detail.getStandard().multiply(new BigDecimal("1000")));
                                outboundTaskDetail.setQty(detail.getQty().multiply(new BigDecimal("1000")));
                                outboundDataService.insertMoveTaskDetail(outboundTaskDetail);
                            }
                        }else {
                            containerCodeList.addAll(containerCodes);
                            continue;
                        }

                        outboundDataService.insertMoveTask(datum);

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

                        FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS移库返回" + rejson);

                        e.printStackTrace();
                        return rejson;
                    }
                }

                if (containerCodeList.size()>0){
                    rejson.setCode("-1");
                    rejson.setMessage("托盘号："+containerCodeList.toString()+"正在执行其他任务");
                    FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS移库返回" + rejson);
                    return rejson;
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
        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsMoveStockTaskError", "移库异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsMoveStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }

    /**
     * 业务盘点 推送eis
     *
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "盘点出库任务数据生成", notes = "盘点出库任务数据生成")
    @PostMapping("/checkouttask")
    public JsonResult checkOutTaskPush(@RequestBody String str) throws Exception {

        //PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsCheckOutTask", "WMS->EIS盘库请求" + str);
           // OutboundTaskDto outboundTaskDto = helper.getObject(OutboundTaskDto.class);
            OutboundTaskDto outboundTaskDto =  JSONObject.parseObject(str,OutboundTaskDto.class);
            String messageID = outboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                //List<OutboundTask> data = outboundTaskDto.getData();
                List<Outbt> data = outboundTaskDto.getData();
                for (Outbt datum : data) {
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

                        List<OutboundTaskDetailDto> details = datum.getDetails();
                        for (OutboundTaskDetailDto detail : details) {

                            String billno = datum.getBillNo();
                            detail.setBillNo(billno);
                            detail.setCtReq(0);

                            long l1 = System.currentTimeMillis();
                            Date t1 = new Date(l1);
                            java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                            detail.setCreateTime(createtime);

                            OutboundTaskDetailDto outboundTaskDetail=new OutboundTaskDetailDto();
                            BeanUtils.copyProperties(detail,outboundTaskDetail);
                            outboundDataService.insertCheckOutTaskDetail(outboundTaskDetail);
                        }

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
        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsCheckOutTaskError", "盘库异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsCheckOutTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }

    /**
     * 业务空托出库 推送eis
     *
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "空托出库任务数据生成", notes = "空托出库任务数据生成")
    @PostMapping("/emptyboxoutstocktask")
    public JsonResult emptyBoxOutStockTask(@RequestBody String str) throws Exception {

      //  PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(str);

        try {
            FileLogHelper.WriteLog("WmsEmptyBoxOutStockTask", "WMS->EIS空托出库请求" + str);
           // OutboundTaskDto outboundTaskDto = helper.getObject(OutboundTaskDto.class);
            OutboundTaskDto outboundTaskDto =  JSONObject.parseObject(str,OutboundTaskDto.class);
            String messageID = outboundTaskDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                //List<OutboundTask> data = outboundTaskDto.getData();
                List<Outbt> data = outboundTaskDto.getData();
                for (Outbt datum : data) {
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

                        OutboundTaskDetailDto detail = new OutboundTaskDetailDto();

                        String billno = datum.getBillNo();
                        detail.setBillNo(billno);
                        detail.setCtReq(0);
                        detail.setSeqNo("1");

                        //detail.setQty(datum.getQty());
                        detail.setFinishQty(BigDecimal.ZERO);
                        String pickcode = datum.getPickCode();
                        detail.setPickCode(pickcode);

                        long l1 = System.currentTimeMillis();
                        Date t1 = new Date(l1);
                        java.sql.Timestamp createtime = new java.sql.Timestamp(t1.getTime());

                        detail.setCreateTime(createtime);

                        outboundDataService.insertEmptyBoxOutStockTaskDetail(detail);

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
        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsEmptyBoxOutStockTaskError", "空托出库异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsEmptyBoxOutStockTask", "WMS->EIS返回" + resultStr);

            return resultStr;
        }
    }

    /**
     * 库存盘点调整 推送eis
     * @param str json串
     * @return JsonResult json串
     * @throws Exception
     */
    @ApiOperation(value = "库存盘点调整任务数据生成", notes = "库存盘点调整任务数据生成")
    @PostMapping("/checkstock")


    public JsonResult checkStock(@RequestBody String str) throws Exception {



        try {
            FileLogHelper.WriteLog("WmsCheckStock", "WMS->EIS库存盘点调整" + str);

            CheckStockDto checkStockDto =  JSONObject.parseObject(str,CheckStockDto.class);

            String messageId = checkStockDto.getMessageId();

            List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageId);

            JsonResult rejson = new JsonResult();
            String string = "";

            if (wmsEisIdempotents.size() != 0) {
                string = wmsEisIdempotents.get(0).getRejson();
                PrologApiJsonHelper helper1 = PrologApiJsonHelper.createHelper(string);
                rejson = helper1.getObject(JsonResult.class);
                return rejson;
            } else {
                List<CheckStock> data = checkStockDto.getData();
                for (CheckStock datum : data) {
                        try {
                            String containerCode = datum.getContainerCode();
                            String ownerId = datum.getOwnerId();
                            String itemId = datum.getItemId();
                            String lotId = datum.getLotId();
                            int count = containerTaskMapper.findByContainerCode(containerCode);
                            SxStore sxStore = sxStockTaskMapper.queryByCode(containerCode);
                            if(count==0 && ownerId.equals(sxStore.getOwnerId()) && itemId.equals(sxStore.getItemId()) && lotId.equals(sxStore.getLotId())){

                                sxStockTaskMapper.updateQty(datum.getContainerCode(),datum.getDiffQty());
                            }else {
                                rejson.setCode("-1");
                                rejson.setMessage("当前托盘有任务或指定信息不正确");
                                FileLogHelper.WriteLog("WmsCheckStockError", "WMS->EIS库存盘点调整失败" + rejson);
                                return rejson;
                            }

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

                            FileLogHelper.WriteLog("WmsCheckStock", "WMS->EIS库存盘点调整返回" + rejson);

                            e.printStackTrace();
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

                FileLogHelper.WriteLog("WmsCheckStock", "WMS->EIS库存盘点调整返回" + rejson);
                return rejson;
            }

        } catch (Exception e) {
            FileLogHelper.WriteLog("WmsCheckStockError", "库存盘点调整异常，错误信息：\n" + e.toString());
            JsonResult resultStr = new JsonResult();
            resultStr.setCode("-1");
            resultStr.setMessage("false");
            FileLogHelper.WriteLog("WmsCheckStock", "WMS->EIS返回" + resultStr);

            return resultStr;
        }

    }

}
