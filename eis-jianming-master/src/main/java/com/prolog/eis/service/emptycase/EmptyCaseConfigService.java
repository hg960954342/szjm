package com.prolog.eis.service.emptycase;

import java.util.List;

public interface EmptyCaseConfigService {

	List<List<Integer>> getEmptyCaseLayer(int defaultLayer,int minLayer,int maxLayer);
}
