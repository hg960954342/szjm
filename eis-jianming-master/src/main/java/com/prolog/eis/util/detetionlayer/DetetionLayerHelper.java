package com.prolog.eis.util.detetionlayer;

import java.util.ArrayList;
import java.util.List;

public class DetetionLayerHelper {

	public static List<List<Integer>> getLayers(int detection,int minLayer,int maxLayer) {
		
		//建民项目detection和分层无关
		List<List<Integer>> layerGroups = new ArrayList<>();
		List<Integer> layers = new ArrayList<Integer>();
		for(int i=minLayer;i<maxLayer + 1;i++) {
			layers.add(i);
		}
		layerGroups.add(layers);
		
		return layerGroups;
	}
}
