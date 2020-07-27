package com.prolog.eis.service;

import com.prolog.eis.model.eis.LayerPortOrigin;

public interface LayerPortOriginService {

	LayerPortOrigin getPortOrigin(String junctionPort,int layer,int defaultOriginX,int defaultOriginY);
}
