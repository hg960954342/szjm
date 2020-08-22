package com.prolog.eis.dao.sxk;

import com.prolog.eis.dto.sxk.StoreLocationGroupDto;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface SxStoreLocationGroupMapper extends BaseMapper<SxStoreLocationGroup>{

	@Insert("<script>insert into SX_STORE_LOCATION_GROUP " +
            "(belong_area,ENTRANCE,GROUP_NO,IN_OUT_NUM,layer," +
            "location_num,reserved_location,x,y,ASCENT_LOCK_STATE,is_lock,READY_OUT_LOCK) values " +
            "<foreach collection='list' item='c' separator=','>" +
            "(#{c.belongArea},#{c.entrance},#{c.groupNo}" +
            ",#{c.inOutNum},#{c.layer},#{c.locationNum},#{c.reservedLocation}" +
            ",#{c.x},#{c.y},0,0,0)" +
            "</foreach></script>")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void saveBatchReturnKey(@Param("list") List<StoreLocationGroupDto> storeLocationGroupDtos);

    /**
     * 根据编号找货位组
     *
     * @param groupNosStr
     * @return
     */
    @Select("select t.id as id,t.GROUP_NO as groupNo,t.location_num as locationNum from SX_STORE_LOCATION_GROUP t" +
            " where FIND_IN_SET(t.GROUP_NO,#{groupNosStr})")
    List<StoreLocationGroupDto> findByGroupNos(@Param("groupNosStr") String groupNosStr);


    /**
     * "t.x=t2.x,t.y=t2.y,t.ENTRANCE=t2.entrance,t.IN_OUT_NUM=t2.inOutNum" +
     * t.belong_area=t2.belongArea,
     *
     * @param list
     */
    @Update("<script>UPDATE SX_STORE_LOCATION_GROUP t," +
            "(<foreach collection = \"list\" separator = \"union all\" item = \"item\">\n" +
            "     select #{item.id} as id," +
            "            #{item.x} as x," +
            "            #{item.y} as y," +
            "            #{item.belongArea} as belongArea," +
            "            #{item.entrance} as entrance," +
            "            #{item.inOutNum} as inOutNum," +
            "            #{item.reservedLocation} as reservedLocation" +
            "  </foreach>) t2" +
            " set t.reserved_location = t2.reservedLocation" +
            " where t.id = t2.id</script>")
    void batchUpdateById(@Param("list") List<StoreLocationGroupDto> list);


    @Update("update\n" +
            " sx_store_location_group t\n" +
            "set\n" +
            " t.ascent_lock_state = #{state}\n" +
            "where\n" +
            " t.id = (\n" +
            "select a.id from(\n" +
            " select\n" +
            "  g.id\n" +
            " from\n" +
            "  sx_store_location_group g\n" +
            " left join sx_store_location l on\n" +
            "  l.store_location_group_id = g.id\n" +
            " left join sx_store s on\n" +
            "  s.store_location_id = l.id\n" +
            " where\n" +
            "  s.container_no = #{containerNo}) as a)")
    void updateState(@Param("state") int state, @Param("containerNo") String containerNo);

    @Update("UPDATE sx_store_location_group slg\n" +
			"SET slg.`IS_LOCK` = #{isLock}\n" +
			"WHERE\n" +
			"FIND_IN_SET(slg.layer,#{layers})\n" +
			"AND slg.x <= #{x}\n" +
			"AND slg.y <= #{y};")
	Integer LockLocationGroup(@Param("isLock") int isLock,@Param("x") int x,@Param("y") int y,@Param("layers") String layers);

    @Select("select slg.id ,slg.location_num as locationNum from sx_store_location_group slg where slg.id not in ( select slg2.id from sx_store_location_group slg2 INNER JOIN sx_store_location sl on sl.STORE_LOCATION_GROUP_ID = slg2.id inner join sx_store s on s.STORE_LOCATION_ID = sl.ID   )")
    List<SxStoreLocationGroup> findNoHaveStore();

    @Select("select slg2.id from sx_store_location_group slg2 INNER JOIN sx_store_location sl on sl.STORE_LOCATION_GROUP_ID = slg2.id inner join sx_store s on s.STORE_LOCATION_ID = sl.ID  ")
    List<SxStoreLocationGroup> findHaveStore();
}
