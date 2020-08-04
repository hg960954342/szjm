package com.prolog.eis.service;

import com.prolog.eis.model.eis.LayerPortOrigin;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.persistence.PostRemove;

public interface LayerPortOriginService {

	LayerPortOrigin getPortOrigin(String junctionPort,int layer,int defaultOriginX,int defaultOriginY);

	@Component
	@Log4j2
	class HistoryListener {



		@PostRemove
		public void PostUpdate(final Object inboundTask) {

		}


	}
}
