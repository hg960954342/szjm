package com.prolog.eis.service.base;

import com.prolog.eis.model.base.BaseTreeModel;

import java.util.List;

public interface UserDeptService {

	/**
	 * 查询部门树
	 * @date 2018年9月5日 下午5:26:38
	 * @author dengss
	 * @return
	 */
	public List<BaseTreeModel> queryDeptTreeNode() throws Exception;
	
	
	/**
	 * 新增部门树
	 * @date 2018年9月5日 下午5:26:38
	 * @author dengss
	 * @return
	 */
	public BaseTreeModel saveDeptTreeNode(int parentId,String objectName) throws Exception;
	
	/**
	 * 修改部门树节点
	 * @date 2018年9月6日 下午2:34:08
	 * @author dengss
	 * @param id
	 * @param objectName
	 * @throws Exception
	 */
	public void updateNode(int id,String objectName) throws Exception;
	
	/**
	 * 移动部门树
	 * @date 2018年9月8日 下午2:51:29
	 * @author dengss
	 * @param targetId
	 * @param parentId
	 * @throws Exception
	 */
	public void moveDeptNode(int targetId, int parentId) throws Exception;
	
	/**
	 * 修改部门树排序
	 * @date 2018年9月8日 下午5:04:34
	 * @author dengss
	 * @param ids
	 * @throws Exception
	 */
	public void updateSoreIndex(List<Integer> ids) throws Exception;
}
