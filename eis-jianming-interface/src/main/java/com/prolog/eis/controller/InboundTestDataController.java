package com.prolog.eis.controller;

import com.alibaba.fastjson.JSONObject;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.InboundTaskDto;
import com.prolog.eis.model.wms.WmsEisIdempotent;
import com.prolog.eis.service.EisIdempotentService;
import com.prolog.eis.service.InboundTestDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@Api(tags = "测试入库数据接收并推送")
@RequestMapping("/api/v1/inboundTest")
public class InboundTestDataController {

    @Autowired
    private InboundTestDataService inboundTestDataService;

    @Autowired
    private EisIdempotentService eisIdempotentService;

    @ApiOperation(value = "入库任务数据生成", notes = "入库任务数据生成")
    @PostMapping("/inboundtestInterface")
    public String inboundTaskPush(@RequestBody String str){

        //将json串转换成json对象,再转换为java对象
        JSONObject jsStr = JSONObject.parseObject(str);

        InboundTaskDto inboundTaskDto = JSONObject.toJavaObject(jsStr, InboundTaskDto.class);

        String messageID = inboundTaskDto.getMessageId();

        List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageID);

         String rejson = "";

        if(!wmsEisIdempotents.isEmpty() || !(wmsEisIdempotents.size() == 0)){
            rejson = wmsEisIdempotents.get(0).getRejson();
            return rejson;
        }else{
            List<InboundTask> data = inboundTaskDto.getData();
            for (InboundTask datum : data) {
                try {
                    long l = System.currentTimeMillis();
                    Date t = new Date(l);
                    java.sql.Timestamp ctime = new java.sql.Timestamp(t.getTime());

                    datum.setCreateTime(ctime);
                    datum.setTaskState(0);

                    long ll = System.currentTimeMillis();
                    Date tt = new Date(ll);
                    java.sql.Timestamp etime = new java.sql.Timestamp(tt.getTime());

                    datum.setEndTime(etime);
                    inboundTestDataService.insertInboundTask(datum);

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
