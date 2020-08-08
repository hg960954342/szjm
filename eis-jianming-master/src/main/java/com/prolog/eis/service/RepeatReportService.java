package com.prolog.eis.service;

import com.prolog.eis.model.wms.RepeatReport;

import java.util.List;

/**
 * 回告wms 重复表Service
 */

public interface RepeatReportService {

    void insert(RepeatReport repeatReport);


    List<RepeatReport> findByState(int state);

    void update(RepeatReport repeatReport);
}
