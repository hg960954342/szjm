package com.prolog.eis.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.base.FirstPrivilege;
import com.prolog.eis.model.base.RoleFirstPrivilege;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface RoleFirstPrivilegeMapper extends BaseMapper<RoleFirstPrivilege>{

	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "privilegeKey",  column = "privilegekey"),
        @Result(property = "privilegeName",  column = "privilegename"),
        @Result(property = "sortIndex",  column = "sortindex")
    })
    @Select("select fg.id,fg.privilegekey,fg.privilegename,fg.sortindex from rolefirstprivilege rfp " + 
    		"left join firstprivilege fg on rfp.firstprivilegeid = fg.id where rfp.roleid = #{roleId} order by fg.sortindex asc")
    public List<FirstPrivilege> findRoleFirstPrivilege(@Param ("roleId") int roleId);
}
