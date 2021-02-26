package com.prolog.eis.model.wms;

import java.util.List;

public class Outbt extends OutboundTask {

    private List<OutboundTaskDetailDto> details;

    public List<OutboundTaskDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<OutboundTaskDetailDto> details) {
        this.details = details;
    }
}
