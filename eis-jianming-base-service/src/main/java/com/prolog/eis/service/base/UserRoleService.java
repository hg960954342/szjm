package com.prolog.eis.service.base;

import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.model.base.UserRole;

import java.util.List;

public interface UserRoleService {

	/**
	 * 查询角色下的所有权限
	 * @date 2018年8月31日 上午11:40:38
	 * @author dengss
	 * @param roleId
	 * @return
	 */
	public List<AllPrivilegeRespDto> findUserRole(int roleId) throws Exception;
	
	/**
	 * 新增角色
	 * @date 2018年9月4日 下午6:53:47
	 * @author dengss
	 * @param roleName
	 * @throws Exception
	 */
	public void saveUserRole(String roleName)throws Exception;
	
	/**
	 * 修改角色
	 * @date 2018年9月4日 下午6:54:43
	 * @author dengss
	 * @param id
	 * @param roleName
	 * @throws Exception
	 */
	public void updateUserRole(int id,String roleName)throws Exception;
	/**
	 * 删除角色
	 * @date 2018年9月26日 上午11:49
	 * @author huhao
	 * @param id
 	 * @throws Exception
	 */
	public void deleteUserRole(int id)throws Exception;
	
	/**
	 * 查询所有的角色
	 * @date 2018年9月5日 上午9:56:22
	 * @author dengss
	 * @return
	 * @throws Exception
	 */
	public List<UserRole> findAllUserRole()throws Exception;
	/**
	 * 修改角色排序
	 * @date 2018年9月5日 上午9:56:22
	 * @author dengss
	 * @return
	 * @throws Exception
	 */
	public void updateSortIndex(List<Integer> ids);
}
