package com.prolog.eis.service.impl.unbound;

import com.prolog.eis.model.wms.OutboundTask;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;

import java.util.List;
import java.util.Set;

public  interface SimilarityDataEntityLoadInterface {


       void addOutboundTask(OutboundTask outboundTask);

      List<DetailDataBean> getOutDetailList() ;

     Set<String> getCrrentBillNoList();
     int getMaxSize();




}
