package com.prolog.eis.service;

import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.wms.AgvStorageLocation;

public interface AgvStorageLocationService {
    AgvStorageLocation findByCoord(Coordinate coordinate);

    AgvStorageLocation findByRcs(String location);
}
