package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.service.impl.unbound.DetailDataBean;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface OutBoundTaskDetailMapper extends BaseMapper<OutboundTaskDetail>{

    @Results(id="map",value = {
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "lotId",  column = "lot_id"),
            @Result(property = "cqty",  column = "cqty"),
            @Result(property = "qty",  column = "qty"),
            @Result(property = "pickCode",  column = "pick_code"),
            @Result(property = "finishQty",  column = "finish_qty")
    })
    @Select(
           "SELECT\n" +
                   "  x.owner_id,\n" +
                   "\tx.item_id,\n" +
                   "\tx.lot_id,\n" +
                   "sum(x.qty) qty,\n" +
                   "sum(x.cqty) cqty,\n" +
                   "sum(x.finish_qty) finish_qty\n" +
                   "\t\n" +
                   "FROM\n" +
                   "\t(\n" +
                   "\t\tSELECT\n" +
                   "\t\t\td.item_id,\n" +
                   "\t\t\td.lot_id,\n" +
                   "\t\t\td.owner_id,\n" +
                   "\t\t\td.qty,\n" +
                   "\t\t\td.finish_qty,\n" +
                   "\t\t\tc.qty cqty\n" +
                   "\t\tFROM\n" +
                   "\t\t\toutbound_task_detail d,\n" +
                   "\t\t\toutbound_task t,\n" +
                   "\t\t\tcontainer_task_detail c\n" +
                   "\t\tWHERE\n" +
                   "\t\t\t  d.bill_no = t.bill_no\n" +
                   "\t\tAND c.item_id = d.item_id\n" +
                   "\t\tAND c.lot_id = d.lot_id\n" +
                   "    and c.owner_id=d.owner_id\n" +
                   "    and d.owner_id=t.owner_id\n" +
                   "\t\tAND c.seqno = d.seqno\n" +
                   "\t\t /*删选已经正在执行的托盘任务qty条件？*/\n" +
                   "    and d.bill_no in (#{bill_no_string})\n" +
                   "   \n" +
                   "\t) x\n" +
                   "GROUP BY\n" +
                   "\tx.item_id,\n" +
                   "\tx.lot_id,\n" +
                   "\tx.owner_id"

    )
     List<DetailDataBean> getOuntBoundDetailAll(@Param("bill_no_string") String billNoString);

    @ResultMap("map")
    @Select(
            "SELECT\n" +
                    "\tx.owner_id,\n" +
                    "\tx.item_id,\n" +
                    "\tx.lot_id,\n" +
                    "\tx.pick_code,\n" +
                    "\tsum(x.qty) qty,\n" +
                    "\tsum(x.cqty) cqty,\n" +
                    "\tsum(x.finish_qty) finish_qty\n" +
                    "FROM\n" +
                    "\t(\n" +
                    "\t\tSELECT\n" +
                    "\t\t\td.item_id,\n" +
                    "\t\t\td.lot_id,\n" +
                    "\t\t\td.owner_id,\n" +
                    "\t\t\td.qty,\n" +
                    "\t\t\td.finish_qty,\n" +
                    "\t\t\td.pick_code,\n" +
                    "\t\t\tc.qty cqty\n" +
                    "\t\tFROM\n" +
                    "\t\t\toutbound_task_detail d,\n" +
                    "\t\t\toutbound_task t,\n" +
                    "\t\t\tcontainer_task_detail c\n" +
                    "\t\tWHERE\n" +
                    "\t\t\td.bill_no = t.bill_no\n" +
                    "\t\tAND c.item_id = d.item_id\n" +
                    "\t\tAND c.lot_id = d.lot_id\n" +
                    "\t\tAND c.owner_id = d.owner_id\n" +
                    "\t\tAND d.owner_id = t.owner_id\n" +
                    "\t\tAND c.seqno = d.seqno\n" +
                    "\t\tAND d.pick_code = t.pick_code \n" +
                    "   AND d.bill_no IN (#{bill_no_string})  \n" +
                    "\t) x\n" +
                    "GROUP BY\n" +
                    "\tx.item_id,\n" +
                    "\tx.lot_id,\n" +
                    "\tx.owner_id,\n" +
                    "\tx.pick_code"
       )
    List<DetailDataBean> getOuntBoundDetailPickCodeAll(@Param("bill_no_string") String billNoString);

    @Results(id="ContainerTaskDetail" , value= {
            @Result(property = "id",  column = "ID"),
            @Result(property = "containerCode",  column = "container_code"),
            @Result(property = "billNo",  column = "bill_no"),
            @Result(property = "seqNo",  column = "seqno"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "lotId",  column = "lot_id"),
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "qty",  column = "qty"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "endTime",  column = "end_time")
    })
    @Select("SELECT\n" +
            "\tx.seqno,\n" +
            "\tx.bill_no,\n" +
            "\tx.item_id,\n" +
            "\tx.owner_id,\n" +
            "\tx.lot_id,\n" +
            "\tt.container_code,\n" +
            "\tx.qty - y.qty - x.finish_qty qty,\n" +
            "\t NOW() create_time \n" +
            "FROM\n" +
            "\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\t*\n" +
            "\t\tFROM\n" +
            "\t\t\toutbound_task_detail\n" +
            "\t\tWHERE\n" +
            "\t\t\tbill_no IN (#{bill_no_string}\n" +
            ") x,\n" +
            "\t\t\tcontainer_task_detail y,\n" +
            "\t\t\tcontainer_task t\n" +
            "\t\tWHERE\n" +
            "\t\t\ty.owner_id = x.owner_id\n" +
            "\t\tAND y.item_id = x.item_id\n" +
            "\t\tAND y.lot_id = x.lot_id\n" +
            "\t\tAND t.item_id = y.item_id\n" +
            "\t\tAND t.owner_id = y.owner_id\n" +
            "\t\tAND t.lot_id = y.lot_id")
    List<ContainerTaskDetail> getOutBoundContainerTaskDetail(String billNoString);



}
