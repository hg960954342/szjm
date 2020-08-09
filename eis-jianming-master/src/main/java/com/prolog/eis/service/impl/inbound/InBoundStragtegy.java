package com.prolog.eis.service.impl.inbound;

import com.prolog.eis.model.wms.InboundTask;
import com.prolog.eis.model.wms.OutboundTask;

/**
 * 出库执行策略接口
 */
public interface InBoundStragtegy {








    public void inbound(InboundTask inboundTask);

}
