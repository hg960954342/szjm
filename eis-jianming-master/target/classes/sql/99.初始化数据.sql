-- 1号提升机
insert into sx_hoister
values(3,'T01',2,1,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(1,3,3,'T010101','T010101','T010101',10,1,null,null,1,1,28,21,1,SYSDATE(),SYSDATE(),1),
(2,3,1,'T010301','T010301','T010301',20,1,null,null,1,3,62,34,1,SYSDATE(),SYSDATE(),1),
(3,3,1,'T010401','T010401','T010401',20,1,null,null,1,4,62,34,1,SYSDATE(),SYSDATE(),1);

insert into sx_connection_rim
values(4,3,3,'T010102','T010102','T010102',20,1,null,null,1,1,28,24,1,SYSDATE(),SYSDATE(),1),
(5,3,3,'T010202','T010202','T010202',20,1,null,null,1,2,28,24,1,SYSDATE(),SYSDATE(),1),
(6,3,2,'T010302','T010302','T010302',20,1,null,null,1,3,62,37,1,SYSDATE(),SYSDATE(),1),
(7,3,2,'T010402','T010402','T010402',20,1,null,null,1,4,62,37,1,SYSDATE(),SYSDATE(),1);

-- 2号提升机
insert into sx_hoister
values(4,'T02',2,1,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(8,4,1,'T020101','T020101','T020101',10,1,null,null,1,1,33,21,1,SYSDATE(),SYSDATE(),1),
(9,4,1,'T020301','T020301','T020301',20,1,null,null,1,3,67,34,1,SYSDATE(),SYSDATE(),1),
(10,4,1,'T020401','T020401','T020401',20,1,null,null,1,4,67,34,1,SYSDATE(),SYSDATE(),1);

insert into sx_connection_rim
values(11,4,2,'T020102','T020102','T020102',20,1,null,null,1,1,33,24,1,SYSDATE(),SYSDATE(),1),
(12,4,2,'T020202','T020202','T020202',20,1,null,null,1,2,33,24,1,SYSDATE(),SYSDATE(),1),
(13,4,2,'T020302','T020302','T020302',20,1,null,null,1,3,67,37,1,SYSDATE(),SYSDATE(),1),
(14,4,2,'T020402','T020402','T020402',20,1,null,null,1,4,67,37,1,SYSDATE(),SYSDATE(),1);

-- 3号提升机
insert into sx_hoister
values(5,'T02',2,1,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(15,5,2,'T030101','T030101','T030101',20,1,null,null,1,1,20,44,1,SYSDATE(),SYSDATE(),1),
(16,5,1,'T030301','T030301','T030301',20,1,null,null,1,3,54,58,1,SYSDATE(),SYSDATE(),1),
(17,5,1,'T030401','T030401','T030401',20,1,null,null,1,4,54,58,1,SYSDATE(),SYSDATE(),1),
(18,5,1,'T030501','T030501','T030501',20,1,null,null,1,5,44,35,1,SYSDATE(),SYSDATE(),1),
(19,5,1,'T030601','T030601','T030601',20,1,null,null,1,6,44,35,1,SYSDATE(),SYSDATE(),1),
(20,5,1,'T030701','T030701','T030701',20,1,null,null,1,7,44,35,1,SYSDATE(),SYSDATE(),1);

insert into sx_connection_rim
values(21,5,2,'T030102','T030102','T030102',20,1,null,null,1,1,20,47,1,SYSDATE(),SYSDATE(),1),
(23,5,2,'T030302','T030302','T030302',10,1,null,null,1,3,54,62,1,SYSDATE(),SYSDATE(),1),
(24,5,2,'T030502','T030502','T030502',10,1,null,null,1,5,44,40,1,SYSDATE(),SYSDATE(),1);

-- 4号提升机
insert into sx_hoister
values(6,'T02',2,1,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(25,6,3,'T040101','T040101','T040101',20,1,null,null,1,1,23,44,1,SYSDATE(),SYSDATE(),1),
(26,6,3,'T040201','T040201','T040201',20,1,null,null,1,2,23,44,1,SYSDATE(),SYSDATE(),1),
(27,6,3,'T040301','T040301','T040301',20,1,null,null,1,3,57,58,1,SYSDATE(),SYSDATE(),1),
(28,6,3,'T040401','T040401','T040401',20,1,null,null,1,4,57,58,1,SYSDATE(),SYSDATE(),1),
(29,6,3,'T040501','T040501','T040501',20,1,null,null,1,5,47,35,1,SYSDATE(),SYSDATE(),1),
(30,6,3,'T040601','T040601','T040601',20,1,null,null,1,6,47,35,1,SYSDATE(),SYSDATE(),1),
(31,6,3,'T040701','T040701','T040701',20,1,null,null,1,7,47,35,1,SYSDATE(),SYSDATE(),1);

insert into sx_connection_rim
values(32,6,3,'T040102','T040102','T040102',10,1,null,null,1,1,23,47,1,SYSDATE(),SYSDATE(),1),
(34,6,2,'T040302','T040302','T040302',10,1,null,null,1,3,57,62,1,SYSDATE(),SYSDATE(),1),
(35,6,2,'T040502','T040502','T040502',10,1,null,null,1,5,47,40,1,SYSDATE(),SYSDATE(),1);

-- 5号提升机
insert into sx_hoister
values(7,'T02',2,1,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(36,7,2,'T050301','T050301','T050301',20,1,null,null,1,3,65,58,1,SYSDATE(),SYSDATE(),1),
(37,7,3,'T050401','T050401','T050401',20,1,null,null,1,4,65,58,1,SYSDATE(),SYSDATE(),1),
(38,7,3,'T050501','T050501','T050501',20,1,null,null,1,5,55,35,1,SYSDATE(),SYSDATE(),1),
(39,7,3,'T050601','T050601','T050601',20,1,null,null,1,6,55,35,1,SYSDATE(),SYSDATE(),1),
(40,7,3,'T050701','T050701','T050701',20,1,null,null,1,7,55,35,1,SYSDATE(),SYSDATE(),1);

insert into sx_connection_rim
values(41,7,1,'T050302','T050302','T050302',10,1,null,null,1,3,65,62,1,SYSDATE(),SYSDATE(),1),
(42,7,1,'T050303','T050303','T050303',10,1,null,null,1,3,71,61,1,SYSDATE(),SYSDATE(),1),
(43,7,1,'T050502','T050502','T050502',10,1,null,null,1,5,55,40,1,SYSDATE(),SYSDATE(),1),
(44,7,1,'T050503','T050503','T050503',10,1,null,null,1,5,55,38,1,SYSDATE(),SYSDATE(),1);

-- 3层空箱直入口
insert into sx_hoister
values(8,'Z01',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(45,8,2,'Z010301','Z010301','Z010301',20,1,null,null,1,3,75,24,1,SYSDATE(),SYSDATE(),1);

-- 3层空箱出库直通口
insert into sx_hoister
values(9,'Z02',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(47,9,1,'Z020301','Z020301','Z020301',20,1,null,null,1,3,59,58,1,SYSDATE(),SYSDATE(),1);

-- 5层空箱出库直通口
insert into sx_hoister
values(10,'Z03',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(49,10,1,'Z030501','Z030501','Z030501',20,1,null,null,1,5,49,35,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层西
insert into sx_hoister
values(11,'X01',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(51,11,2,'X010101','X010101','X010101',10,1,null,null,1,1,22,15,1,SYSDATE(),SYSDATE(),1),
(52,11,1,'X010102','X010102','X010102',20,1,null,null,1,1,28,21,1,SYSDATE(),SYSDATE(),1),
(53,11,2,'X010103','X010103','X010103',10,1,null,null,1,1,24,14,1,SYSDATE(),SYSDATE(),1),
(54,11,2,'X010104','X010104','X010104',10,1,null,null,1,1,33,14,1,SYSDATE(),SYSDATE(),1),
(68,11,2,'X010105','X010105','X010105',10,1,null,null,1,1,26,15,1,SYSDATE(),SYSDATE(),1),
(69,11,2,'X010106','X010106','X010106',10,1,null,null,1,1,30,15,1,SYSDATE(),SYSDATE(),1),
(70,11,2,'X010107','X010107','X010107',10,1,null,null,1,1,36,15,1,SYSDATE(),SYSDATE(),1);

insert into sx_hoister
values(12,'X02',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(55,12,2,'X010101','X010101','X010101',10,1,null,null,1,1,22,15,1,SYSDATE(),SYSDATE(),1),
(56,12,1,'X020102','X020102','X020102',20,1,null,null,1,1,20,21,1,SYSDATE(),SYSDATE(),1),
(57,12,2,'X010103','X010103','X010103',10,1,null,null,1,1,24,14,1,SYSDATE(),SYSDATE(),1),
(58,12,2,'X010104','X010104','X010104',10,1,null,null,1,1,33,14,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层北入库口
insert into sx_hoister
values(13,'X03',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(59,13,3,'X030102','X030102','X030102',10,1,null,null,1,1,13,58,1,SYSDATE(),SYSDATE(),1),
(60,13,3,'X030101','X030101','X030101',20,1,null,null,1,1,18,59,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层北出库口
insert into sx_hoister
values(14,'X04',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(61,14,2,'X040102','X040102','X040102',10,1,null,null,1,1,13,60,1,SYSDATE(),SYSDATE(),1),
(62,14,1,'X040101','X040101','X040101',20,1,null,null,1,1,17,61,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层北空托入库口
insert into sx_hoister
values(15,'X05',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(63,15,1,'X050101','X050101','X050101',10,1,null,null,1,1,15,64,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层北质检出库口
insert into sx_hoister
values(16,'X06',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(64,16,1,'X060101','X060101','X060101',20,1,null,null,1,1,11,66,1,SYSDATE(),SYSDATE(),1),
(65,16,2,'X060102','X060102','X060102',10,1,null,null,1,1,11,65,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层北质检出库口暂存位1
insert into sx_hoister
values(17,'X07',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(66,17,1,'X070101','X070101','X070101',20,1,null,null,1,1,12,66,1,SYSDATE(),SYSDATE(),1);

-- 添加虚拟mcs提升机 1层北质检出库口暂存位2
insert into sx_hoister
values(18,'X08',2,3,1,2,0,null,SYSDATE());

insert into sx_connection_rim
values(67,18,1,'X080101','X080101','X080101',20,1,null,null,1,1,13,66,1,SYSDATE(),SYSDATE(),1);


