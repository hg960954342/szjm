package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.ContainerTaskDetailPool;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.framework.dao.helper.SqlFactory;
import com.prolog.framework.dao.helper.SqlMethod;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ContainerTaskDetailPoolMapper extends BaseMapper<ContainerTaskDetailPool> {


    /**
     * 获取订单池中共有多少种商品
     * @param billNo
     * @return
     */
    @Select(
           " SELECT \r\n"+
           " count(*) \r\n" +
    "FROM \r\n"+
    "(\r\n"+
            "SELECT \r\n"+
            " detail.* \r\n"+
            "  FROM \r\n"+
            "  outbound_task_detail_pool pool \r\n"+
            "   LEFT JOIN outbound_task_detail detail ON detail.bill_no = pool.bill_no \r\n"+
            "  AND detail.pick_code = pool.pick_code \r\n"+
            "  WHERE \r\n"+
            "   pool.bill_no =#{billNo} \r\n"+
                   " )a \r\n"+
                   "GROUP BY \r\n"+
                   " a.item_id, \r\n"+
                   "a.lot_id"
           )
    long getContainerTaskDetailPoolCountByBillNo(@Param("billNo") String billNo);


    @Results(id="OutboundTask" , value= {
            @Result(property = "id",  column = "id"),
            @Result(property = "billNo",  column = "bill_no"),
            @Result(property = "wmsPush",  column = "wms_push"),
            @Result(property = "reBack",  column = "reback"),
            @Result(property = "emptyContainer",  column = "empty_container"),
            @Result(property = "taskType",  column = "task_type"),
            @Result(property = "sfReq",  column = "sfreq"),
            @Result(property = "pickCode",  column = "pick_code"),
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "endTime",  column = "end_time")    })
    /**
     * 获取所有超时出库订单 按降序排序
     * @return
     */
    @Select("select * from (select TIMESTAMPDIFF(MINUTE,NOW(),t.create_time) overtime,t.* from outbound_task t where t.task_type=1 )a \r\n"+
                   "WHERE a.overtime>#{overTime} ORDER BY a.overtime")
    List<OutboundTask> getOutBoudTaskOverTime(@Param("overTime") long overTime);

}
