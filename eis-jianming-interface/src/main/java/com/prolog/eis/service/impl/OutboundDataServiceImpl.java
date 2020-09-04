package com.prolog.eis.service.impl;

import com.prolog.eis.dao.OutboundDataMapper;
import com.prolog.eis.model.wms.CheckStock;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
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
    public void insertOutboundTaskDetail(OutboundTaskDetail detail) {

        outboundDataMapper.insertOutboundTaskDetail(detail);
    }

    @Override
    public void insertMoveTaskDetail(OutboundTaskDetail detail) {

        outboundDataMapper.insertMoveTaskDetail(detail);
    }

    @Override
    public void insertCheckOutTaskDetail(OutboundTaskDetail detail) {
        outboundDataMapper.insertCheckOutTaskDetail(detail);
    }

    @Override
    public void insertEmptyBoxOutStockTask(OutboundTask datum) {
        outboundDataMapper.insertEmptyBoxOutStockTask(datum);
    }

    @Override
    public void insertEmptyBoxOutStockTaskDetail(OutboundTaskDetail detail) {
        outboundDataMapper.insertEmptyBoxOutStockTaskDetail(detail);
    }

    @Override
    public void insertMoveTask(OutboundTask datum) {

        outboundDataMapper.insertMoveTask(datum);
    }




}
