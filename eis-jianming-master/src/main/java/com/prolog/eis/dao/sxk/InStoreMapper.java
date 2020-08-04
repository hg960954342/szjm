package com.prolog.eis.dao.sxk;

import com.prolog.eis.dto.sxk.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface InStoreMapper {

	@Select("select sl.LAYER as layer,count(s.ID) as containerCount from sx_store_location sl \r\n" + 
			"left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" + 
			"inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" + 
			"where slg.IS_LOCK = 0 and sl.LAYER NOT IN (select cl.LAYER from sx_ceng_lock cl) and sl.layer in (${layersStr})\r\n" + 
			"GROUP BY sl.LAYER ORDER BY sl.LAYER")
	List<AllInStoreLocationLayersDto> findStoreLocation(@Param("layersStr")String layersStr);

	@Select("select sl.LAYER as layer,count(s.ID) as propertyCount from sx_store_location sl \r\n" + 
			"left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" + 
			"where s.TASK_PROPERTY1 = #{taskProperty1} and s.TASK_PROPERTY2 = #{taskProperty2} and sl.LAYER in (${layersStr}) GROUP BY sl.LAYER")
	List<AllInStorePropertyCountDto> findAllPropertyLayer(@Param("layersStr")String layersStr,@Param("taskProperty1")String taskProperty1, @Param("taskProperty2")String taskProperty2);

	@Select("select sl.LAYER as layer,count(s.ID) as inTaskCount from sx_store_location sl \r\n" + 
			"left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" + 
			"where s.STORE_STATE < 20 and sl.layer in (${layersStr}) \r\n" + 
			"GROUP BY sl.LAYER ORDER BY sl.LAYER")
	List<AllInStoreInTaskCountDto> findInTaskLayer(@Param("layersStr")String layersStr);

	@Select("select sl.LAYER as layer,count(s.ID) as outTaskCount from sx_store_location sl \r\n" + 
			"left join sx_store s on s.STORE_LOCATION_ID = sl.id \r\n" + 
			"where s.STORE_STATE > 20 and s.STORE_STATE < 40 and sl.layer in (${layersStr}) \r\n" + 
			"GROUP BY sl.LAYER ORDER BY sl.LAYER")
	List<AllInStoreOutTaskCountDto> findOutTaskLayer(@Param("layersStr")String layersStr);

	@Select("select tmx.layer, COUNT(*) as taskCount from sx_path_planning_task_mx tmx \r\n" + 
			"inner join sx_connection_rim r on r.entry_code = tmx.node\r\n" + 
			"where (tmx.is_complete = 20 or tmx.is_complete = 30) and r.sx_hoister_id = #{hoisterId} and tmx.layer in (${layersStr})\r\n" + 
			"GROUP BY tmx.layer  ORDER BY tmx.LAYER")
	List<AllInStoreHoisterTaskCountDto> findHoisterTaskLayer(@Param("hoisterId")int hoisterId,@Param("layersStr")String layersStr);
	
	
	@Select("select count(*) as emptyCount from sx_store_location sl \r\n" + 
			"inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID\r\n" + 
			"where not EXISTS (select s.STORE_LOCATION_ID from sx_store s where s.STORE_LOCATION_ID = sl.ID) \r\n" + 
			"and slg.IS_LOCK = 0 and sl.ASCENT_LOCK_STATE = 0 and slg.ASCENT_LOCK_STATE = 0 \r\n" + 
			"and sl.LAYER = #{layer}")
	Integer findEmptyLocation(@Param("layer")Integer layer);

	@Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation from sx_store_location sl \r\n" + 
			"			left join sx_store s on s.STORE_LOCATION_ID = sl.ID \r\n" + 
			"			inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \r\n" + 
			"			where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 \r\n" + 
			"			and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \r\n" + 
			"			s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \r\n" + 
			"			and ((slg.entrance1_property1 = #{taskProperty1} and slg.entrance1_property2 = #{taskProperty2}) \r\n" + 
			"			or (slg.entrance2_property1 =  #{taskProperty1} and slg.entrance2_property1 = #{taskProperty2} )) and slg.layer = #{layer} \r\n" + 
			"			GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
	List<InStoreLocationGroupDto> findSamePropertyStoreLocationGroup(@Param("layer")Integer layer, @Param("taskProperty1")String taskProperty1, @Param("taskProperty2")String taskProperty2);

	@Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \n" + 
			"						left join sx_store s on s.STORE_LOCATION_ID = sl.ID \n" + 
			"						inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \n" + 
			"						where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0  and sl.is_inBound_location = 1\n" + 
			"						and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \n" + 
			"						s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \n" + 
			"						and slg.layer = #{layer} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 )\n" + 
			"						GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
	List<InStoreLocationGroupDto> findStoreLocationGroup(@Param("layer")Integer layer,@Param("weight") Double weight);

	@Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \n" + 
			"						left join sx_store s on s.STORE_LOCATION_ID = sl.ID \n" + 
			"						inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \n" + 
			"						where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 and sl.is_inBound_location = 1\n" + 
			"						and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \n" + 
			"						s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \n" + 
			"						and slg.layer = #{layer} and slg.belong_area = #{area} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 )\n" + 
			"						GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
	List<InStoreLocationGroupDto> findStoreLocationGroupByArea(@Param("layer")Integer layer, @Param("area")Integer area,@Param("weight") Double weight);

	@Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl  \r\n" + 
			"									left join sx_store s on s.STORE_LOCATION_ID = sl.ID  \r\n" + 
			"									inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID  \r\n" + 
			"									where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0  and sl.is_inBound_location = 1 \r\n" + 
			"									and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on  \r\n" + 
			"									s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID)  \r\n" + 
			"									and slg.layer = #{layer} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 ) \r\n" + 
			"									and sl.id not in (select ssr.store_parent_id from sx_store_relation ssr where ssr.store_lock = 1 union select ssr2.store_childen_id from sx_store_relation ssr2 where ssr2.store_lock = 1)	GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
	List<InStoreLocationGroupDto> findStoreLocationGroupForLx(@Param("layer")Integer layer,@Param("weight") Double weight);

	@Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \r\n" + 
			"								left join sx_store s on s.STORE_LOCATION_ID = sl.ID \r\n" + 
			"								inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \r\n" + 
			"								where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 and sl.is_inBound_location = 1\r\n" + 
			"								and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \r\n" + 
			"								s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \r\n" + 
			"								and slg.layer = #{layer} and slg.belong_area = #{area} and EXISTS (select sl2.id from sx_store_location sl2 where sl2.limit_weight > #{weight} and sl2.STORE_LOCATION_GROUP_ID = slg.ID and sl2.is_inBound_location = 1 ) and sl.id not in (select ssr.store_parent_id from sx_store_relation ssr where ssr.store_lock = 1 union select ssr2.store_childen_id from sx_store_relation ssr2 where ssr2.store_lock = 1)\r\n" + 
			"								GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
	List<InStoreLocationGroupDto> findStoreLocationGroupByAreaForLx(@Param("layer")Integer layer, @Param("area")Integer area,@Param("weight") Double weight);

	@Select("select slg.id as storeLocationGroupId,count(s.id) as containerCount,slg.IN_OUT_NUM as inOutNum ,slg.x,slg.y,slg.reserved_location as reservedLocation,slg.entrance1_property1 as entrance1Property1 ,slg.entrance1_property2 as entrance1Property2 ,slg.entrance2_property1 as entrance2Property1 ,slg.entrance2_property2 as  entrance2Property2 from sx_store_location sl \r\n" +
			"								left join sx_store s on s.STORE_LOCATION_ID = sl.ID \r\n" +
			"								inner join sx_store_location_group slg on slg.id = sl.STORE_LOCATION_GROUP_ID \r\n" +
			"								where slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0 and sl.is_inBound_location = 1\r\n" +
			"								and slg.location_num > (select count(*) from sx_store_location sl inner join sx_store s on \r\n" +
			"								s.STORE_LOCATION_ID = sl.ID where sl.STORE_LOCATION_GROUP_ID = slg.ID) \r\n" +
			"								and slg.layer = #{layer} and slg.belong_area = #{area} and slg.reserved_location = #{reservedLocation} and sl.id not in (select ssr.store_parent_id from sx_store_relation ssr where ssr.store_lock = 1 union select ssr2.store_childen_id from sx_store_relation ssr2 where ssr2.store_lock = 1)\r\n" +
			"								GROUP BY slg.ID ,slg.IN_OUT_NUM,slg.IN_OUT_NUM,slg.x,slg.y,slg.reserved_location")
    List<InStoreLocationGroupDto> findAreaLocation(@Param("layer")Integer layer, @Param("reservedLocation")int reservedLocation, @Param("area")int area);
}
