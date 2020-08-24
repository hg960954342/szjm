package com.prolog.eis.service;

import com.prolog.eis.service.impl.ResultAgvDto;

import java.util.List;

public interface EisSendRcsTaskService {
    public void updateAgvTask( List<ResultAgvDto> list);
}
