package com.prolog.eis.service.impl.unbound.calculation;

import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;

import java.math.BigDecimal;
import java.util.List;

public interface CalculationService<T> {
    public List<T> buildOutBoundQty(DetailDataBean detailDataBeand, BigDecimal miniPackage, boolean isPickStation);

}
