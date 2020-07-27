package com.prolog.eis.service.middle;

public interface MiddleTableNoticeService {
	public void noticeWmsRawTrkInterfaceInbound();
	
	public void noticeWmsRawTrkInterfaceOutbound();

	public void noticeWmsBaBinstaR();
	
	public void noticeWmsDispatchingInterface();
	
	public void noticeWmsSetPort();
}
