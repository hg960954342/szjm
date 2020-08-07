package com.prolog.eis.model.wms;

import java.util.List;

/**
 * 盘点回告wms 明细 实体类
 * 订单、商品、批号、分摊数量、托盘、点位。
 */
public class EisReport {

    //单据号
    private String billNo;

    //类型
    private String taskType;
    //明细
    private List<ReportDateil> dateils;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public List<ReportDateil> getDateils() {
        return dateils;
    }

    public void setDateils(List<ReportDateil> dateils) {
        this.dateils = dateils;
    }
}
