package com.prolog.eis.model.wms;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;


public class OutboundTaskDto {

    private List<Outbt> data;

    private int size;

    @JSONField(name="MessageID")
    private String messageId;


    public List<Outbt> getData() {
        return data;
    }

    public void setData(List<Outbt> data) {
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
