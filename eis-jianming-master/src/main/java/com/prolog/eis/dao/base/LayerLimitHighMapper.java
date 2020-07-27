package com.prolog.eis.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.eis.LayerLimitHigh;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface LayerLimitHighMapper extends BaseMapper<LayerLimitHigh> {
	
	@Select("select t.layer from layer_limit_high t where t.layer in (${layers}) and t.limit_high > #{high}")
	public List<Integer> getHighAvailableLayer(@Param("layers") String layers, @Param("high") double high);
}
