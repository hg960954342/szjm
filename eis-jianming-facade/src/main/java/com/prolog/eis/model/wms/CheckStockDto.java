package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CheckStockDto {

    private List<CheckStock> data;

    private int size;

    @JsonProperty("MessageID")
    private String messageId;


    public List<CheckStock> getData() {
        return data;
    }

    public void setData(List<CheckStock> data) {
        this.data = data;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
