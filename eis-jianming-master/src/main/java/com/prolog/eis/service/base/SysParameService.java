package com.prolog.eis.service.base;

import java.util.List;

public interface SysParameService {

	/**
	 * 获取每层预留货位
	 * @param layer
	 * @return
	 */
	int getLayerReserveCount(List<Integer> layer);
}
