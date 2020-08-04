package com.prolog.eis.dao;

import com.prolog.eis.dto.eis.EmptyCaseLayerDto;
import com.prolog.eis.model.eis.EmptyCaseConfig;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmptyCaseConfigMapper extends BaseMapper<EmptyCaseConfig>{

	@Results({
		@Result(property = "id",  column = "id"),
		@Result(property = "layer",  column = "layer"),
		@Result(property = "minCount",  column = "min_count"),
		@Result(property = "sortIndex",  column = "sort_index")
	})
	@Select("select t.* from empty_case_config t order by t.sort_index")
	List<EmptyCaseConfig> getEmptyCaseConfigs();
	
	@Select("select t.layer,COUNT(*) as emptyCaseCount\r\n" + 
			"from sx_store s\r\n" + 
			"left join sx_store_location t on s.STORE_LOCATION_ID = t.id\r\n" + 
			"where s.TASK_TYPE = -1\r\n" + 
			"group by t.layer")
	List<EmptyCaseLayerDto> getEmptyCaseStores();
}
