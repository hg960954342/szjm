package com.prolog.eis.dao.base;

import com.prolog.eis.model.base.EisClientConfig;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface EisClientConfigMapper extends BaseMapper<EisClientConfig>{

	@Update("update eis_client_config ec set ec.config_value = #{configValue} where ec.config_key  = #{configKey}")
	void updateConfig(@Param("configKey")String configKey, @Param("configValue")String configValue);

}
