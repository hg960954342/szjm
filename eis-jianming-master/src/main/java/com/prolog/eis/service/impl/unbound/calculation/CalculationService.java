package com.prolog.eis.service.impl.unbound.calculation;

import com.prolog.eis.dto.sxk.OutBoundSxStoreDto;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;

import java.util.List;

public interface CalculationService<T> {
    public List<T> buildOutBoundQty(DetailDataBean detailDataBeand, float miniPackage, boolean isPickStation);

}
