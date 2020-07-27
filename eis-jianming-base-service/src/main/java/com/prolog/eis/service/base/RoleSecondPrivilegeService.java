package com.prolog.eis.service.base;

public interface RoleSecondPrivilegeService {

	/**
	 * 保存二级角色权限
	 * @date 2018年9月5日 上午10:38:06
	 * @author dengss
	 * @param roleId
	 * @param secondPrivilegeId
	 * @param firstPrivilegeId
	 * @throws Exception
	 */
	public void saveRoleSecondPrivile(int roleId,int secondPrivilegeId,int firstPrivilegeId) throws Exception;
	
	/**
	 * 删除角色二级权限
	 * @date 2018年9月5日 上午10:44:13
	 * @author dengss
	 * @param roleId
	 * @param firstPrivilegeId
	 * @throws Exception
	 */
	public void deleteRoleSecondPrivilege(int roleId,int secondPrivilegeId) throws Exception;
}
