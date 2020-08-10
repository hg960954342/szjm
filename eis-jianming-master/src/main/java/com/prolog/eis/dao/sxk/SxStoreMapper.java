package com.prolog.eis.dao.sxk;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.prolog.eis.dto.eis.SxStoreDto;
import com.prolog.eis.dto.sxk.LayerTaskGroupSortDto;
import com.prolog.eis.dto.sxk.SxStoreGroupDto;
import com.prolog.eis.dto.sxk.SxStoreLock;
import com.prolog.eis.dto.sxk.SxStoreLocksDto;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface SxStoreMapper extends BaseMapper<SxStore> {

	/**
     * 查询每层任务数分组排序（过滤已上架）
     * @return
     */
    @Select("select t.*,t.taskCount/t.carCount as agvTaskCount from (select \r\n" + 
    		"	   sl.layer,\r\n" + 
    		"	   count(ss.id) as taskCount,\r\n" + 
    		"	   (select count(1) from sx_car sc where sc.layer = sl.layer and (sc.car_state = 1 or sc.car_state = 2)) as carCount\r\n" + 
    		"from sx_store ss\r\n" + 
    		"left join sx_store_location sl on sl.id = ss.store_location_id \r\n" + 
    		"where (ss.store_state = 1 or ss.store_state = 3) \r\n" + 
    		"group by sl.layer) t order by agvTaskCount asc")
    List<LayerTaskGroupSortDto> findLayerTaskGroupSort();
    
    /**
     * 查询锁定状态
     * @param containerNo
     * @return
     */
    @Select("select \n" + 
    		"ss.id as storeId,\n" + 
    		"sl.id as locationId,\n" + 
    		"sl.ascent_lock_state as ascentLockState, \n" + 
    		"sslp.ascent_lock_state as ascentGroupLockState, \n" + 
    		"sslp.is_lock as isLock, \n" + 
    		"sl.dept_num as deptNum, \n" + 
    		"ss.store_state as storeState, \n" + 
    		"sl.layer, \n" + 
    		"sl.x, \n" + 
    		"sl.y \n" + 
    		"from \n" + 
    		"sx_store ss \n" + 
    		"left join sx_store_location sl on \n" + 
    		"sl.id = ss.store_location_id \n" + 
    		"left join sx_store_location_group sslp on \n" + 
    		"sl.store_location_group_id = sslp.id\n" + 
    		"where \n" + 
    		"ss.container_no =  \n" + 
    		"#{containerNo}\n" + 
    		"order by sl.dept_num")
    SxStoreLock findSxStoreLock(@Param("containerNo") String containerNo);
    
    /**
     * 查询锁定状态
     * @param containerNo
     * @return
     */
    @Select("select\n" + 
    		" sl.ascent_lock_state as ascentLockStat,\n" + 
    		" sslp.ascent_lock_state as ascentGroupLockState,\n" + 
    		" sslp.is_lock as isLock,\n" + 
    		" ss.container_no as containerNo,\n" + 
    		" sl.dept_num as deptNum\n" + 
    		"from\n" + 
    		" sx_store ss\n" + 
    		"left join sx_store_location sl on\n" + 
    		" sl.id = ss.store_location_id\n" + 
    		"left join sx_store_location_group sslp on\n" + 
    		" sl.store_location_group_id = sslp.id\n" + 
    		"where\n" + 
    		" ${containerSql}")
    List<SxStoreLocksDto> findSxStoreLocks(@Param("containerSql") String containerSql);

    @Select("select\r\n" + 
    		"	sl.ascent_lock_state as ascentLockState,\r\n" + 
    		"	sl.dept_num as deptNum,\r\n" + 
    		"	sl.layer,\r\n" + 
    		"	sl.location_index as locationIndex,\r\n" + 
    		"	sl.store_location_group_id as storeLocationGroupId,\r\n" + 
    		"	sl.store_location_id1 as storeLocationId1,\r\n" + 
    		"	sl.store_location_id2 as storeLocationId2,\r\n" + 
    		"	sl.x,\r\n" + 
    		"	sl.y,\r\n" + 
    		"	ss.container_no as containerNo,\r\n" + 
    		"	ss.task_property1 as taskProperty1,\r\n" + 
    		"	ss.task_property2 as taskProperty2,\r\n" + 
    		"	ss.store_state as storeState,\r\n" +
    		"	ss.id as sotreId\r\n" +
    		"from\r\n" + 
    		"	sx_store_location sl\r\n" + 
    		"left join sx_store ss on\r\n" + 
    		"	sl.id = ss.store_location_id\r\n" + 
    		"where\r\n" + 
    		"	sl.store_location_group_id = (\r\n" + 
    		"	select\r\n" + 
    		"		t.store_location_group_id\r\n" + 
    		"	from\r\n" + 
    		"		sx_store_location t\r\n" + 
    		"	left join sx_store ss on\r\n" + 
    		"		t.id = ss.store_location_id\r\n" + 
    		"	where\r\n" + 
    		"		ss.container_no = #{containerNo} )\r\n" + 
    		"	and ss.id != 0")
    public List<SxStoreGroupDto> findStoreGroup(@Param("containerNo") String containerNo);
    
    @Update("update sx_store t set t.store_state = #{state} where t.container_no = #{containerNo}")
    public void updateStoreState(@Param("containerNo") String containerNo,@Param("state") int state);

    @Update("update sx_store t set t.store_state = 20 , t.HOISTER_NO = NULL , t.CAR_NO = NULL , t.TASK_ID = NULL, t.source_location_id = NULL   where t.CONTAINER_NO = #{containerNo}")
    public void updateContainerGround(@Param("containerNo") String containerNo);

	@Delete("delete from sx_store sx where sx.container_no = #{containerNo}")
	void deleteByContainer(@Param("containerNo") String containerNo);

	@Select("select s.CONTAINER_NO containerNo,l.layer,l.x,l.y,l.dept_num deptNum\n" + 
			"from sx_store s\n" + 
			"left join sx_store_location l on s.STORE_LOCATION_ID = l.id\n" + 
			"left join container_task ta on s.CONTAINER_NO = ta.container_code\n" + 
			"where s.TASK_TYPE = -1 and ta.id is null\n" + 
			"order by l.dept_num")
	List<SxStoreDto> getEmptyContainerCode();
}
