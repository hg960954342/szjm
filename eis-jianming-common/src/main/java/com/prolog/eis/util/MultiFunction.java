package com.prolog.eis.util;

@FunctionalInterface
public interface MultiFunction<P1,P2, T> {
    T apply(P1 p1,P2 p2);
}
