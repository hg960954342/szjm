package com.prolog.eis.service.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.base.BatchSqlMapper;
import com.prolog.eis.service.base.BatchSqlService;

@Service
public class BatchSqlServiceImpl implements BatchSqlService {
	@Autowired
	private BatchSqlMapper batchSqlMapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public void pubInsertSql(List list,String tableName) {
		int init = 1000;// 每隔1000条循环一次
		int total = list.size();
		int cycelTotal = total / init;
		if (total % init != 0) {
			cycelTotal += 1;
			if (total < init) {
				init = list.size();
			}
		}
		List list2 = new ArrayList<>();
		for (int i = 0; i < cycelTotal; i++) {
			for (int j = 0; j < init; j++) {
				if (j + 1 > list.size()) {
					break;
				}
				if (list.get(j) == null) {
					break;
				}
				list2.add(list.get(j));
			}
			batchSqlMapper.pubInsertSql(list2,tableName);
			list.removeAll(list2);// 移出已经保存过的数据
			list2.clear();// 移出当前保存的数据
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public void pubUpdateSql(List list,String tableName) {
		int init = 1000;// 每隔1000条循环一次
		int total = list.size();
		int cycelTotal = total / init;
		if (total % init != 0) {
			cycelTotal += 1;
			if (total < init) {
				init = list.size();
			}
		}
		List list2 = new ArrayList<>();
		for (int i = 0; i < cycelTotal; i++) {
			for (int j = 0; j < init; j++) {
				if (j + 1 > list.size()) {
					break;
				}
				if (list.get(j) == null) {
					break;
				}
				list2.add(list.get(j));
			}
			batchSqlMapper.pubUpdateSql(list2,tableName);
			list.removeAll(list2);// 移出已经保存过的数据
			list2.clear();// 移出当前保存的数据
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void pubDeleteSql(List list,String tableName) {

	}

	 
}
