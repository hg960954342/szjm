package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.ResultContainer;
import com.prolog.eis.service.impl.unbound.entity.CheckOutResponse;

import java.util.List;
import java.util.Map;

public interface ContainerTaskDetailService {

//    List<ContainerTaskDetail> selectByContainerCode(String containerCode);
    List<Map<String,Object>> getInBoundReportByContainerCode(String containerCode);
    List<Map<String,Object>> selectByContainerCode(String containerCode);

    //根据订单号查询
    List<ContainerTaskDetail> selectByBillNo(String billNo);

    //
    public List<CheckOutResponse.DataBean> getCheckReportData(String billNo);
}
