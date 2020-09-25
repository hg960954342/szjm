package com.prolog.eis.service.impl.inbound;

import com.prolog.eis.model.wms.InboundTask;

/**
 * 出库执行策略接口
 */
public interface InBoundStragtegy {


     void inbound(InboundTask inboundTask);

}
