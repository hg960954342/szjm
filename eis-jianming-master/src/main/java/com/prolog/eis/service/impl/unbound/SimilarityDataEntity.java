package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.model.wms.OutboundTask;

public class SimilarityDataEntity {
    private float similarity;
    private OutboundTask OutboundTask;

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public com.prolog.eis.model.wms.OutboundTask getOutboundTask() {
        return OutboundTask;
    }

    public void setOutboundTask(com.prolog.eis.model.wms.OutboundTask outboundTask) {
        OutboundTask = outboundTask;
    }
}
