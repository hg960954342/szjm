package com.prolog.eis.service.base;

public interface SysParameService {

	/**
	 * 获取每层预留货位
	 * @param layer
	 * @return
	 */
	int getLayerReserveCount(int layer);
}
