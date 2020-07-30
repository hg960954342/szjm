package com.prolog.eis.service.sxk;

import java.util.List;

import com.prolog.eis.dto.sxk.AllInStoreLocationLayersDto;
import com.prolog.eis.dto.sxk.AreaSortDto;

public interface SxInStoreService {

	/**
	 * 
	 * @param containerNo
	 * @param layer
	 * @param taskProperty1
	 * @param taskProperty2
	 * @param originX
	 * @param originY		
	 * @param reservedLocation 货位类型(1、空托盘垛货位组，2、理货预留货位组，3、融合/HUB货位组，4、MIT货位组)
	 * @param area 货位区域默认为null
	 * @param area 货位区域默认为null
	 * @return cengEmptyCount 层需要预留的空货位数量
	 * @throws Exception
	 */
	public Integer getInStoreDetail(String containerNo, Integer layer,String taskProperty1,String taskProperty2,Integer originX ,Integer originY,Integer reservedLocation,Integer area,double weight,int cengEmptyCount )throws Exception;
	
	public List<AllInStoreLocationLayersDto> getAllInStore(int hoisterId,List<Integer> layers,String taskProperty1, String taskProperty2) throws Exception;
	
	/**
	 * 找最优层
	 * @param layers
	 * @return
	 * @throws Exception
	 */
	public Integer findLayer(int hoisterId ,List<Integer> layers,int cengEmptyCount,String taskProperty1,String taskProperty2)throws Exception;
	
	public Integer findLocationGroup(Integer layer, String taskProperty1, String taskProperty2,Integer originX ,Integer originY,  List<AreaSortDto> areaSortDtos,Integer area,double weight)throws Exception;
	
	public Integer findLocationId(Integer locationGroupId , String taskProperty1, String taskProperty2,double weight)throws Exception;
}
