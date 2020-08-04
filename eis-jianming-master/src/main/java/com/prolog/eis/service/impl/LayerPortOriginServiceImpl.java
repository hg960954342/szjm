package com.prolog.eis.service.impl;

import com.prolog.eis.dao.LayerPortOriginMapper;
import com.prolog.eis.model.eis.LayerPortOrigin;
import com.prolog.eis.service.LayerPortOriginService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LayerPortOriginServiceImpl implements LayerPortOriginService{

	@Autowired
	private LayerPortOriginMapper layerPortOriginMapper;
	
	@Override
	public LayerPortOrigin getPortOrigin(String junctionPort,int layer,int defaultOriginX,int defaultOriginY){
		//获取配置的原点
		//
		List<LayerPortOrigin> layerPortOrigins = layerPortOriginMapper.findByMap(MapUtils.put("entryCode", junctionPort).put("layer", layer).getMap(), LayerPortOrigin.class);
		if(layerPortOrigins.isEmpty()) {
			//记录下哪些接驳点层没有设置原点
			FileLogHelper.WriteLog("getPortOrigin原点配置", "接驳点" + junctionPort + "层" + layer + "未设置原点");
			
			LayerPortOrigin layerPortOrigin = new LayerPortOrigin();
			layerPortOrigin.setOriginX(defaultOriginX);
			layerPortOrigin.setOriginY(defaultOriginY);
			
			return layerPortOrigin;
		}else {
			return layerPortOrigins.get(0);
		}
	}
}
