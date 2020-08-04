package com.prolog.eis.service.impl;


import com.prolog.eis.dao.EisIdempotentMapper;
import com.prolog.eis.model.wms.WmsEisIdempotent;
import com.prolog.eis.service.EisIdempotentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EisIdempotentServiceImpl implements EisIdempotentService{

    @Autowired
    private EisIdempotentMapper eisIdempotentMapper;


    @Override
    public List<WmsEisIdempotent> queryRejsonById(String messageId) {
        return eisIdempotentMapper.queryRejsonById(messageId);

    }

    @Override
    public void insertReport(WmsEisIdempotent wmsEisIdempotent) {
        eisIdempotentMapper.insertReport(wmsEisIdempotent);
    }

}
