package com.prolog.eis.service.test;

import com.prolog.eis.service.test.impl.SxStoreViewDto;
import com.prolog.eis.service.test.impl.SxStoreViewSimpleDto;

import java.util.List;

public interface TestService {

	void deleteStore(String containerNo) throws Exception;
	 Integer updateIsLockByLayer(int isLock,int layer);
	 List<SxStoreViewDto> getSxStoreViewDto(int layer);
	 List<SxStoreViewSimpleDto> getSxStoreViewDtoSimpleDto(int layer);
}
