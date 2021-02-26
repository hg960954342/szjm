package com.prolog.eis.service;


import com.prolog.eis.model.wms.InboundTask;

public interface InboundDataService {

    Integer findByContainerCode(String containerCode);

    void insertInboundTask(InboundTask datum);


    void insertEmptyBoxInStockTask(InboundTask datum);
}
