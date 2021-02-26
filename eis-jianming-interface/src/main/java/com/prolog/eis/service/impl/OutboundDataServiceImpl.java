package com.prolog.eis.service.impl;

import com.prolog.eis.dao.OutboundDataMapper;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetailDto;
import com.prolog.eis.service.OutboundDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutboundDataServiceImpl implements OutboundDataService {

    @Autowired
    private OutboundDataMapper outboundDataMapper;

    @Override
    public void insertOutboundTask(OutboundTask datum) {
        outboundDataMapper.insertOutboundTask(datum);
    }

    @Override
    public void insertOutboundTaskDetail(OutboundTaskDetailDto detail) {

        outboundDataMapper.insertOutboundTaskDetail(detail);
    }

    @Override
    public void insertMoveTaskDetail(OutboundTaskDetailDto detail) {

        outboundDataMapper.insertMoveTaskDetail(detail);
    }

    @Override
    public void insertCheckOutTaskDetail(OutboundTaskDetailDto detail) {
        outboundDataMapper.insertCheckOutTaskDetail(detail);
    }

    @Override
    public void insertEmptyBoxOutStockTask(OutboundTask datum) {
        outboundDataMapper.insertEmptyBoxOutStockTask(datum);
    }

    @Override
    public void insertEmptyBoxOutStockTaskDetail(OutboundTaskDetailDto detail) {
        outboundDataMapper.insertEmptyBoxOutStockTaskDetail(detail);
    }

    @Override
    public void insertMoveTask(OutboundTask datum) {

        outboundDataMapper.insertMoveTask(datum);
    }




}
