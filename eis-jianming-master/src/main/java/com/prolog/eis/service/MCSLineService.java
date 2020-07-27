package com.prolog.eis.service;

public interface MCSLineService {

	void emptyPalletRequest(String deviceNo,String containerNo) throws Exception;
	
	void createEmptyBoxTask(String entryCode,int layer) throws Exception;
	
	void splitOutBound(String deviceNo);
	
	String bcrRequest(String device,String containerNo) throws Exception;
	
	boolean checkAvoid(int layer,int x,int y);
}
