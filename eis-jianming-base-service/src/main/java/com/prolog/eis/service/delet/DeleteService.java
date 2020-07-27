package com.prolog.eis.service.delet;

public interface DeleteService {

	/**
	 * 删除部门节点
	 * 
	 * @date 2018年9月8日 下午4:36:23
	 * @author dengss
	 * @param id
	 * @throws Exception
	 */
	public void DeleteUserDept(int id) throws Exception;

	/**
	 * 删除用户
	 * 
	 * @date 2018年9月8日 下午5:55:55
	 * @author dengss
	 * @param id
	 * @throws Exception
	 */
	public void DeleteSysUser(int id) throws Exception;
}
