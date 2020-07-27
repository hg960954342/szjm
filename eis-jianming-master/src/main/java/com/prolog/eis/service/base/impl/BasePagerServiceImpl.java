package com.prolog.eis.service.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prolog.eis.dao.base.BasePagerMapper;
import com.prolog.eis.dto.base.DataEntity;
import com.prolog.eis.service.base.BasePagerService;

@Service
public class BasePagerServiceImpl implements BasePagerService{

	@Autowired
	private BasePagerMapper basePagerMapper;

	@Override
	public List<DataEntity> getPagers(String columns, String tableName,String conditions,String orders,int startRowNum, int endRowNum) {
		startRowNum = startRowNum-1;
		endRowNum = endRowNum-1;
		List<Map<String, Object>> list = basePagerMapper.getPager(columns, tableName, conditions, orders, startRowNum,endRowNum);
 		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		String[] keyStr = columns.split(",");
		Map<String, String> keyMap = new HashMap<String, String>();
		for (int i = 0; i < keyStr.length; i++) {
			keyMap.put(keyStr[i].toUpperCase(), keyStr[i]);
		}
		for (Map<String, Object> map : list) {
			@SuppressWarnings("rawtypes")
			Iterator iter = map.entrySet().iterator();
			Map<String, Object> newMap = new HashMap<String, Object>();			
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				String key = entry.getKey().toString();
				Object val = entry.getValue();
				if (keyMap.containsValue(key)) {
					String newKey = keyMap.get(key.toUpperCase()).toString();
					newMap.put(newKey, val);
				}
			}
			newList.add(newMap);
		}		
		
		List<DataEntity> dataEntitieList = new ArrayList<DataEntity>();
		for (Map<String, Object> map : newList) {
				DataEntity dataEntity = new DataEntity();
  			 	dataEntity.setDatas(map);
  				dataEntitieList.add(dataEntity);
 		}
 		return dataEntitieList;
	}

	@Override
	public int getTotalCount(String tableName,String conditions) {
		int count = basePagerMapper.getToalCount(tableName,conditions);
 		return count;
	}
}
