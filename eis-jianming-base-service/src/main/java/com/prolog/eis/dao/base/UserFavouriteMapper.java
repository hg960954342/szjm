package com.prolog.eis.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.base.UserFavourite;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface UserFavouriteMapper extends BaseMapper<UserFavourite>{

	@Select("select uf.firstPrivilegeId from UserFavourite uf where uf.userId = #{userId}")
	public List<Integer> findFirstPrivilege(@Param ("userId") int userId);
}
