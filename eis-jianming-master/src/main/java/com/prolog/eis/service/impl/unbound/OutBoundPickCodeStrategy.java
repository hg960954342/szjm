package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
