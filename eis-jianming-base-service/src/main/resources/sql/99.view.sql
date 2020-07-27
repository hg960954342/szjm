CREATE OR REPLACE VIEW SYSUSERVIEW AS
  select u.id as id,
       u.loginname as loginName,
       u.username as userName,
       ud.fullpath as deptFullPath,
       u.sex as sex,
	   u.work_no as workNo,
       r.rolename as roleName
from SysUser u
left join UserDept ud on u.userdeptid = ud.id
left join UserRole r on u.roleid = r.id