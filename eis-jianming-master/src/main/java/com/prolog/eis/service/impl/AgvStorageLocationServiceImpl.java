package com.prolog.eis.service.impl;

import com.prolog.eis.dao.AgvStorageLocationMapper;
import com.prolog.eis.dto.base.Coordinate;
import com.prolog.eis.model.wms.AgvStorageLocation;
import com.prolog.eis.service.AgvStorageLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgvStorageLocationServiceImpl implements AgvStorageLocationService {
    @Autowired
    private AgvStorageLocationMapper agvStorageLocationMapper;
    @Override
    public AgvStorageLocation findByCoord(Coordinate coordinate) {
        int layer = coordinate.getLayer();
        int x = coordinate.getX();
        int y = coordinate.getY();
       /* Map<String, Object> map = MapUtils.put("ceng", layer).put("x", x).put("y", y).getMap();
        agvStorageLocationMapper.findByMap(map,AgvStorageLocation.class);*/
        return agvStorageLocationMapper.findByCoord(layer,x,y);
    }

    @Override
    public AgvStorageLocation findByRcs(String location) {
        return agvStorageLocationMapper.findByRcs(location);
    }

    /*public static void main(String[] args) {
        String coord = "0300010002";

    }*/
}
