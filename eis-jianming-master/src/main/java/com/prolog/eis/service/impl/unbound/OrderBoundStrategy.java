package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.dao.*;
import com.prolog.eis.dao.baseinfo.PortInfoMapper;
import com.prolog.eis.model.eis.PortInfo;
import com.prolog.eis.model.wms.*;
import com.prolog.eis.util.PrologCoordinateUtils;
import com.prolog.eis.util.PrologLocationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
 * 订单出库 未指定拣选站
 */
@Component(OutBoundType.TASK_TYPE+1+OutBoundType.IF_SfReq+0)
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class OrderBoundStrategy extends DefaultOutBoundPickCodeStrategy {




    @Override
    public void unbound(OutboundTask outboundTask) {
        //获取出库明细
        List<DetailDataBean> list = similarityDataEntityListLoad.getOutDetailList(this.getClass());

        //1.获取能够作业的拣选站
        List<PickStation> listPickStation = getAvailablePickStation();
        if (listPickStation.size() < 1) {
            log.info("未找能够作业的拣选站！");
            return;
        }//没有拣选站 结束当前


        for (DetailDataBean detailDataBeand : list) {
            PickStation pickStation = listPickStation.get(0);//   取第一个可用的拣选站
            String pickCode = pickStation.getStationNo();
            unbound(detailDataBeand,pickCode);
        }


    }

}