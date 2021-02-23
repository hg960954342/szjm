package com.prolog.eis.service.impl.unbound;

import java.math.BigDecimal;

public class DivideAndRemainderToFloat {

    /*
      float数取余运算
     */
    public static BigDecimal[] divideAndRemainderToFloat(BigDecimal miniPackage, BigDecimal e) {
        BigDecimal b[] = e.divideAndRemainder(miniPackage);
        if(b.length<=2){
            b[0]=b[0].multiply(miniPackage);
            return b;
        }
        return new BigDecimal[]{};

    }

    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
         return b1.subtract(b2) ;

    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
         return b1.add(b2) ;

    }
}
