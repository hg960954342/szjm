package com.prolog.eis.service.impl;

import com.prolog.eis.dao.RepeatReportMapper;
import com.prolog.eis.model.wms.RepeatReport;
import com.prolog.eis.service.RepeatReportService;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RepeatReportServiceImpl implements RepeatReportService {
    @Autowired
    private RepeatReportMapper repeatReportMapper;

    @Override
    public void insert(RepeatReport repeatReport) {
        repeatReportMapper.save(repeatReport);
    }

    @Override
    public List<RepeatReport> findByState(int state) {
        Map<String, Object> map = MapUtils.put("reportState", state).getMap();
        return repeatReportMapper.findByMap(map,RepeatReport.class);
    }

    @Override
    public void update(RepeatReport repeatReport) {
        repeatReportMapper.update(repeatReport);
    }
}
