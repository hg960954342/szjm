package com.prolog.eis.service.base;

public interface RoleFirstPrivilegeService {

	/**
	 * 保存角色一级权限
	 * @date 2018年9月5日 上午9:39:24
	 * @author dengss
	 * @throws Exception
	 */
	public void saveRoleFirstPrivilege(int roleId,int firstPrivilegeId) throws Exception;
	
	/**
	 * 删除角色一级权限
	 * @date 2018年9月5日 上午10:44:13
	 * @author dengss
	 * @param roleId
	 * @param firstPrivilegeId
	 * @throws Exception
	 */
	public void deleteRoleFirstPrivilege(int roleId,int firstPrivilegeId) throws Exception;
}
