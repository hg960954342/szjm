package com.prolog.eis.service.impl;

import com.prolog.eis.dao.OutboundTestDataMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.service.OutboundTestDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutboundTestDataServiceImpl implements OutboundTestDataService {

    @Autowired
    private OutboundTestDataMapper outboundTestDataMapper;

    @Override
    public void insertOutboundTask(OutboundTask datum) {
        outboundTestDataMapper.insertOutboundTask(datum);
    }

    @Override
    public void insertOutboundTaskDetail(OutboundTaskDetail detail) {

        outboundTestDataMapper.insertOutboundTaskDetail(detail);
    }

    @Override
    public void insertMoveTaskDetail(OutboundTaskDetail detail) {

        outboundTestDataMapper.insertMoveTaskDetail(detail);
    }

    @Override
    public void insertMoveTask(OutboundTask datum) {

        outboundTestDataMapper.insertMoveTask(datum);
    }
}
