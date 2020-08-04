package com.prolog.eis.dto.eis;

import java.util.List;

/**
 * 回告wms 实体类
 */
public class EisReportDto {
    private List<EisReport> data;

    public List<EisReport> getData() {
        return data;
    }

    public void setData(List<EisReport> data) {
        this.data = data;
    }
}
