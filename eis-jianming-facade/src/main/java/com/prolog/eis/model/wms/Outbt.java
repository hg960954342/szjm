package com.prolog.eis.model.wms;

import java.util.List;

public class Outbt extends OutboundTask {

    private List<OutboundTaskDetailD> details;

    public List<OutboundTaskDetailD> getDetails() {
        return details;
    }

    public void setDetails(List<OutboundTaskDetailD> details) {
        this.details = details;
    }
}
