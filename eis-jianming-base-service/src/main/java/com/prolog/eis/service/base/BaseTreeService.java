package com.prolog.eis.service.base;

import com.prolog.eis.model.base.BaseTreeModel;

import java.util.List;

public interface BaseTreeService {

	/**
	 * 查询无极数
	 * @date 2018年9月5日 下午5:14:59
	 * @author dengss
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<BaseTreeModel> queryBaseTreeNode(String tableName) throws Exception;
	
	/**
	 * 保存无极树
	 * @date 2018年9月6日 下午12:47:55
	 * @author dengss
	 * @param tableName
	 * @param parentId
	 * @param objectName
	 * @return
	 * @throws Exception
	 */
	public BaseTreeModel saveBaseTreeNode(String tableName,int parentId,String objectName) throws Exception;
	
	/**
	 * 修改objectName名称和fullPath
	 * @date 2018年9月6日 下午1:52:29
	 * @author dengss
	 * @param tableName
	 * @param id
	 * @param objectName
	 * @throws Exception
	 */
	public void updateBaseTreeNode(String tableName,int id,String objectName) throws Exception;
	
	/**
	 * 移动节点
	 * @date 2018年9月6日 下午5:02:11
	 * @author dengss
	 * @param targetParentId
	 */
	public void moveBaseTreeNode(String tableName,int targetId,int parentId) throws Exception;

}
