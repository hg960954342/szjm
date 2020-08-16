
package com.prolog.eis.util;

import com.prolog.eis.dto.base.Coordinate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PrologLocationUtils {




    public static Coordinate analysis(String coordinateStr) {
        Coordinate coordinate = new Coordinate();
        int layer  = 0;
        int x = Integer.valueOf(coordinateStr.substring(0,6));
        int y = Integer.valueOf(coordinateStr.substring(8,14));
        coordinate.setLayer(layer);
        coordinate.setX(x);
        coordinate.setY(y);
        return coordinate;
    }
    public static String splicingXYStr(int layer,int x,int y) {
        StringBuilder sb = new StringBuilder();
       Layer[] layers=Layer.values();
       Map map=new HashMap<Integer,Layer>();
       for(Layer l:layers){
           map.put(l.getLayer(),l);
       }
        sb.append(String.format("%06d",x));
        sb.append(map.get(layer));
        sb.append(String.format("%06d", y));
        return sb.toString();
    }

    public static void main(String[] args) {
        Layer[] layers=Layer.values();
        Map map=new HashMap<Integer,Layer>();
        for(Layer l:layers){
            map.put(l.getLayer(),l);
        }
        System.out.println(map.get(4));
    }
}
enum Layer{
    AB(4),
    XY(3);

    private int layer;
    Layer(int layer) {
        this.layer=layer;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}