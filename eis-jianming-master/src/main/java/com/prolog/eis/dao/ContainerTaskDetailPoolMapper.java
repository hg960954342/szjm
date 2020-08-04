package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.ContainerTaskDetailPool;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContainerTaskDetailPoolMapper extends BaseMapper<ContainerTaskDetailPool> {





    @Select("select count(*) from (select a.* from outbound_task_detail_pool a,\n" +
            "(select * from outbound_task_detail \n" +
            "  where bill_no=#{billNo}  )b where a.item_id=b.item_id and a.lot_id=b.lot_id ) ")
    long getContainerTaskDetailPoolCountByBillNo(@Param("billNo") String billNo);


}
