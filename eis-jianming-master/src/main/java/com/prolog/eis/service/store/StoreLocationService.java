package com.prolog.eis.service.store;

import com.prolog.eis.dto.sxk.StoreLocationGroupDto;
import com.prolog.eis.model.sxk.SxStoreLocation;

import java.util.List;

public interface StoreLocationService {

	public void importStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos)throws Exception;

	public List<SxStoreLocation> findByGroupId(int groupId) throws Exception;

	/**
	 * 导入垂直货位数据
	 * @param verticalLocationDtos
	 * @return
	 *//*
	List<VerticalLocationDto> importVerticalLocation(List<VerticalLocationDto> verticalLocationDtos) throws Exception;*/
}
