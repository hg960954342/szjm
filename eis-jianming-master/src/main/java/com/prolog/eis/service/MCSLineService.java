package com.prolog.eis.service;

public interface MCSLineService {
		
	void splitOutBound(String deviceNo);
	
	void buildEmptyContainerSupply() throws Exception;
}
