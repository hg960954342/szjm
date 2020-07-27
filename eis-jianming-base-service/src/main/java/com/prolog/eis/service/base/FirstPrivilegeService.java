package com.prolog.eis.service.base;

import java.util.List;

import com.prolog.eis.model.base.FirstPrivilege;

public interface FirstPrivilegeService {

	/**
	 * 查询所有的一级权限
	 * @date 2018年8月30日 下午3:12:58
	 * @author dengss
	 * @return
	 * @throws Exception
	 */
	public List<FirstPrivilege> findFirstPrivilege() throws Exception;
	
	/**
	 * 新增一级权限
	 * @date 2018年9月3日 下午2:38:20
	 * @author dengss
	 * @param firstPrivilege
	 * @throws Exception
	 */
	public void saveFirstPrivilege(FirstPrivilege firstPrivilege) throws Exception;
	
	/**
	 * 修改一级权限
	 * @date 2018年9月3日 下午2:40:21
	 * @author dengss
	 * @param firstPrivilege
	 * @throws Exception
	 */
	public void updateFirstPrivilege(FirstPrivilege firstPrivilege) throws Exception;
	
	/**
	 * 删除一级权限
	 * @date 2018.09.26 AM10:59
	 * @author huhao
	 * @param id
	 * @throws Exception
	 */
	public void deleteFirstPrivilege(int  id) throws Exception;
	/**
	 * 修改一级权限排序
	 * @date 2018.09.27 AM09:31
	 * @author huhao
	 * @param List<Integer> ids
	 * @throws Exception
	 */
	public void updateSortIndex(List<Integer> ids);
}
