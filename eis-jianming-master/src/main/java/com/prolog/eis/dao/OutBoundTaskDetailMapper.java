package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.service.impl.unbound.DetailDataBean;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OutBoundTaskDetailMapper extends BaseMapper<OutboundTaskDetail> {

    @Results(id="map",value = {
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "itemName",  column = "item_name"),
            @Result(property = "lotId",  column = "lot_id"),
            @Result(property = "cqty",  column = "cqty"),
            @Result(property = "qty",  column = "qty"),
            @Result(property = "pickCode",  column = "pick_code"),
            @Result(property = "finishQty",  column = "finish_qty"),
            @Result(property = "billNo",  column = "bill_no")

    })
    @Select(
            " SELECT \n" +
                    "                  sum(d.qty) qty, \n" +
                    "                  sum(d.finish_qty) finish_qty, \n" +
                    "                  sum(d.qty_) cqty ,\n" +
                    "                  d.owner_id,d.item_id,d.lot_id,d.item_name,d.pick_code,group_concat(d.bill_no) bill_no\n" +
                    "                FROM  \n" +
                    "               (select d.qty,d.finish_qty,c.qty qty_, d.owner_id,d.item_id,d.lot_id,d.item_name,d.pick_code,d.bill_no\n" +
                    "                  from outbound_task_detail d   \n" +
                    "                INNER JOIN outbound_task t ON t.bill_no = d.bill_no and d.bill_no IN (${bill_no_string} ) \n" +
                    "                left JOIN container_task_detail c ON c.bill_no = d.bill_no )d GROUP BY d.owner_id,d.item_id,d.lot_id,d.item_name,d.pick_code"
    )
    List<DetailDataBean> getOuntBoundDetailAll(@Param("bill_no_string") String billNoString);



    @Results(id="ContainerTaskDetail" , value= {
            @Result(property = "id",  column = "ID"),
            @Result(property = "containerCode",  column = "container_code"),
            @Result(property = "qty",  column = "qty"),
            @Result(property = "billNo",  column = "bill_no"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "lotId",  column = "lot_id"),
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "createTime",  column = "create_time"),
            @Result(property = "seqNo",  column = "seqno"),
            @Result(property = "endTime",  column = "end_time")
    })
   @Select("SELECT\n" +
           "\tx.seqno,\n" +
           "\tx.bill_no,\n" +
           "\tx.item_id,\n" +
           "\tx.owner_id,\n" +
           "\tx.lot_id,\n" +
           "\t#{container_code} container_code,\n" +
           "\tx.qty - x.finish_qty qty,\n" +
           "\tNOW() create_time,\n" +
           "\tNULL end_time\n" +
           "FROM\n" +
           "\t(\n" +
           "\t\tSELECT\n" +
           "\t\t\t*\n" +
           "\t\tFROM\n" +
           "\t\t\toutbound_task_detail\n" +
           "\t\tWHERE\n" +
           "\t\t\tbill_no IN (${billNoString})\n" +
           "\t) x")
    List<ContainerTaskDetail> getOutBoundContainerTaskDetail(@Param("billNoString")String billNoString,@Param("container_code")String containerCode);

    /**
     * 获取订单池中的商品总数
     * @param billNoString
     * @return
     */
  @Select("SELECT\n" +
          "\tsum(count)\n" +
          "FROM\n" +
          "\t(\n" +
          "\t\tSELECT\n" +
          "\t\t\towner_id,\n" +
          "\t\t\titem_id,\n" +
          "\t\t\tlot_id,\n" +
          "\t\t\tcount(*) count\n" +
          "\t\tFROM\n" +
          "\t\t\toutbound_task_detail\n" +
          "\t\tWHERE\n" +
          "\t\t\tbill_no IN (${bill_no_string})\n" +
          "\t\tGROUP BY\n" +
          "\t\t\towner_id,\n" +
          "\t\t\titem_id,\n" +
          "\t\t\tlot_id\n" +
          "\t)")
    float getPoolItemCount(@Param("bill_no_string") String billNoString);
    /**
     * 获取给定订单的和订单池比较 相同的商品总数目
     * @param billNoString
     * @param billNo
     * @return
     */
 @Select("SELECT\n" +
         "\tsum(z.count) count\n" +
         "FROM\n" +
         "\t(\n" +
         "\t\tSELECT\n" +
         "\t\t\ty.item_id,\n" +
         "\t\t\ty.owner_id,\n" +
         "\t\t\ty.lot_id,\n" +
         "\t\t\tcount(*) count\n" +
         "\t\tFROM\n" +
         "\t\t\t(\n" +
         "\t\t\t\tSELECT\n" +
         "\t\t\t\t\td.item_id,\n" +
         "\t\t\t\t\td.lot_id,\n" +
         "\t\t\t\t\td.owner_id\n" +
         "\t\t\t\tFROM\n" +
         "\t\t\t\t\toutbound_task_detail d,\n" +
         "\t\t\t\t\t(\n" +
         "\t\t\t\t\t\tSELECT\n" +
         "\t\t\t\t\t\t\towner_id,\n" +
         "\t\t\t\t\t\t\titem_id,\n" +
         "\t\t\t\t\t\t\tlot_id,\n" +
         "\t\t\t\t\t\t\tcount(*) count\n" +
         "\t\t\t\t\t\tFROM\n" +
         "\t\t\t\t\t\t\toutbound_task_detail\n" +
         "\t\t\t\t\t\tWHERE\n" +
         "\t\t\t\t\t\t\tbill_no IN (${bill_no_string})\n" +
         "\t\t\t\t\t\tGROUP BY\n" +
         "\t\t\t\t\t\t\towner_id,\n" +
         "\t\t\t\t\t\t\titem_id,\n" +
         "\t\t\t\t\t\t\tlot_id\n" +
         "\t\t\t\t\t) x\n" +
         "\t\t\t\tWHERE\n" +
         "\t\t\t\t\td.bill_no =#{bill_no}\n" +
         "\t\t\t\tAND x.owner_id = d.owner_id\n" +
         "\t\t\t\tAND x.lot_id = d.lot_id\n" +
         "\t\t\t\tAND x.item_id = d.item_id\n" +
         "\t\t\t) y\n" +
         "\t\tGROUP BY\n" +
         "\t\t\ty.item_id,\n" +
         "\t\t\ty.owner_id,\n" +
         "\t\t\ty.lot_id\n" +
         "\t)z\n")
 float getPoolSameItemCount(@Param("bill_no_string") String billNoString, @Param("bill_no")String billNo);



}
