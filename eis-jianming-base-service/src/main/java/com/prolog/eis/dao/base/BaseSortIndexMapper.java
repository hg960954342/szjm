package com.prolog.eis.dao.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


public interface BaseSortIndexMapper{
	
	
	/**
	 * 修改sortIndex
	 * @date 2018年9月8日 下午2:45:48
	 * @author dengss
	 * @param tableName
	 * @param parentId
	 * @param id
	 */
	@Update("update ${tableName} set sortIndex = #{sortIndex} where id = #{id}")
	public void updateSortIndex(@Param("tableName")String tableName,@Param("sortIndex")int sortIndex,@Param("id") int id);
}
