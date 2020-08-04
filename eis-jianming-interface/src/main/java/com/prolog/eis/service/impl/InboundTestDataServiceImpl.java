package com.prolog.eis.service.impl;


import com.prolog.eis.dao.InboundTestDataMapper;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.InboundTestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class InboundTestDataServiceImpl implements InboundTestDataService{

    @Autowired
    private InboundTestDataMapper inboundTestDataMapper;


    @Override
    public void insertInboundTask(InboundTask datum) {
        inboundTestDataMapper.insertInboundTask(datum);

    }


}
