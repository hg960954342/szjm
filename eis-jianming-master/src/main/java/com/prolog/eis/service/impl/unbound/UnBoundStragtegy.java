package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.model.wms.OutboundTask;

/**
 * 出库执行策略接口
 */
public interface UnBoundStragtegy {


    void unbound(OutboundTask outboundTask);

}
