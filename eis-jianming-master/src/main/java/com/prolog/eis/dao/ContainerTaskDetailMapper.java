package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.service.impl.unbound.entity.CheckOutResponse;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.ResultHandler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ContainerTaskDetailMapper extends BaseMapper<ContainerTaskDetail> {


    @Results({
            @Result(property = "containerCode", column = "container_code"),
            @Result(property = "billNo", column = "bill_no"),
            @Result(property = "type", column = "task_type"),
            @Result(property = "consignor", column = "owner_id"),
            @Result(property = "seqNo", column = "seqno"),
            @Result(property = "itemId", column = "item_id"),
            @Result(property = "lotId", column = "lot_id"),
            @Result(property = "qty", column = "qty"),
            @Result(property = "agvLoc", column = "source")
    })
    @ResultType(Map.class)
    @Select("select d.*,t.task_type,t.source from container_task_detail d ,container_task t " +
            "where d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    void getReportData(@Param("containerCode") String containerCode, ResultHandler handler);

    @Select("select d.bill_no billno,d.lot_id lotid,d.seqno,d.item_id itemid,d.ownerid,d.qty,t.container_code containercode,ibk.task_type type,t.source agvloc from container_task_detail d ,container_task t, inbound_task ibk where ibk.bill_no=d.bill_no and d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map<String, Object>> getInBoundReport(@Param("containerCode") String containerCode);


    @Results({
            @Result(property = "containerCode", column = "container_code"),
            @Result(property = "billNo", column = "bill_no"),
            @Result(property = "taskType", column = "task_type"),
            @Result(property = "ownerId", column = "owner_id"),
            @Result(property = "seqNo", column = "seqno"),
            @Result(property = "itemId", column = "item_id"),
            @Result(property = "itemName", column = "item_name"),
            @Result(property = "qty", column = "qty"),
            @Result(property = "agvLoc", column = "source")
    })
    @Select("select d.*, t.task_type,t.source from container_task_detail d ,container_task t " +
            "where d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map> getData(@Param("containerCode") String containerCode);


    @Results({
            @Result(property = "billNo", column = "bill_no"),
            @Result(property = "type", column = "type"),
            @Result(property = "details", column = "item_id=item_id,lot_id=lot_id,bill_no=bill_no",
                    many = @Many(select = "com.prolog.eis.dao.ContainerTaskDetailMapper.getCheckDetail")),
    })
    @Select("select bill_no,\"3\" type,d.item_id,d.lot_id from outbound_task_detail d where d.bill_no = #{billNo} GROUP BY item_id,lot_id")
    List<CheckOutResponse.DataBean> getCheckReportData(@Param("billNo") String billNo);


    @Results({
            @Result(property = "seqNo", column = "seqno"),
            @Result(property = "itemId", column = "item_id"),
            @Result(property = "lotId", column = "lot_id"),
            @Result(property = "containerCode", column = "CONTAINER_NO"),
            @Result(property = "qty", column = "QTY")
    })
    @Select("select d.seqno,s.item_id,s.lot_id,s.CONTAINER_NO,s.WEIGHT qty from outbound_task_detail d ,sx_store s\n" +
            "where d.item_id=s.item_id and d.lot_id=s.lot_id and d.item_id=#{item_id} and d.lot_id=#{lot_id} and d.bill_no=#{bill_no}")
    List<CheckOutResponse.DataBean.DetailsBean> getCheckDetail(@Param("item_id") String itemId, @Param("lot_id") String lotId,@Param("bill_no") String billNo);

    @Select("select sum(qty) from container_task_detail where container_code = #{containerCode}")
    BigDecimal queryPickQtyByConcode(String containerCode);
}