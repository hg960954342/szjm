package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTaskDetail;

import java.util.List;

public interface ContainerTaskDetailService {
    List<ContainerTaskDetail> selectByContainerCode(int containerCode);

    //根据订单号查询
    List<ContainerTaskDetail> selectByBillNo(String billNo);
}
