package com.prolog.eis.service.test;

import com.prolog.eis.dto.sxk.TestBuildSxStoreDto;
import com.prolog.eis.service.test.impl.SxStoreViewDto;
import com.prolog.eis.service.test.impl.SxStoreViewMapDto;
import com.prolog.eis.service.test.impl.SxStoreViewSimpleDto;

import java.util.List;
import java.util.Map;

public interface TestService {

	void deleteStore(String containerNo) throws Exception;
	 Integer updateIsLockByLayer(int isLock,int layer);
	 List<SxStoreViewDto> getSxStoreViewDto(int layer);
	 List<SxStoreViewSimpleDto> getSxStoreViewDtoSimpleDto(int layer);
	SxStoreViewMapDto getSxStoreViewMapDtoByLayer(int layer);
	public String getSxStoreContainerNo(int layer,int x,int y);
	Object getLogViewMCSData(int pq_curpage, int pq_rpp);
	Object getLogViewRCSData(int pq_curpage, int pq_rpp);
	List<String> getSxStoreList(String itemName,String itemValue);
	Object listSxStoreQuery(String itemId, String lotId, String ownerId,String itemName,String lot, Integer pqCurpage, Integer pqRpp);
	Object buildSxStore(TestBuildSxStoreDto testBuildSxStoreDto);
}
