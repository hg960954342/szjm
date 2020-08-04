package com.prolog.eis.service.base;

import com.prolog.eis.dto.base.AllPrivilegeRespDto;
import com.prolog.eis.model.base.SecondPrivilege;

import java.util.List;

public interface SecondPrivilegeService {

	/**
	 * 查询一级权限下的二级权限
	 * @date 2018年8月30日 下午3:35:26
	 * @author dengss
	 * @param firstPrivilegeId
	 * @return
	 */
	public List<SecondPrivilege> findSecondPrivilege(int firstPrivilegeId) throws Exception;
	
	/**
	 * 新增二级权限
	 * @date 2018年9月3日 下午3:16:25
	 * @author dengss
	 * @param secondPrivilege
	 * @throws Exception
	 */
	public void saveSecondPrivilege(SecondPrivilege secondPrivilege) throws Exception;
	
	/**
	 * 修改二级权限
	 * @date 2018年9月3日 下午3:17:08
	 * @author dengss
	 * @param secondPrivilege
	 * @throws Exception
	 */
	public void updateSecondPrivilege(SecondPrivilege secondPrivilege) throws Exception;
	/**
	 * 删除二级权限
	 * @date 2018年9月26日上午11:34
	 * @author huhao
	 * @param id
	 * @throws Exception
	 */
	public void deleteSecondPrivilege(int id) throws Exception;
	/**
	 * 查询所有的一级二级权限
	 * @date 2018年9月3日 下午3:15:05
	 * @author dengss
	 * @return
	 * @throws Exception
	 */
	public List<AllPrivilegeRespDto> allAllPrivilegeResp() throws Exception;
	/**
	 *   修改二级排序
	 * @date 2018年9月27日 上午09:34
	 * @author huhao
	 * @return void
	 * @throws Exception
	 */
	public void updateSortIndex(List<Integer> ids);
}
