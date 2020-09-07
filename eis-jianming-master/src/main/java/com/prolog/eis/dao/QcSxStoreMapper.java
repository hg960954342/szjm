package com.prolog.eis.dao;

import com.prolog.eis.dto.eis.SxStoreDto;
import com.prolog.eis.dto.eis.YiWeiCountDto;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.wms.OutboundTaskDetail;
import com.prolog.eis.service.impl.unbound.entity.CheckOutResult;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface QcSxStoreMapper extends BaseMapper<SxStore>{

	@Select("select t.ID id,t.CONTAINER_NO containerNo,t.CONTAINER_SUB_NO containerSubNo,t.STORE_LOCATION_ID storeLocationId,\r\n" + 
			"	t.TASK_TYPE taskType,t.TASK_PROPERTY1 taskProperty1,t.TASK_PROPERTY2 taskProperty2,t.BUSINESS_PROPERTY1 businessProperty1,\r\n" + 
			"	t.BUSINESS_PROPERTY2 businessProperty2,t.BUSINESS_PROPERTY3 businessProperty3,t.BUSINESS_PROPERTY4 businessProperty4,\r\n" + 
			"	t.BUSINESS_PROPERTY5 businessProperty5,t.STORE_STATE storeState,t.IN_STORE_TIME inStoreTime,t.HOISTER_NO hoisterNo,\r\n" + 
			"	t.CAR_NO carNo,t.TASK_ID taskId,t.EMPTY_PALLET_COUNT emptyPalletCount,t.source_location_id sourceLocationId,t.CREATE_TIME createTime,\r\n" + 
			"	tl.layer,tl.x,tl.y\r\n" + 
			"from sx_store t\r\n" + 
			"left join sx_store_location tl on t.STORE_LOCATION_ID = tl.id\r\n" + 
			"where ${containerSubsStr}\r\n" + 
			"order by tl.dept_num")
	List<SxStoreDto> findSxStoreByContainerSubNo(@Param("containerSubsStr")String containerSubsStr);
	
	@Select("select t.ID id,t.CONTAINER_NO containerNo,t.CONTAINER_SUB_NO containerSubNo,t.STORE_LOCATION_ID storeLocationId,\r\n" + 
			"	t.TASK_TYPE taskType,t.TASK_PROPERTY1 taskProperty1,t.TASK_PROPERTY2 taskProperty2,t.BUSINESS_PROPERTY1 businessProperty1,\r\n" + 
			"	t.BUSINESS_PROPERTY2 businessProperty2,t.BUSINESS_PROPERTY3 businessProperty3,t.BUSINESS_PROPERTY4 businessProperty4,\r\n" + 
			"	t.BUSINESS_PROPERTY5 businessProperty5,t.STORE_STATE storeState,t.IN_STORE_TIME inStoreTime,t.HOISTER_NO hoisterNo,\r\n" + 
			"	t.CAR_NO carNo,t.TASK_ID taskId,t.EMPTY_PALLET_COUNT emptyPalletCount,t.source_location_id sourceLocationId,t.CREATE_TIME createTime,\r\n" + 
			"	tl.layer,tl.x,tl.y\r\n" + 
			"from sx_store t\r\n" + 
			"left join sx_store_location tl on t.STORE_LOCATION_ID = tl.id\r\n" + 
			"where ${containersStr}\r\n" +
			"order by tl.dept_num")
	List<SxStoreDto> findSxStoreByContainerNo(@Param("containersStr")String containersStr);
	
	@Select("select t.ID id,t.CONTAINER_NO containerNo,t.CONTAINER_SUB_NO containerSubNo,t.STORE_LOCATION_ID storeLocationId,\r\n" + 
			"t.TASK_TYPE taskType,t.TASK_PROPERTY1 taskProperty1,t.TASK_PROPERTY2 taskProperty2,t.BUSINESS_PROPERTY1 businessProperty1,\r\n" + 
			"t.BUSINESS_PROPERTY2 businessProperty2,t.BUSINESS_PROPERTY3 businessProperty3,t.BUSINESS_PROPERTY4 businessProperty4,\r\n" + 
			"t.BUSINESS_PROPERTY5 businessProperty5,t.STORE_STATE storeState,t.IN_STORE_TIME inStoreTime,t.HOISTER_NO hoisterNo,\r\n" + 
			"t.CAR_NO carNo,t.TASK_ID taskId,t.EMPTY_PALLET_COUNT emptyPalletCount,t.source_location_id sourceLocationId,t.CREATE_TIME createTime,\r\n" + 
			"tl.layer,tl.x,tl.y \r\n" + 
			"from sx_store t\r\n" + 
			"left join sx_store_location tl on t.STORE_LOCATION_ID = tl.id \r\n" + 
			"left join sx_store_location_group g on tl.store_location_group_id = g.id \r\n" + 
			"left join wms_outbound_task ot on t.CONTAINER_NO = ot.container_code\r\n" + 
			"where  tl.layer = #{layer} and t.TASK_TYPE = -1 and t.STORE_STATE = 20 and ot.id is null \r\n" +
			"and g.ASCENT_LOCK_STATE = 0 and g.IS_LOCK = 0\r\n" + 
			"order by tl.dept_num asc")
	List<SxStoreDto> findEmptySxStoreByLayer(@Param("layer")int layer);
	
	@Select("select t.ID id,t.CONTAINER_NO containerNo,t.CONTAINER_SUB_NO containerSubNo,t.STORE_LOCATION_ID storeLocationId,\r\n" + 
			"t.TASK_TYPE taskType,t.TASK_PROPERTY1 taskProperty1,t.TASK_PROPERTY2 taskProperty2,t.BUSINESS_PROPERTY1 businessProperty1,\r\n" + 
			"t.BUSINESS_PROPERTY2 businessProperty2,t.BUSINESS_PROPERTY3 businessProperty3,t.BUSINESS_PROPERTY4 businessProperty4,\r\n" + 
			"t.BUSINESS_PROPERTY5 businessProperty5,t.STORE_STATE storeState,t.IN_STORE_TIME inStoreTime,t.HOISTER_NO hoisterNo,\r\n" + 
			"t.CAR_NO carNo,t.TASK_ID taskId,t.EMPTY_PALLET_COUNT emptyPalletCount,t.source_location_id sourceLocationId,t.CREATE_TIME createTime,\r\n" + 
			"tl.layer,tl.x,tl.y \r\n" + 
			"from sx_store t\r\n" + 
			"left join sx_store_location tl on t.STORE_LOCATION_ID = tl.id \r\n" + 
			"left join sx_store_location_group g on tl.store_location_group_id = g.id \r\n" + 
			"left join wms_outbound_task ot on t.CONTAINER_NO = ot.container_code\r\n" + 
			"where t.TASK_TYPE = -1 and t.STORE_STATE = 20 and ot.id is null \r\n" +
			"and g.ASCENT_LOCK_STATE = 0 and g.IS_LOCK = 0\r\n" + 
			"order by tl.dept_num asc")
	List<SxStoreDto> findEmptySxStore();
	
	@Select("select t.CONTAINER_NO \r\n" + 
			"from sx_store t\r\n" + 
			"where t.TASK_TYPE = -1\r\n" + 
			"union all\r\n" + 
			"select zt.container_code\r\n" + 
			"from zt_ckcontainer zt\r\n" + 
			"where zt.task_type = -1")
	List<String> getAllEmptyContainer();
	
	@Select("select s.id,s.CONTAINER_NO as containerNo, s.CONTAINER_SUB_NO as containerSubNo,s.STORE_LOCATION_ID as storeLocationId ,\r\n" + 
			"s.TASK_TYPE as taskType, s.TASK_PROPERTY1 as taskProperty1,s.TASK_PROPERTY2 as taskProperty2,\r\n" + 
			"s.STORE_STATE as storeState,sl.layer\r\n" + 
			"from sx_store s \r\n" + 
			"inner join sx_store_location sl on sl.id = s.STORE_LOCATION_ID \r\n" + 
			"inner join sx_store_location_group slg on slg.ID = sl.store_location_group_id\r\n" + 
			"where s.STORE_STATE = 31 and slg.ASCENT_LOCK_STATE = 1 and slg.READY_OUT_LOCK = 0")
	List<SxStoreDto> findReadOutStore();
	
	@Select("select l.layer,count(s.id) yiweiCount from sx_store s\r\n" + 
			"left join sx_store_location l on s.STORE_LOCATION_ID = l.id\r\n" + 
			"where s.STORE_STATE = 40\r\n" + 
			"GROUP BY l.layer")
	List<YiWeiCountDto> getLayerYiWeiCount();

	@Results(id="SxStore" , value= {
			@Result(property = "containerNo",  column = "CONTAINER_NO"),
			@Result(property = "containerSubNo",  column = "CONTAINER_SUB_NO"),
			@Result(property = "storeLocationId",  column = "STORE_LOCATION_ID"),
			@Result(property = "sxStoreType",  column = "SX_STORE_TYPE"),
			@Result(property = "taskType",  column = "TASK_TYPE"),
			@Result(property = "taskProperty1",  column = "TASK_PROPERTY1"),
			@Result(property = "taskProperty2",  column = "TASK_PROPERTY2"),
			@Result(property = "businessProperty1",  column = "BUSINESS_PROPERTY1"),
			@Result(property = "businessProperty2",  column = "BUSINESS_PROPERTY2"),
			@Result(property = "businessProperty3",  column = "BUSINESS_PROPERTY3"),
			@Result(property = "businessProperty4",  column = "BUSINESS_PROPERTY4"),
			@Result(property = "businessProperty5",  column = "BUSINESS_PROPERTY5"),
			@Result(property = "storeState",  column = "STORE_STATE"),
			@Result(property = "inStoreTime",  column = "IN_STORE_TIME"),
			@Result(property = "hoisterNo",  column = "HOISTER_NO"),
			@Result(property = "carNo",  column = "CAR_NO"),
			@Result(property = "taskId",  column = "task_id"),
			@Result(property = "emptyPalletCount",  column = "EMPTY_PALLET_COUNT"),
			@Result(property = "sourceLocationId",  column = "source_location_id"),
			@Result(property = "createTime",  column = "CREATE_TIME"),
			@Result(property = "weight",  column = "WEIGHT"),
			@Result(property = "itemId",  column = "item_id"),
			@Result(property = "lotId",  column = "lot_id"),
			@Result(property = "ownerId",  column = "owner_id"),
			@Result(property = "qty",  column = "qty"),
			@Result(property = "stationId",  column = "station_id"),
			@Result(property = "containerState",  column = "container_state"),
			@Result(property = "storeNo",  column = "store_no"),
			@Result(property = "storeLocationGroupId",  column = "STORE_LOCATION_GROUP_ID"),
			@Result(property = "layer",  column = "LAYER"),//返回的是sx_store_location.layer
			@Result(property = "x",  column = "X"),//返回的是sx_store_location.x
			@Result(property = "y",  column = "Y"),//返回的是sx_store_location.y
			@Result(property = "storeLocationId1",  column = "STORE_LOCATION_ID1"),
			@Result(property = "storeLocationId2",  column = "STORE_LOCATION_ID2"),
			@Result(property = "ascentLockState",  column = "ASCENT_LOCK_STATE"),
			@Result(property = "locationIndex",  column = "LOCATION_INDEX"),
			@Result(property = "deptNum",  column = "dept_num"),
			@Result(property = "depth",  column = "depth"),
			@Result(property = "createTime",  column = "CREATE_TIME"),
			@Result(property = "verticalLocationGroupId",  column = "vertical_location_group_id"),
			@Result(property = "actualWeight",  column = "actual_weight"),
			@Result(property = "limitWeight",  column = "limit_weight"),
			@Result(property = "isInBoundLocation",  column = "is_inBound_location"),
			@Result(property = "wmsStoreNo",  column = "wms_store_no"),
			@Result(property = "taskLock",  column = "task_lock"),
			@Result(property = "groupNo",  column = "GROUP_NO"),
			@Result(property = "entrance",  column = "ENTRANCE"),
			@Result(property = "inOutNum",  column = "IN_OUT_NUM"),
			@Result(property = "isLock",  column = "IS_LOCK"),
			@Result(property = "ascentLockState",  column = "ASCENT_LOCK_STATE"),
			@Result(property = "readyOutLock",  column = "READY_OUT_LOCK"),
			@Result(property = "locationNum",  column = "location_num"),
			@Result(property = "entrance1Property1",  column = "entrance1_property1"),
			@Result(property = "entrance1Property2",  column = "entrance1_property2"),
			@Result(property = "entrance2Property1",  column = "entrance2_property1"),
			@Result(property = "entrance2Property2",  column = "entrance2_property2"),
			@Result(property = "reservedLocation",  column = "reserved_location"),
			@Result(property = "belongArea",  column = "belong_area"),
			@Result(property = "createTime",  column = "CREATE_TIME")
	})
	@Select("select * from sx_store a\n" +
			"      INNER JOIN sx_store_location l ON a.store_location_id = l.id \n" +
			"\t\t\tINNER JOIN sx_store_location_group g ON l.store_location_group_id = g.id \n" +
			"and g.IS_LOCK=0\n" +
			"and a.STORE_STATE=20\n" +
			"and g.ASCENT_LOCK_STATE =0 \n"+
			"and a.item_id = #{itemId}\n" +
			"and a.lot_id = #{lotId}\n" +
			"and a.owner_id = #{ownerId}\n" +
			"ORDER BY dept_num asc,qty asc")
	List<Map<String,Object>> getSxStoreByOrder(@Param("itemId") String itemId, @Param("lotId") String lotId , @Param("ownerId") String ownerId );



	@Select("select sum(qty) from sx_store a\n" +
			"      INNER JOIN sx_store_location l ON a.store_location_id = l.id \n" +
			"\t\t\tINNER JOIN sx_store_location_group g ON l.store_location_group_id = g.id \n" +
			"and g.IS_LOCK=0\n" +
			"and a.STORE_STATE=20\n" +
			"and a.item_id = #{itemId}\n" +
			"and a.lot_id = #{lotId}\n" +
			"and a.owner_id = #{ownerId}\n" +
			"ORDER BY dept_num asc,qty asc")
	Float getSxStoreCount(@Param("itemId") String itemId, @Param("lotId") String lotId , @Param("ownerId") String ownerId );


      @Results(id="checkOutResult" , value= {
				@Result(property = "layer",  column = "layer"),
				@Result(property = "x",  column = "x"),
				@Result(property = "y",  column = "y"),
				@Result(property = "itemId",  column = "item_id"),
				@Result(property = "lotId",  column = "lot_id"),
			    @Result(property = "ownerId",  column = "owner_id"),
			   @Result(property = "containerCode",  column = "CONTAINER_NO"),
			    @Result(property = "qty",  column = "qty")
      })
	  @Select("select d.*,a.*,l.*,g.* from \n" +
			"(select d.item_id,d.lot_id from outbound_task_detail d where d.bill_no = #{billNo} GROUP BY d.item_id,d.lot_id)d\n" +
			"inner join sx_store a on d.item_id =a.item_id and d.lot_id=a.lot_id \n" +
			"INNER JOIN sx_store_location l ON a.store_location_id = l.id  \n" +
			"INNER JOIN sx_store_location_group g ON l.store_location_group_id = g.id  \n" +
			"and g.IS_LOCK=0 and a.STORE_STATE=20 ORDER BY dept_num asc,qty asc ")
	List<CheckOutResult> getCheckOutByOutBoundTaskDetail(@Param("billNo") String billNo );

      @ResultMap("SxStore")
      @Select("SELECT * FROM\n" +
			  "\tsx_store ss\n" +
			  "\tINNER JOIN sx_store_location sl ON ss.store_location_id = sl.id\n" +
			  "\tINNER JOIN sx_store_location_group slg ON sl.store_location_group_id = slg.id \n" +
			  "\tAND slg.IS_LOCK = 0 \n" +
			  "\tAND ss.STORE_STATE = 20 \n" +
			  "\tAND slg.ASCENT_LOCK_STATE=0\n" +
			  "\tAND CONTAINER_NO = #{containerCode} and ss.item_id = #{itemId} and ss.lot_id = #{lotId}  ORDER BY dept_num asc,qty asc")
	  Map<String,Object> findSxStore(@Param("containerCode") String containerCode,@Param("lotId") String lotId, @Param("itemId") String itemId);
}
