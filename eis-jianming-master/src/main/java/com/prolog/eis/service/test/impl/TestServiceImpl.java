package com.prolog.eis.service.test.impl;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.service.test.TestService;
import com.prolog.framework.utils.MapUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class TestServiceImpl implements TestService{

	@Autowired
	private SxStoreMapper sxStoreMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreTaskFinishService sxStoreTaskFinishService;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteStore(String containerNo) throws Exception {

		clearSxStore(containerNo);
	}

	//判断有无库存
	private SxStore clearSxStore(String containerNo) throws Exception {
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
				SxStore.class);
		if (sxStores.size() == 1) {
			SxStore sxStore = sxStores.get(0);
			Integer storeLocationId = sxStore.getStoreLocationId();
			SxStoreLocation cksxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
			// 根据出库任务类型转换
			sxStoreMapper.deleteByContainer(containerNo);
			sxStoreLocationMapper.updateMapById(storeLocationId, MapUtils.put("actualWeight", 0).getMap(),
					SxStoreLocation.class);
			sxStoreTaskFinishService.computeLocation(sxStore);
			sxStoreLocationGroupMapper.updateMapById(cksxStoreLocation.getStoreLocationGroupId(),
					MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);

			return sxStore;
		}else {
			return null;
		}
	}

	@Override
	public Integer updateIsLockByLayer(int isLock,int layer){
		return sxStoreLocationGroupMapper.updateIsLockByLayer(isLock,layer);
	}

	@Override
	public List<SxStoreViewDto> getSxStoreViewDto(int layer){
         return sxStoreMapper.getSxStoreViewDtoByLayer(layer);
	}

	@Override
	public List<SxStoreViewSimpleDto> getSxStoreViewDtoSimpleDto(int layer){
		return sxStoreMapper.getSxStoreViewDtoSimpleByLayer(layer);
	}


	@Override
	public SxStoreViewMapDto getSxStoreViewMapDtoByLayer(int layer){

		return sxStoreMapper.getSxStoreViewMapDtoByLayer(layer);
	}

	@Override
	public String getSxStoreContainerNo(int layer,int x,int y){

		return sxStoreMapper.getSxStoreContainerNo(layer,x,y);
	}
}
