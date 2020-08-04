package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.model.wms.OutboundTask;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Component
@Scope(SCOPE_SINGLETON)
public class SimilarityDataEntityLoad {

    private  long similarity;
    private OutboundTask outboundTask;


    public long getSimilarity() {
        return similarity;
    }

    public void setSimilarity(long similarity) {
        this.similarity = similarity;
    }

    public OutboundTask getOutboundTask() {
        return outboundTask;
    }

    public void setOutboundTask(OutboundTask outboundTask) {
        this.outboundTask = outboundTask;
    }
}
