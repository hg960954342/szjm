package com.prolog.eis.model.wms;

import java.util.List;

public class Outbt extends OutboundTask {

    private List<OutboundTaskDetail> details;

    public List<OutboundTaskDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OutboundTaskDetail> details) {
        this.details = details;
    }
}
