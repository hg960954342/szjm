package com.prolog.eis.dao.base;

import com.prolog.eis.model.base.UserRole;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserRoleMapper extends BaseMapper<UserRole> {

	@Select("select max(ur.sortindex) from UserRole ur")
	public Integer findMaxSortIndex();
	
	@Select("select count(*) \r\n" + 
			"from Userrole r\r\n" + 
			"where r.rolename = #{rolename} and r.id != #{id}")
	public int findOtherUserName(@Param ("rolename") String userName,@Param ("id") int id);
}
