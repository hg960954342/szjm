package com.prolog.eis.service.simulator;

import java.util.List;

import com.prolog.eis.model.middle.WmsRawTrkInterface;

public interface BuildTestDataService {

	long wmsRawTrkInterface(List<WmsRawTrkInterface> list);

	List<WmsRawTrkInterface> getWmsRawTrkInterface();
}
