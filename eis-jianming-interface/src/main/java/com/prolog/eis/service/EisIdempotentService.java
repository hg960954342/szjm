package com.prolog.eis.service;

import com.prolog.eis.model.wms.WmsEisIdempotent;

import java.util.List;

public interface EisIdempotentService {


    List<WmsEisIdempotent> queryRejsonById(String messageId);

    void insertReport(WmsEisIdempotent wmsEisIdempotent);
}
