package com.prolog.eis.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prolog.eis.dao.base.SysParameMapper;
import com.prolog.eis.model.base.SysParame;
import com.prolog.eis.service.base.SysParameService;
import com.prolog.framework.utils.MapUtils;

@Service
public class SysParameServiceImpl implements SysParameService {

	@Autowired
	private SysParameMapper sysParameMapper;
	
	@Override
	public List<SysParame> getSysParames() {
		// TODO Auto-generated method stub
	 	return sysParameMapper.getSysParames();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void setSysParameValue(String key, String value) throws Exception {
		// TODO Auto-generated method stub
		List<SysParame> sysParames = sysParameMapper.findByMap(MapUtils.put("parameNo", key).getMap(), SysParame.class);
		if(sysParames.size() == 1) {
			SysParame sysParame = sysParames.get(0);
			if(sysParame.getIsReadOnly() != 0) {
				throw new Exception("参数只读，不可修改");
			}
			
			int parameType = sysParame.getParameType();
			switch (parameType) {
			case 1:
				break;
			case 2:
				Boolean str2Result = value.matches("^-?\\d+$"); 
				if(!str2Result) {
					throw new Exception("请输入整数");
				}
				break;
			case 3:
				Boolean str3Result = value.matches("^(-?\\d+)(\\.\\d+)?$"); 
				if(!str3Result) {
					throw new Exception("请输入数字");
				}
				break;
			default:
				throw new Exception("parameType异常");
			}
			
			sysParameMapper.updateMapById(sysParame.getParameNo(), MapUtils.put("parameValue", value).getMap(), SysParame.class);
		}else {
			if(sysParames.size() > 0) {
				throw new Exception("key:" + key + "存在多个");
			}else {
				throw new Exception("key:" + key + "不存在");
			}
		}
	}
}
