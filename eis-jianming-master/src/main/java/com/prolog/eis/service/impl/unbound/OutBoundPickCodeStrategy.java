package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.model.wms.OutboundTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单出库  指定拣选站
 *
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+1)
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class OutBoundPickCodeStrategy extends DefaultOutBoundPickCodeStrategy {



    @Override
    public void unbound(OutboundTask outboundTask)  {}





}
