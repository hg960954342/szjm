package com.prolog.eis.dao.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.model.base.RoleSecondPrivilege;
import com.prolog.eis.model.base.SecondPrivilege;
import com.prolog.framework.dao.mapper.BaseMapper;

public interface RoleSecondPrivilegeMapper extends BaseMapper<RoleSecondPrivilege>{

	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "firstPrivilegeId",  column = "firstprivilegeid"),
        @Result(property = "privilegeKey",  column = "privilegekey"),@Result(property = "privilegeName",  column = "privilegename"),
        @Result(property = "sortIndex",  column = "sortindex")
    })
    @Select("select sp.id,sp.firstprivilegeid,sp.privilegekey,sp.privilegename, sp.sortindex from rolesecondprivilege rsp " + 
    		"left join secondprivilege sp on rsp.secondprivilegeid = sp.id where rsp.roleid = #{roleId} order by sp.sortindex asc")
    public List<SecondPrivilege> findRoleSecondPrivilege(@Param ("roleId") int roleId);
}
