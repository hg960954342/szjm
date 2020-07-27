package com.prolog.eis.controller.report;

import com.alibaba.fastjson.JSON;
import com.prolog.eis.dto.eis.WcsPublicResponseDto;
import com.prolog.eis.dto.mcs.CodeReadeEquipStatusDto;
import com.prolog.eis.dto.mcs.McsEquipStatusDto;
import com.prolog.eis.util.PrologApiJsonHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author panteng
 * @description: 设备数据上报
 * @date 2020/5/21 9:45
 */
@RestController
@Api(tags = "设备数据上报")
@RequestMapping("/api/v1/master/equip")
public class EquipDataReortController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquipDataReortController.class);

    @ApiOperation(value = "MCS设备状态上报", notes = "MCS设备状态上报")
    @PostMapping("/mcsReport")
    public WcsPublicResponseDto mcsReport(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        List<McsEquipStatusDto> dtos = helper.getObjectList("carryList", McsEquipStatusDto.class);
        if (CollectionUtils.isEmpty(dtos)) {
            return WcsPublicResponseDto.newInstance(false, "数据为空", null);
        }
        Map<String, String> mcsEquipStatusDtoMap = dtos.stream().filter(t -> StringUtils.isNotEmpty(t.getPlcId())).collect(Collectors.toMap(McsEquipStatusDto::getPlcId,
                v -> JSON.toJSONString(v), (K1, K2) -> K1));

        return WcsPublicResponseDto.newInstance(true, "请求成功", null);
    }

    @ApiOperation(value = "MCS读码器及称重设备状态上报", notes = "MCS读码器及称重设备状态上报")
    @PostMapping("/codeReadeReport")
    public WcsPublicResponseDto codeReadeReport(@RequestBody String json) throws Exception {
        PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(json);
        //HashSet 存储
        List<CodeReadeEquipStatusDto> dtos = helper.getObjectList("carryList", CodeReadeEquipStatusDto.class);
        if (CollectionUtils.isEmpty(dtos)) {
            return WcsPublicResponseDto.newInstance(false, "未获取到数据", null);
        }
        Map<String, String> mcsEquipStatusDtoMap = dtos.stream().filter(t -> StringUtils.isNotEmpty(t.getDeviceNo())).collect(Collectors.toMap(CodeReadeEquipStatusDto::getDeviceNo,
                v -> JSON.toJSONString(v), (K1, K2) -> K1));

        return WcsPublicResponseDto.newInstance(true, "请求成功", null);
    }
}
