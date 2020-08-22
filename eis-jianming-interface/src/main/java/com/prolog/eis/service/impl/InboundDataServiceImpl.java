package com.prolog.eis.service.impl;


import com.prolog.eis.dao.InboundDataMapper;
import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.service.InboundDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InboundDataServiceImpl implements InboundDataService {

    @Autowired
    private InboundDataMapper inboundDataMapper;


    @Override
    public void insertInboundTask(InboundTask datum) {
        inboundDataMapper.insertInboundTask(datum);

    }

    @Override
    public void insertEmptyBoxInStockTask(InboundTask datum) {
        inboundDataMapper.insertEmptyBoxInStockTask(datum);
    }


}
