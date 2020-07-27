insert into UserRole values(1,'系统管理员',0,1);
insert into UserRole values(2,'普通用户',1,2);
insert into UserDept values(1,'普罗格',0,'普罗格',1);
insert into sysuser (ID, USERDEPTID, LOGINNAME, USERNAME, USERPASSWORD, ROLEID, SEX, MOBILE, LASTLOGINTIME, CREATETIME, WORK_NO)
values (1, 1, 'admin', '管理员', '21218CCA77804D2BA1922C33E0151105', 1, 1, null, '2019-09-26 20:03:53', '2019-09-26 20:03:53', null);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (1, 'PrivilegeManager', '功能权限配置', 1);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (2, 'RoleManager', '角色管理', 2);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (3, 'UserManage', '用户管理', 3);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (7, 'YezManager', '货主资料', 6);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (13, 'AreaViewManager', '区域资料', 10);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (18, 'PickOrderRequestManage', '拣选单索取', 11);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (19, 'SysClientConfigViewMananger', '系统参数配置查询', 15);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (20, 'InterFaceExceptionViewManager', '接口重传', 16);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (8, 'LiaoXiangManager', '料箱资料', 4);


insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (10, 'SpInfoManager', '商品资料', 7);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (11, 'huoweikucunViewManager', '货位资料', 8);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (17, 'OrderViwViewManager', '订单查询', 14);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (12, 'KucunQueryViewManager', '库存同步更新', 9);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (14, 'BoZhongViewManage', '播种作业', 12);

insert into FirstPrivilege (ID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (15, 'LackWarningSearchViewManage', '库存预警', 13);


insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (1, 1, 'AddFirstPrivilegeCommand', '添加一级权限', 1);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (2, 1, 'EditFirstPrivilegeCommand', '编辑一级权限', 2);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (3, 1, 'DeleteFirstPrivilegeCommand', '删除一级权限', 3);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (4, 1, 'AddSecondPrivilegeCommand', '添加二级级权限', 4);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (5, 1, 'EditSecondPrivilegeCommand', '编辑二级权限', 5);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (6, 1, 'DeletSecondPrivilegeCommand', '删除二级权限', 6);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (7, 1, 'FirstMoveCommand', '一级权限排序', 7);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (8, 1, 'SecondMoveCommand', '二级权限排序', 8);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (9, 2, 'AddRoleCommand', '添加角色', 1);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (10, 2, 'EditRoleCommand', '编辑角色', 2);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (11, 2, 'DeleteRoleCommand', '删除角色', 3);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (12, 2, 'MoveCommand', '角色排序', 4);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (13, 2, 'EditUserPrivilegeCommand', '角色权限编辑', 5);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (14, 3, 'AddUserCommand', '添加用户', 1);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (15, 3, 'EditUserCommand', '编辑用户', 2);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (16, 3, 'DeleteUserCommand', '删除用户', 3);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (17, 3, 'AddTreeRootCommand', '添加根部门', 4);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (18, 3, 'AddTreeCommand', '添加部门', 5);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (19, 3, 'EditTreeCommand', '编辑部门', 6);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (20, 3, 'DeleteTreeCommand', '删除部门', 7);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (21, 3, 'MoveTreeCommand', '部门排序', 8);

insert into SecondPrivilege (ID, FIRSTPRIVILEGEID, PRIVILEGEKEY, PRIVILEGENAME, SORTINDEX)
values (22, 3, 'MoveTreeNodeCommand', '移动部门', 9);