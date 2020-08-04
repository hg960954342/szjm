package com.prolog.eis.service.emptycase.impl;

import com.prolog.eis.dao.EmptyCaseConfigMapper;
import com.prolog.eis.dto.eis.EmptyCaseLayerDto;
import com.prolog.eis.model.eis.EmptyCaseConfig;
import com.prolog.eis.service.emptycase.EmptyCaseConfigService;
import com.prolog.eis.util.ListHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmptyCaseConfigServiceImpl implements EmptyCaseConfigService{

	@Autowired
	private EmptyCaseConfigMapper emptyCaseConfigMapper;
	
	@Override
	public List<List<Integer>> getEmptyCaseLayer(int defaultLayer,int minLayer,int maxLayer){
		
		//获取空箱配置
		List<EmptyCaseConfig> emptyCaseConfigList = emptyCaseConfigMapper.getEmptyCaseConfigs();
		//获取空箱库存数量
		List<EmptyCaseLayerDto> emptyCaseLayerDtoList = emptyCaseConfigMapper.getEmptyCaseStores();
		
		int bestLayer = 0;
		for (EmptyCaseConfig emptyCaseLayerDto : emptyCaseConfigList) {
			int layer = emptyCaseLayerDto.getLayer();
			//判断当前层的空箱库存数是否大于配置数
			EmptyCaseLayerDto temEmptyCaseLayer = ListHelper.firstOrDefault(emptyCaseLayerDtoList, p->p.getLayer() == layer);
			if(null == temEmptyCaseLayer) {
				//找到当前层
				bestLayer = layer;
				break;
			}else {
				if(emptyCaseLayerDto.getMinCount() > temEmptyCaseLayer.getEmptyCaseCount()) {
					//找到当前层
					bestLayer = layer;
					break;
				}
			}
		}
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if(bestLayer > 0) {
			if(minLayer <= bestLayer && bestLayer <= maxLayer) {
				List<Integer> firstLayers = new ArrayList<Integer>();
				firstLayers.add(bestLayer);
				
				result.add(firstLayers);
			}else {
				if(bestLayer > maxLayer) {
					List<Integer> firstLayers = new ArrayList<Integer>();
					firstLayers.add(maxLayer);
					
					result.add(firstLayers);
				}else {
					List<Integer> firstLayers = new ArrayList<Integer>();
					firstLayers.add(minLayer);
					
					result.add(firstLayers);
				}
			}
		}else {
			List<Integer> firstLayers = new ArrayList<Integer>();
			firstLayers.add(defaultLayer);
			
			result.add(firstLayers);
		}
		
		//添加除2层外其他层
		List<Integer> secondLayers = new ArrayList<Integer>();
		for(int i = 1;i<=7;i++) {
			if(i!=2 && minLayer <= i && i <= maxLayer) {
				secondLayers.add(i);	
			}
		}
		if(!secondLayers.isEmpty()) {
			result.add(secondLayers);
		}
		
		//添加2层
		if(minLayer <= 2 && 2 <= maxLayer) {
			List<Integer> thirdLayers = new ArrayList<Integer>();
			thirdLayers.add(2);
			result.add(thirdLayers);
		}
		
		return result;
	}
}
