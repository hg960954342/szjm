package com.prolog.eis.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.dao.base.BaseSortIndexMapper;
import com.prolog.eis.model.base.BaseTreeModel;
import com.prolog.eis.service.base.BaseTreeService;
import com.prolog.eis.service.base.UserDeptService;

@Service
public class UserDeptServiceImpl implements UserDeptService{

	@Autowired
	private BaseTreeService baseTreeService;
	@Autowired
	private BaseSortIndexMapper baseSortIndexMapper;
	
	@Override
	public List<BaseTreeModel> queryDeptTreeNode() throws Exception {
		return baseTreeService.queryBaseTreeNode("UserDept");
	}

	@Override
	public BaseTreeModel saveDeptTreeNode(int parentId, String objectName) throws Exception {
		BaseTreeModel baseTreeModel =baseTreeService.saveBaseTreeNode("UserDept", parentId, objectName);
		return baseTreeModel;
	}

	@Override
	public void updateNode(int id, String objectName) throws Exception {
		baseTreeService.updateBaseTreeNode("UserDept", id, objectName);
	}

	@Override
	public void moveDeptNode(int targetId, int parentId) throws Exception {
		baseTreeService.moveBaseTreeNode("UserDept", targetId, parentId);
	}

	@Override
	public void updateSoreIndex(List<Integer> ids) throws Exception {
		for(int i =0; i < ids.size(); i++) {
			baseSortIndexMapper.updateSortIndex("UserDept",i+1, ids.get(i));
		}
	}
	

}
