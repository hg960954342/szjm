package com.prolog.eis.model.wms;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class InboundTaskDto {

    private List<InboundTask> data;

    private int size;

    @JsonProperty("messageID")
    private String messageId;

    public List<InboundTask> getData() {
        return data;
    }

    public void setData(List<InboundTask> data) {
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
