package com.prolog.eis.dao.sxk;

import com.prolog.eis.dto.sxk.*;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocationDto;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

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

    @Delete("delete from sx_store")
    void deleteAll();

    @Update("update sx_store t set t.store_state = #{state} where t.id = #{containerNo}")
    public void updateContainerNoState(@Param("containerNo") String containerNo,@Param("state") int state);

    @Update("update sx_store t set t.store_state = 20 , t.HOISTER_NO = NULL , t.CAR_NO = NULL , t.TASK_ID = NULL, t.source_location_id = NULL   where t.CONTAINER_NO = #{containerNo}")
    public void updateContainerGround(@Param("containerNo") String containerNo);

	/**
	 * 查找托盘剁
	 * @param layer
	 * @return
	 */
	@Select("select \n" +
			"s.ID as id,\n" +
			"s.CONTAINER_NO  as containerNo,\n" +
			"sl.x as x,\n" +
			"sl.y as y,\n" +
			"sl.layer as layer,\n"+
			"sg.reserved_location as reservedLocation,\n"+
			"s.STORE_LOCATION_ID  as storeLocationId\n" +
			"from sx_store s \n" +
			"join sx_store_location sl on sl.id = s.STORE_LOCATION_ID" +
			" join sx_store_location_group sg on sl.STORE_LOCATION_GROUP_ID = sg.id\n" +
			" where s.TASK_TYPE = -1 and s.STORE_STATE = 20 and sl.dept_num = 0 and " +
			" sg.IS_LOCK = 0 and sg.ASCENT_LOCK_STATE = 0"+
			" and sl.layer = #{layer}" +
			" and s.CONTAINER_NO not in (select work_container_no from split_fold_machine where work_container_no is not null)")
	List<SplitOutDto> findTraychop(@Param("layer") int layer);

	@Delete("delete from sx_store sx where sx.container_no = #{containerNo}")
	void deleteByContainer(@Param("containerNo") String containerNo);
	
	@Select("select\r\n" + 
			"	b.layer,\r\n" + 
			"	b.belong_area as belongArea,\r\n" + 
			"	a.taskCount,\r\n" + 
			"	b.carNoCount,\r\n" + 
			"	a.taskCount / b.carNoCount as avgCount,\r\n" + 
			"	floor(b.carNoCount * 1.5) as maxCarTask\r\n" + 
			"from\r\n" + 
			"	(\r\n" + 
			"	select\r\n" + 
			"		l.layer,\r\n" + 
			"		g.belong_area as belongArea,\r\n" + 
			"		count(hz.id) as taskCount\r\n" + 
			"	from\r\n" + 
			"		sx_path_planning_task_hz hz\r\n" + 
			"	left join sx_store s on\r\n" + 
			"		hz.container_no = s.container_no\r\n" + 
			"	left join sx_store_location l on\r\n" + 
			"		s.store_location_id = l.id\r\n" + 
			"	left join sx_store_location_group g on\r\n" + 
			"		l.store_location_group_id = g.id\r\n" + 
			"	left join sx_path_planning_task_mx mx on\r\n" + 
			"		hz.id = mx.task_hz_id\r\n" + 
			"	where\r\n" + 
			"		mx.transportation_equipment = 2\r\n" + 
			"		and mx.is_complete in (10,20,30)\r\n" + 
			"		and hz.path_type = 2\r\n" + 
			"	group by\r\n" + 
			"		g.belong_area,\r\n" + 
			"		l.layer) a\r\n" + 
			"right join (\r\n" + 
			"	select\r\n" + 
			"		sc.belong_area,\r\n" + 
			"		sc.layer,\r\n" + 
			"		count(sc.id) as carNoCount\r\n" + 
			"	from\r\n" + 
			"		sx_car sc\r\n" + 
			"	where\r\n" + 
			"		sc.car_state = 1\r\n" + 
			"		or sc.car_state = 2\r\n" + 
			"	group by\r\n" + 
			"		sc.belong_area,\r\n" + 
			"		sc.layer) b on\r\n" + 
			"	a.belongArea = b.belong_area\r\n" + 
			"	and a.layer = b.layer\r\n" + 
			"order by\r\n" + 
			"	avgCount asc")
	List<SxStoreDto> findAreaLaryGroup();
	
	@Update("update sx_store t set t.car_no = #{carNo} where t.container_no = #{containerNo}")
	void updateStoreCar(@Param("carNo") String carNo,@Param("containerNo") String containerNo);

	@Select("select slg.id ,slg.reserved_location as reservedLocation from sx_store_location_group slg inner join sx_store_location sl on sl.store_location_group_id = slg.ID\n" + 
			"inner join sx_store sx  on sx.STORE_LOCATION_ID = sl.id where sx.id = #{storeId}")
	SxStoreLocationGroup findAreaTypeById(@Param("storeId")Integer storeId);

	@Select("select s.id,s.CONTAINER_NO as containerNo, s.CONTAINER_SUB_NO as containerSubNo,s.STORE_LOCATION_ID as storeLocationId ,\n" + 
			"s.TASK_TYPE as taskType, s.TASK_PROPERTY1 as taskProperty1,s.TASK_PROPERTY2 as taskProperty2,\n" + 
			"s.STORE_STATE as storeState\n" + 
			"from sx_store s \n" + 
			"inner join sx_store_location sl on sl.id = s.STORE_LOCATION_ID \n" + 
			"inner join sx_store_location_group slg on slg.ID = sl.store_location_group_id\n" + 
			"where s.STORE_STATE = 31 and slg.ASCENT_LOCK_STATE = 1 and slg.READY_OUT_LOCK = 0")
	List<SxStore> findReadOutStore();
	
	/**
	 * 托盘找库存
	 * @param join
	 * @return
	 */
	@Select("select t.CONTAINER_NO\n" +
			"  from sx_store t where FIND_IN_SET (t.CONTAINER_NO,#{join})" +
			" and t.STORE_STATE > 10" )
    List<String> findByContainerCodes(@Param("join") String join);

	/**
	 * 定位查询 库存信息
	 * @param coordinate
	 * @return
	 */
	@Select("select s.CONTAINER_NO as containerNo,\n" +
			" t.id as storeLocationId,"+
			" s.STORE_STATE as storeState"+
			"  from sx_store_location t " +
			"  left join sx_store s on s.STORE_LOCATION_ID = t.id " +
			"  where t.store_no=#{coordinate}")
    SxStoreLocationDto getSxStoreLocationByCoord(@Param("coordinate") String coordinate);
	
	/**
	 * 查询容器所在的货位组Id
	 * @param containerNo
	 * @return
	 */
	@Select("select l.store_location_group_id \r\n" + 
			"from sx_store_location l \r\n" + 
			"inner join sx_store s on s.STORE_LOCATION_ID = l.id\r\n" + 
			"where l.store_no=#{coordinate}")
	Integer getStoreLocationGroupIdByCoordinate(String coordinate);
	
	/**
	 * 查询货位组的所有库存
	 * @param groupId
	 * @return
	 */
	@Select("select s.CONTAINER_NO as containerNo,\r\n" + 
			"			t.id as storeLocationId,\r\n" + 
			"			s.STORE_STATE as storeState\r\n" + 
			"			 from sx_store s\r\n" + 
			"			 left join sx_store_location t on s.STORE_LOCATION_ID = t.id\r\n" + 
			"			 where t.store_location_group_id=#{groupId}")
	List<SxStoreLocationDto> getSxStoreLocationListByGroupId(@Param("groupId") Integer groupId);
	
	@Select("select count(*) from stacker_store s where s.box_no = #{boxNo}")
	int getStackerStoreExists(@Param("boxNo") String boxNo);
}
