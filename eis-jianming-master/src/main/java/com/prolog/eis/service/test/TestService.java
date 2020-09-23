package com.prolog.eis.service.test;

import com.prolog.eis.service.test.impl.SxStoreViewDto;
import com.prolog.eis.service.test.impl.SxStoreViewMapDto;
import com.prolog.eis.service.test.impl.SxStoreViewSimpleDto;

import java.util.List;

public interface TestService {

	void deleteStore(String containerNo) throws Exception;
	 Integer updateIsLockByLayer(int isLock,int layer);
	 List<SxStoreViewDto> getSxStoreViewDto(int layer);
	 List<SxStoreViewSimpleDto> getSxStoreViewDtoSimpleDto(int layer);
	SxStoreViewMapDto getSxStoreViewMapDtoByLayer(int layer);
	public String getSxStoreContainerNo(int layer,int x,int y);
	Object getLogViewMCSData(int pq_curpage, int pq_rpp);
	Object getLogViewRCSData(int pq_curpage, int pq_rpp);
}
