package com.prolog.eis.dao;

import com.prolog.eis.model.wms.ContainerTaskDetail;
import com.prolog.eis.model.wms.ResultContainer;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ContainerTaskDetailMapper extends BaseMapper<ContainerTaskDetail> {


    @Select("select d.bill_no billno,d.lot_id lotid,d.seqno,d.item_id itemid,d.ownerid,d.qty,t.container_code " +
            "containercode,task_type tasktype,t.source agvloc from container_task_detail d ,container_task t " +
            "where d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map<String,Object>> getReportData(@Param("containerCode") String containerCode);

    @Select("select d.bill_no billno,d.lot_id lotid,d.seqno,d.item_id itemid,d.ownerid,d.qty,t.container_code containercode,ibk.task_type type,t.source agvloc from container_task_detail d ,container_task t, inbound_task ibk where ibk.bill_no=d.bill_no and d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map<String, Object>> getInBoundReport(@Param("containerCode")String containerCode);


    @Results({
            @Result(property = "containerCode",  column = "container_code"),
            @Result(property = "billNo",  column = "bill_no"),
            @Result(property = "taskType",column = "task_type"),
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "seqNo",  column = "seqno"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "qty",  column = "qty"),
            @Result(property = "agvLoc",  column = "source")
    })
    @Select("select d.*, t.task_type,t.source from container_task_detail d ,container_task t " +
            "where d.container_code=t.container_code and t.container_code=#{containerCode}; ")
    List<Map> getData(@Param("containerCode") String containerCode);




    @Results({
            @Result(property = "billno",  column = "bill_no"),
            @Result(property = "tasktype",  column = "task_type"),
            @Result(property = "details", column="container_code",
                    many=@Many(select = "com.prolog.eis.dao.ContainerTaskDetailMapper.getCheckDetail")),
    })
    @Select("select  x.bill_no,x.task_type,GROUP_CONCAT(x.container_code) container_code from (\n" +
            "SELECT d.bill_no,\n" +
            " t.task_type,t.container_code FROM\n" +
            " container_task_detail d INNER join container_task t ON t.container_code = d.container_code  and d.bill_no=#{billNo})x\n" +
            " GROUP BY x.bill_no,x.task_type")
    List<ResultContainer.DataBean> getCheckReportData(@Param("billNo")String billNo);



    @Results({
            @Result(property = "seqno",  column = "seqno"),
            @Result(property = "itemid",  column = "item_id"),
            @Result(property = "lotid", column="lot_id"),
            @Result(property = "containercode", column="container_code"),
            @Result(property = "qty", column="qtyy"),
    })
    @SelectProvider(type = SQLBuilder.class,method="getCheckDetailSQL")
    List<ResultContainer.DataBean.DetailsBean> getCheckDetail(@Param("container_code") String container_code);
}
class  SQLBuilder{



    public String getCheckDetailSQL(String container_code){
        return "select  x.* from (\n" +
                "\n" +
                "select t.qty qtyy,d.* from \tcontainer_task_detail d INNER join container_task t ON t.container_code = d.container_code \r\n" +
                "where  d.container_code in ("+container_code+") )x";
    }
}