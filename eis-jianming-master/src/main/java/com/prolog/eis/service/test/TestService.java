package com.prolog.eis.service.test;

public interface TestService {

	void deleteStore(String containerNo) throws Exception;
	 Integer updateIsLockByLayer(int isLock,int layer);
}
