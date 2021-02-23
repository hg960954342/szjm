package com.prolog.eis.service.impl.unbound.calculation;

import com.prolog.eis.dto.sxk.OutBoundSxStoreDto;
import com.prolog.eis.service.impl.unbound.entity.DetailDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CalculationTemplate {

    @Autowired
    CalculationService calculationService;

    public List<OutBoundSxStoreDto> calculationOutQty(DetailDataBean detailDataBeand, BigDecimal miniPackage, boolean isPickStation){
        return calculationService.buildOutBoundQty( detailDataBeand,  miniPackage,  isPickStation);
    }

}
