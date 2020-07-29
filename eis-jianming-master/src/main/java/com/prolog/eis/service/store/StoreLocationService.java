package com.prolog.eis.service.store;

import java.util.List;

import com.prolog.eis.dto.sxcsc.location.StoreLocationGroupDto;
import com.prolog.eis.dto.sxcsc.location.VerticalLocationDto;
import com.prolog.eis.model.store.SxStoreLocation;

public interface StoreLocationService {

	public void importStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos)throws Exception;

	public List<SxStoreLocation> findByGroupId(int groupId) throws Exception;

	/**
	 * 导入垂直货位数据
	 * @param verticalLocationDtos
	 * @return
	 */
	List<VerticalLocationDto> importVerticalLocation(List<VerticalLocationDto> verticalLocationDtos) throws Exception;
}
