package com.prolog.eis.controller.pick;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.prolog.eis.dao.pick.PickingTaskMapper;
import com.prolog.eis.dto.pick.PushDataDto;
import com.prolog.eis.dto.pick.SendMcsPickTaskDto;
import com.prolog.eis.model.pick.PickingTask;
import com.prolog.eis.model.wms.WmsEisIdempotent;

import com.prolog.eis.service.pick.EisIdempotentService;
import com.prolog.eis.service.pick.impl.McsPickTaskSend;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "WMS拣货任务接口")
@RequestMapping("/api/v1/picktask")
public class PickingController {
    @Resource
    private EisIdempotentService eisIdempotentService;

    @Resource
    PickingTaskMapper pickingTaskMapper;

    @Autowired
    McsPickTaskSend mcsPickTaskSend;

    @ApiOperation(value = "下发数据接口", notes = "下发数据接口")
    @PostMapping("/pushData")
    public Object querySxStore(@RequestBody String pushDataDto) throws Exception {


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //大小写脱敏 默认为false  需要改为true
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        PushDataDto pushDataDtoObj=objectMapper.readValue(pushDataDto,PushDataDto.class);



        String messageId=pushDataDtoObj.getMessageID();
        List<WmsEisIdempotent> wmsEisIdempotents = eisIdempotentService.queryRejsonById(messageId);
        if(wmsEisIdempotents.isEmpty()){
             List<PushDataDto.DataBean> list=pushDataDtoObj.getData();
            SendMcsPickTaskDto sendMcsPickTaskDto=new SendMcsPickTaskDto();
            List<SendMcsPickTaskDto.CarryListBean> carryList=new ArrayList<>();
            list.forEach(x->{
                PickingTask pickingTask=new PickingTask();
                BeanUtils.copyProperties(x,pickingTask);
                pickingTaskMapper.save(pickingTask);
                SendMcsPickTaskDto.CarryListBean bean=new SendMcsPickTaskDto.CarryListBean();
                bean.setBillNo(pickingTask.getBillNo());
                bean.setContainerNo(pickingTask.getBarCode());
                carryList.add(bean);

            });

            mcsPickTaskSend.send(sendMcsPickTaskDto);

        }

         return MapUtils.put("code","0").put("message","success").getMap();

    }




}
