package com.prolog.eis.dao.base;

import com.prolog.eis.dto.base.SysUserRespDto;
import com.prolog.eis.model.base.SysUser;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser>{
	
	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "userDeptId",  column = "userdeptid"),
        @Result(property = "loginName",  column = "loginname"),@Result(property = "userName",  column = "username"),
        @Result(property = "userPassword",  column = "userpassword"),@Result(property = "roleId",  column = "roleid"),
        @Result(property = "sex",  column = "sex"),@Result(property = "mobile",  column = "mobile"),
        @Result(property = "lastLoginTime",  column = "lastlogintime"),@Result(property = "createTime",  column = "createtime"),
        @Result(property = "workNo",column = "WORK_NO")
	})
    @Select("select t.id, t.userdeptid, t.loginname,t.username, t.userpassword,t.roleid," + 
    		"t.sex,t.mobile, t.lastlogintime,t.createtime from sysuser t where t.loginname = #{userName}")
    public List<SysUser> findUser(@Param ("userName") String userName);
	
	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "userDeptId",  column = "userdeptid"),
        @Result(property = "loginName",  column = "loginname"),@Result(property = "userName",  column = "username"),
        @Result(property = "userPassword",  column = "userpassword"),@Result(property = "roleId",  column = "roleid"),
        @Result(property = "sex",  column = "sex"),@Result(property = "mobile",  column = "mobile"),
        @Result(property = "lastLoginTime",  column = "lastlogintime"),@Result(property = "createTime",  column = "createtime"),
        @Result(property = "workNo",column = "WORK_NO")
	})
    @Select("select t.id, t.userdeptid, t.loginname,t.username, t.userpassword,t.roleid," + 
    		"t.sex,t.mobile, t.lastlogintime,t.createtime,t.WORK_NO from sysuser t where t.loginname = #{loginname} and t.id != #{id} ")
    public List<SysUser> findOtherUserLoginName(@Param ("loginname") String loginname,@Param ("id") int id);
	
	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "userDeptId",  column = "userdeptid"),
        @Result(property = "loginName",  column = "loginname"),@Result(property = "userName",  column = "username"),
        @Result(property = "userPassword",  column = "userpassword"),@Result(property = "roleId",  column = "roleid"),
        @Result(property = "sex",  column = "sex"),@Result(property = "mobile",  column = "mobile"),
        @Result(property = "lastLoginTime",  column = "lastlogintime"),@Result(property = "createTime",  column = "createtime"),
        @Result(property = "objectName",  column = "objectname"),@Result(property = "fullPath",  column = "fullpath"),
        @Result(property = "workNo",column = "WORK_NO")
	})
	@Select("select su.id,su.userdeptid,su.loginname,su.username,su.userpassword,su.roleid,su.sex,su.mobile, su.lastlogintime," + 
			"su.createtime,su.WORK_NO,ud.objectname,ud.fullpath from sysuser su " + 
			"left join userdept ud on su.userdeptid = ud.id " + 
			"where (su.loginname like ${loginname} or su.username like ${loginname}) and su.userdeptid in (select su.id from userdept t where concat(t.fullpath,'/' like ${fullpath})) ")
	public List<SysUserRespDto> queryUser(@Param ("name") String name,@Param("fullpath") String fullpath);
	
	
	
	
	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "userDeptId",  column = "userdeptid"),
        @Result(property = "loginName",  column = "loginname"),@Result(property = "userName",  column = "username"),
        @Result(property = "roleId",  column = "roleid"),@Result(property = "sex",  column = "sex"),
        @Result(property = "mobile",  column = "mobile"),@Result(property = "lastLoginTime",  column = "lastlogintime"),
        @Result(property = "createTime",  column = "createtime"),@Result(property = "objectName",  column = "objectname"),
        @Result(property = "fullPath",  column = "fullpath"), @Result(property = "roleName",  column = "rolename"),
        @Result(property = "workNo",column = "WORK_NO")
    })
	@Select("select su.id,su.userdeptid,su.loginname,su.username,su.roleid,su.sex,su.mobile, su.lastlogintime," + 
			"su.createtime,su.WORK_NO,ud.objectname,ud.fullpath,ur.rolename from sysuser su " + 
			"left join userdept ud on su.userdeptid = ud.id left join userrole ur on su.roleid = ur.id " + 
			"where (su.loginname like ${name} or su.username like ${name}) and su.userdeptid in (select t.id from userdept t where concat(t.fullpath,'/' like ${fullpath})) order by ud.sortindex ")
	public List<SysUserRespDto> queryUserNoPwd(@Param ("name") String name,@Param("fullpath") String fullpath);
	
	
	
	@Results({
        @Result(property = "id",  column = "id"),@Result(property = "userDeptId",  column = "userdeptid"),
        @Result(property = "loginName",  column = "loginname"),@Result(property = "userName",  column = "username"),
        @Result(property = "roleId",  column = "roleid"), @Result(property = "sex",  column = "sex"),
        @Result(property = "mobile",  column = "mobile"),@Result(property = "lastLoginTime",  column = "lastlogintime"),
        @Result(property = "createTime",  column = "createtime"), @Result(property = "workNo",column = "WORK_NO")
    })
	@Select("select t.id,t.userdeptid,t.loginname,t.username,t.roleid,t.sex,t.mobile,t.lastlogintime,t.createtime,t.WORK_NO from sysuser t where t.id = #{id} order by t.id ")
	public SysUser findUserNoPwd(@Param ("id") int id);
	
}
