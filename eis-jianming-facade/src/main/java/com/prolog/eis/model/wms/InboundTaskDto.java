package com.prolog.eis.model.wms;

import java.util.List;


public class InboundTaskDto {

    private List<InboundTask> data;
    private int size;
    private String messageID;

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

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }
}
