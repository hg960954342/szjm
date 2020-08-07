package com.prolog.eis.model.wms;

import java.util.List;

/**
 * 回告wms 实体类
 */
public class EisReportDto {
    private List<EisReport> data;
    private int size;
    private String messageID;

    public List<EisReport> getData() {
        return data;
    }

    public void setData(List<EisReport> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
