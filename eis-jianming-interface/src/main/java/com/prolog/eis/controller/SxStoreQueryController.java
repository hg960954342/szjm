package com.prolog.eis.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "EIS查询接口")
@RequestMapping("/api/v1/query")
public class SxStoreQueryController {


    @ApiOperation(value = "查询库存接口", notes = "查询库存接口")
    @PostMapping("/querySxStore")
    public void querySxStore(@RequestBody String querySxStoreStr) throws Exception {


    }




}
