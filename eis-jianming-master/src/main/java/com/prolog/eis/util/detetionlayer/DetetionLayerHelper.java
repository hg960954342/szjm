package com.prolog.eis.util.detetionlayer;

import java.util.ArrayList;
import java.util.List;

public class DetetionLayerHelper {

	public static List<List<Integer>> getLayers(int detection,int minLayer,int maxLayer) {
		List<List<Integer>> layerGroups = new ArrayList<>();
		
		if(detection <= 1) {
			List<Integer> layer1s = new ArrayList<Integer>();
			if(minLayer <= 5 && maxLayer>=5) {
				layer1s.add(5);
			}
			if(minLayer <= 6 && maxLayer>=6) {
				layer1s.add(6);
			}
			if(!layer1s.isEmpty()) {
				layerGroups.add(layer1s);	
			}
		}

		if(detection <= 2) {
			List<Integer> layer2s = new ArrayList<Integer>();
			if(minLayer <= 7 && maxLayer>=7) {
				layer2s.add(7);
			}
			if(!layer2s.isEmpty()) {
				layerGroups.add(layer2s);	
			}
		}

		if(detection <= 3) {
			List<Integer> layer3s = new ArrayList<Integer>();
			if(minLayer <= 3 && maxLayer>=3) {
				layer3s.add(3);
			}
			if(!layer3s.isEmpty()) {
				layerGroups.add(layer3s);
			}
		}

		if(detection <= 4) {
			List<Integer> layer4s = new ArrayList<Integer>();
			if(minLayer <= 4 && maxLayer>=4) {
				layer4s.add(4);	
			}
			if(!layer4s.isEmpty()) {
				layerGroups.add(layer4s);
			}
		}
		
		return layerGroups;
	}
}
