package com.prolog.eis.service.impl.unbound;

import java.math.BigDecimal;

public class DivideAndRemainderToFloat {

    /*
      float数取余运算
     */
    public static BigDecimal[] divideAndRemainderToFloat(float miniPackage, float e) {
        BigDecimal xx = new BigDecimal(Float.toString(miniPackage));
        BigDecimal ee = new BigDecimal(Float.toString(e));
        BigDecimal b[] = ee.divideAndRemainder(xx);
        if(b.length<=2){
            b[0]=b[0].multiply(xx);
            return b;
        }
        return new BigDecimal[]{};

    }

    public static float subtract(float f, float y) {
        BigDecimal b1 = new BigDecimal(Float.toString(f));
        BigDecimal b2 = new BigDecimal(Float.toString(y));
        float z = b1.subtract(b2).floatValue();
        return z;
    }

    public static float add(float f, float y) {
        BigDecimal b1 = new BigDecimal(Float.toString(f));
        BigDecimal b2 = new BigDecimal(Float.toString(y));
        float z = b1.add(b2).floatValue();
        return z;
    }
}
