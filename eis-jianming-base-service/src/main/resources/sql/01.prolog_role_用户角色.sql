-- PRINT '------ 角色 ------'
CREATE TABLE UserRole
(
  Id          INT  PRIMARY KEY AUTO_INCREMENT,
  RoleName    VARCHAR(50) NOT NULL UNIQUE,         -- 角色名称
  IsDefault   INT(1) NOT NULL,                     -- 是否默认角色
  SortIndex	  INT NOT NULL					       -- 排序索引
);

-- PRINT '------ 一级权限 ------'
CREATE TABLE FirstPrivilege
(
  Id                  INT  PRIMARY KEY AUTO_INCREMENT,
  PrivilegeKey        VARCHAR(50) NOT NULL,               -- 权限Key（例：UserManage）
  PrivilegeName       VARCHAR(50) NOT NULL,               -- 权限名称(例：用户管理)
  SortIndex			  INT NOT NULL					      -- 排序索引
);


-- PRINT '------ 二级权限 ------'
CREATE TABLE SecondPrivilege
(
  Id                  INT  PRIMARY KEY AUTO_INCREMENT,
  FirstPrivilegeId    INT NOT NULL,
  PrivilegeKey        VARCHAR(50) NOT NULL,             -- 权限Key（例：DeleteUser）
  PrivilegeName       VARCHAR(50) NOT NULL,             -- 权限名称(例：删除用户)
  SortIndex			  INT NOT NULL					 	-- 排序索引
);


-- PRINT '------ 角色一级权限 ------'
CREATE TABLE RoleFirstPrivilege
(
  Id                  INT  PRIMARY KEY AUTO_INCREMENT,
  RoleId              INT NOT NULL,                      -- 角色id
  FirstPrivilegeId    INT NOT NULL                       -- 角色一级权限Id
);

ALTER TABLE RoleFirstPrivilege  ADD  
  CONSTRAINT FK_RoleFirstPrivilege_Role FOREIGN KEY(RoleId) REFERENCES UserRole (Id);
ALTER TABLE RoleFirstPrivilege  ADD  
  CONSTRAINT FK_RFP_FirstPrivilege FOREIGN KEY(FirstPrivilegeId) REFERENCES FirstPrivilege (Id);


-- PRINT '------ 角色二级权限 ------'
CREATE TABLE RoleSecondPrivilege
(
  Id                  INT  PRIMARY KEY AUTO_INCREMENT,
  RoleId              INT NOT NULL,           -- 角色id
  SecondPrivilegeId   INT NOT NULL            -- 角色一级权限Id
);

ALTER TABLE RoleSecondPrivilege  ADD  
  CONSTRAINT FK_RoleInPrivilege_Role FOREIGN KEY(RoleId) REFERENCES UserRole (Id);
ALTER TABLE RoleSecondPrivilege  ADD  
  CONSTRAINT FK_RFP_SecondPrivilege FOREIGN KEY(SecondPrivilegeId) REFERENCES SecondPrivilege (Id);

-- PRINT '------ 用户部门 ------'
CREATE TABLE  UserDept(
  Id              INT PRIMARY KEY AUTO_INCREMENT,					 -- 部门id 
  ObjectName      VARCHAR(50) NOT NULL,			                     -- 部门名称
  ParentId        INT NOT NULL,						                 -- 父部门id
  FullPath        VARCHAR(260) NOT NULL UNIQUE,                      -- 部门全路径，不能有两个相同的路径
  SortIndex       INT default 0 NOT NULL 			                 -- 排序索引
);
--确保一个部门下的子节点名称不重复
CREATE UNIQUE INDEX IDX_ParentId_ObjectName ON UserDept 
(
  ParentId ASC,
  ObjectName ASC
);

-- PRINT '------ 用户 ------'
CREATE TABLE  SysUser(
  Id                    INT  PRIMARY KEY AUTO_INCREMENT,
  UserDeptId            INT NOT NULL,
  LoginName             VARCHAR(50) NOT NULL UNIQUE,
  UserName              VARCHAR(50) NOT NULL,                          -- 姓名
  UserPassword          VARCHAR(50) NOT NULL,                          -- 密码
  RoleId                INT NOT NULL,                                  -- 角色
  Sex                   INT NOT NULL,                                  -- 性别 0:无 1:男 2:女  
  Mobile                VARCHAR(50) NULL,                              -- 联系电话
  WORK_NO				VARCHAR(50),				                   -- 工号
  LastLoginTime         DATE NULL,                                     -- 最后一次登录时间
  CreateTime            DATE NOT NULL                                  -- 创建时间
);
ALTER TABLE SysUser ADD
  CONSTRAINT FK_SysUser_Dept FOREIGN KEY(UserDeptId) REFERENCES UserDept(Id);

-- PRINT '------ 用户照片 ------'
CREATE TABLE  UserPicture(
  Id                  INT  PRIMARY KEY AUTO_INCREMENT,
  UserId              INT  NOT NULL UNIQUE,
  PictureBytes        BLOB NULL, 
  FileExtend          VARCHAR(50)  NULL                                -- 照片扩展名
);
ALTER TABLE UserPicture ADD
  CONSTRAINT FK_UserPicture_SysUser FOREIGN KEY(UserId) REFERENCES SysUser(Id);


-- PRINT '------ 用户收藏夹 ------'
CREATE TABLE UserFavourite
(
  Id                  INT  PRIMARY KEY AUTO_INCREMENT,
  UserId              INT  NOT NULL,                                   -- 用户id
  FirstPrivilegeId    INT NOT NULL                                     -- 角色一级权限Id
);
CREATE UNIQUE INDEX IDX_UserId_FirstPrivilegeId ON UserFavourite 
(
  UserId ASC,
  FirstPrivilegeId ASC
);
ALTER TABLE UserFavourite ADD  
  CONSTRAINT FK_UserFavourite_SysUser FOREIGN KEY(UserId) REFERENCES SysUser (Id);
ALTER TABLE UserFavourite ADD  
  CONSTRAINT FK_UF_FirstPrivilege FOREIGN KEY(FirstPrivilegeId) REFERENCES FirstPrivilege (Id);

alter table SysUser add unique(WORK_NO);
