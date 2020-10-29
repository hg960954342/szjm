package com.prolog.eis.controller.pick;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prolog.eis.dao.pick.PickingTaskMapper;
import com.prolog.eis.dto.pick.McsBcrPushDataDto;
import com.prolog.eis.dto.pick.McsResultDto;
import com.prolog.eis.dto.pick.PushDataDto;
import com.prolog.eis.dto.pick.WmsPickResultDto;
import com.prolog.eis.model.pick.PickingTask;
import com.prolog.eis.model.wms.LoginWmsResponse;
import com.prolog.eis.model.wms.WmsEisIdempotent;
import com.prolog.eis.service.login.WmsLoginService;
import com.prolog.eis.service.pick.EisIdempotentService;
import com.prolog.eis.service.pick.impl.McsPickTaskSend;
import com.prolog.eis.service.pick.impl.WmsPickTaskResultSend;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologDateUtils;
import com.prolog.eis.util.PrologHttpUtils;
import com.prolog.eis.util.PrologStringUtils;
import com.prolog.framework.utils.MapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "MCS拣货任务接口")
@RequestMapping("/api/v1/mcs/pick/")
public class McsBcrPushController {
    @Resource
    private EisIdempotentService eisIdempotentService;

    @Resource
    PickingTaskMapper pickingTaskMapper;

    @Autowired
    McsPickTaskSend mcsPickTaskSend;

    @Autowired
    WmsPickTaskResultSend wmsPickTaskResultSend;

    @ApiOperation(value = "MCS询问EIS容器走向MCS→EIS", notes = "MCS询问EIS容器走向MCS→EIS")
    @PostMapping("/pushData")
    public McsBcrPushDataDto pushData(@RequestBody String pushBcrData) throws Exception {
        JSONObject jsonObject=JSONObject.parseObject(pushBcrData);
        String id= jsonObject.getString("id");
        String containerId= jsonObject.getString("containerId");
        String billNoCurrent= jsonObject.getString("billNo");
        List<PickingTask> list=pickingTaskMapper.findByMap(MapUtils.put("barCode",containerId).getMap(),PickingTask.class);
        McsBcrPushDataDto mcsBcrPushDataDto=new McsBcrPushDataDto();
        mcsBcrPushDataDto.setCode("200");
        mcsBcrPushDataDto.setMessage(null);
        McsBcrPushDataDto.DataBean dataBean=new McsBcrPushDataDto.DataBean();
        mcsBcrPushDataDto.setSuccess(true);
        if(list.isEmpty()){
            /**
             * 去异常口
             */
            dataBean.setBcrPointValue("2");
        }else{

            PickingTask pickingTask=list.get(0);
            String billNo=pickingTask.getBillNo();
            if(billNo.equals(billNoCurrent)){
                dataBean.setBcrPointValue("1");
            }
            /**
             * 去异常口
             */
            dataBean.setBcrPointValue("2");


        }
        mcsBcrPushDataDto.setData(dataBean);

        return mcsBcrPushDataDto;
    }

    @ApiOperation(value = "分拨完成回传EIS MCS→EIS", notes = "分拨完成回传EIS MCS→EIS")
    @PostMapping("/pushCompleteData")
    public McsResultDto pushCompleteData(@RequestBody String pushBcrData) throws Exception {
        JSONObject jsonObject=JSONObject.parseObject(pushBcrData);
        String slidePort= jsonObject.getString("distributePort");
        String containerId= jsonObject.getString("containerId");
        List<PickingTask> list=pickingTaskMapper.findByMap(MapUtils.put("barCode",containerId).getMap(),PickingTask.class);
        McsResultDto mcsResultDto=new McsResultDto();
        mcsResultDto.setCode("200");
        WmsPickResultDto wmsPickResultDto=new WmsPickResultDto();
        wmsPickResultDto.setMessageID(PrologStringUtils.newGUID());
        if(list.isEmpty()){
            mcsResultDto.setSuccess(false);
            mcsResultDto.setMessage("查找不到条码对应的单号！");
        }else if(list.size()>1){
            mcsResultDto.setSuccess(false);
            mcsResultDto.setMessage("条码对应多条单号！");
        }else{
            mcsResultDto.setSuccess(true);
            PickingTask pickingTask=list.get(0);
            String billNo=pickingTask.getBillNo();
            wmsPickResultDto.setSize(1);
            List<WmsPickResultDto.DataBean> data=new ArrayList<>();
            WmsPickResultDto.DataBean dataBean=new WmsPickResultDto.DataBean();
            dataBean.setBarcode(containerId);
            dataBean.setBillno(billNo);
            dataBean.setPort(slidePort);
            dataBean.setPorttime(PrologDateUtils.getDate());
            data.add(dataBean);
            wmsPickResultDto.setData(data);
        }

        String resultJSON=wmsPickTaskResultSend.sendResultTask(wmsPickResultDto);
        JSONObject jsonObject1=JSONObject.parseObject(resultJSON);
        mcsResultDto.setMessage(jsonObject1.getString("message"));
        mcsResultDto.setCode("0".equals(jsonObject1.getString("code"))?"200":jsonObject1.getString("code"));
        mcsResultDto.setSuccess("0".equals(jsonObject1.getString("code")));
        return mcsResultDto;
    }


}
