package com.prolog.eis.service.store;

import java.util.List;

import com.prolog.eis.dto.sxk.SxHoisterRimDto;
import com.prolog.eis.vo.SxHoisterConfigVo;
import com.prolog.eis.vo.SxLayerHoisterConfigVo;
import com.prolog.framework.common.message.RestMessage;

public interface HoisterConfigService {
	/**
     * 查询所有提升机配置
     * @return
     */
    List<SxHoisterConfigVo> findAll();

    RestMessage<String> updateHoisterConfig(SxLayerHoisterConfigVo vo);
    
    List<Integer> findLayerByHoisterId(int hoisterId,Integer sourceLayer)throws Exception;

	List<SxHoisterRimDto> findAllRim()throws Exception;
}
