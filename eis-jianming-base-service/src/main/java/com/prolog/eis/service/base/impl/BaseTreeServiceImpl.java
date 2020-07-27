package com.prolog.eis.service.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.base.BaseTreeMapper;
import com.prolog.eis.model.base.BaseTreeModel;
import com.prolog.eis.service.base.BaseTreeService;

@Service
public class BaseTreeServiceImpl implements BaseTreeService{

	@Autowired
	private BaseTreeMapper baseTreeMapper;

	@Override
	public List<BaseTreeModel> queryBaseTreeNode(String tableName) throws Exception {
		List<BaseTreeModel> Trees = baseTreeMapper.findBaseTree(tableName);
		return Trees;
	}

	@Override
	@Transactional
	public BaseTreeModel saveBaseTreeNode(String tableName, int parentId, String objectName) throws Exception {
		BaseTreeModel baseTreeModel = new BaseTreeModel();
		List<BaseTreeModel> subNodes = baseTreeMapper.findSubNode(tableName, parentId);
		int sortIndex = 0;
		List<String> list = new ArrayList<String>();
		for(int i =0; i<subNodes.size();i++) {
			if(sortIndex < subNodes.get(i).getSortIndex()) {
				sortIndex = subNodes.get(i).getSortIndex();
			}
			list.add(subNodes.get(i).objectName);
		}
		String newObjectName = this.getNewName(list, objectName);
		BaseTreeModel parentNode = baseTreeMapper.findParentNode(tableName, parentId);
		String fullPath = "";
		if(null != parentNode) {
			fullPath = parentNode.getFullPath()+"/"+newObjectName;
		}else {
			fullPath = newObjectName;
		}
		baseTreeModel.setFullPath(fullPath);
		baseTreeModel.setObjectName(newObjectName);
		baseTreeModel.setSortIndex(sortIndex+1);
		baseTreeModel.setParentId(parentId);
		baseTreeMapper.saveBaseTree(tableName, baseTreeModel.getObjectName(), baseTreeModel.getParentId(),
				baseTreeModel.getFullPath(), baseTreeModel.getSortIndex());
		return baseTreeModel;
	}


	public String getNewName(List<String> nameList, String defaultName)throws Exception{
		String nodeName = defaultName;
		int exName = 0;
		while (true){
			boolean  isContainsName = false;
			for (String name : nameList){
				if (name.equals(nodeName)){
					isContainsName = true;
					break;
				}
			}
			if (!isContainsName) break;
			exName++;
			nodeName = defaultName + String.valueOf(exName);
		}
		return nodeName;
	}

	@Override
	@Transactional
	public void updateBaseTreeNode(String tableName, int id, String objectName) throws Exception {
		BaseTreeModel node = baseTreeMapper.findNode(tableName,id);
		String fullPath = "";
		String oldFullPath = "";
		if(null != node) {
			oldFullPath = node.getFullPath();
			List<BaseTreeModel> subNodes = baseTreeMapper.querySubNode(tableName, node.getParentId(),objectName,id);
			if(subNodes.size()>0) {
				throw new Exception(objectName+"该节点名称已被使用");
			}
			if(!node.getFullPath().contains("/")) {
				fullPath = objectName;
			}else {
				String parentPath = node.getFullPath().substring(0,node.getFullPath().lastIndexOf("/"));
				fullPath = parentPath +"/"+objectName;
			}
			//修改objectName
			baseTreeMapper.updateNode(tableName, id, objectName);
			this.updateFullPath(tableName, fullPath, oldFullPath);
		}else{
			throw new Exception("该节点不存在，或已被删除！");
		}
	}

	//修改全路径
	public void updateFullPath(String tableName,String fullPath,String oldFullPath) throws Exception{
		String ofp = oldFullPath+"/%";
		baseTreeMapper.updateFullPath(tableName, fullPath, oldFullPath,ofp);
	}

	@Override
	@Transactional
	public void moveBaseTreeNode(String tableName,int targetId, int parentId) throws Exception {
		// 查询目标节点
		BaseTreeModel targetNode = baseTreeMapper.findNode(tableName,targetId);
		if(null == targetNode) {
			throw new Exception("该节点不存在，或已被删除！");
		}else {
			// 查询移动节点下的所有子节点
			List<BaseTreeModel> subNodes = baseTreeMapper.findSubNode(tableName,parentId);
			int sortIndex = 0;
			for(BaseTreeModel subNode : subNodes) {
				//ojectName进行比较
				if(subNode.getObjectName().equals(targetNode.getObjectName()) && subNode.getId() != targetId) {
					throw new Exception("在目标节点下已存在同名节点："+targetNode.getObjectName());
				}
				if(sortIndex < subNode.getSortIndex()) {
					sortIndex = subNode.getSortIndex();
				}
			}
			String newFullPath = "";
			//查询移动节点
			BaseTreeModel moveNode = baseTreeMapper.findNode(tableName,parentId);
			if(null == moveNode) {
				newFullPath = targetNode.objectName;
			}else {
				newFullPath = moveNode.getFullPath()+"/"+ targetNode.objectName;
			}
			this.updateFullPath(tableName, newFullPath, targetNode.getFullPath());
			baseTreeMapper.updateSortIndex(tableName, sortIndex+1,parentId,targetNode.getId());
		}
	}
}


