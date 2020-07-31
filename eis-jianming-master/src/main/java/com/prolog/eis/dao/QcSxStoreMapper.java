package com.prolog.eis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.SxStoreDto;
import com.prolog.eis.dto.eis.YiWeiCountDto;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.framework.dao.mapper.BaseMapper;

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
}
