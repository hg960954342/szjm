package com.prolog.eis.dao.base;

import com.prolog.eis.model.base.UserFavourite;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserFavouriteMapper extends BaseMapper<UserFavourite>{

	@Select("select uf.firstPrivilegeId from UserFavourite uf where uf.userId = #{userId}")
	public List<Integer> findFirstPrivilege(@Param ("userId") int userId);
}
