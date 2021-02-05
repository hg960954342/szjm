package com.prolog.eis.service;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.InboundTaskDecimal;


public interface InboundDataService {

    Integer findByContainerCode(String containerCode);

    void insertInboundTask(InboundTaskDecimal datum);


    void insertEmptyBoxInStockTask(InboundTaskDecimal datum);
}
