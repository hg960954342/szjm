package com.prolog.eis.service.sxk.impl;

import com.prolog.eis.dao.sxk.InStoreMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.dto.sxk.*;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.service.sxk.SxInStoreService;
import com.prolog.eis.util.FileLogHelper;
import com.prolog.eis.util.ListHelper;
import com.prolog.framework.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class SxInStoreServiceImpl implements SxInStoreService{

	@Autowired
	private InStoreMapper inStoreMapper;
	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer getInStoreDetail(String containerNo, Integer layer, String taskProperty1, String taskProperty2,
			Integer originX, Integer originY, Integer reservedLocation, Integer area, double weight, int cengEmptyCount)
					throws Exception {

		try {
			List<Integer> types = new ArrayList<>();
			types.add(1);

			List<AreaSortDto> areaSortDtos = new ArrayList<>();
			AreaSortDto areaSortDto = new AreaSortDto();
			areaSortDto.setSortIndex(1);
			areaSortDto.setTypes(types);
			areaSortDtos.add(areaSortDto);
			/*if (areaSortDtos.size() == 0) {
				throw new Exception("货位类型无法获取");
				//return null;
			}*/
			Integer locationId = null;
			long startTime = System.currentTimeMillis();
			// step2 再找货位组(找出离原点最近的货位组)
			Integer storeLocationGroupId = findLocationGroup(layer, taskProperty1, taskProperty2, originX, originY,
					areaSortDtos, area, weight);
			// step3 再找具体货位(找出离原点最近的货位)
			Integer findLocationId = findLocationId(storeLocationGroupId, taskProperty1, taskProperty2, weight);
			if (findLocationId != null) {
				locationId = findLocationId;
			}
			long endTime = System.currentTimeMillis();
			System.out.println("time" + (endTime - startTime) + "ms");
			return locationId;
		}catch (Exception e) {
			// TODO: handle exception

			FileLogHelper.WriteLog("getInStoreDetailError", e.toString());

			return null;
		}
	}

	@Override
	public Integer findLocationGroup(Integer layer, String taskProperty1, String taskProperty2, Integer originX,
			Integer originY, List<AreaSortDto> areaSortDtos, Integer area, double weight) throws Exception {
		Integer storeLocationGroupId = null;
		List<InStoreLocationGroupDto> findStoreLocationGroup = new ArrayList<InStoreLocationGroupDto>();
		// 找所有的可用货位组
		if (area == null) {
			findStoreLocationGroup = inStoreMapper.findStoreLocationGroup(layer, weight);
		} else {
			findStoreLocationGroup = inStoreMapper.findStoreLocationGroupByArea(layer, area, weight);
		}
		List<InStoreLocationGroupDto> findSameTypeLocationGroup = findBestTypeLocationGroup(findStoreLocationGroup,
				areaSortDtos);
		if (findSameTypeLocationGroup.size() == 0) {
			return storeLocationGroupId;
		}
		// step1找相同属性的可用货位组
		List<InStoreLocationGroupDto> inStoreLocationGroupDtos = findSamePropertyLocationGroup(
				findSameTypeLocationGroup, taskProperty1, taskProperty2);
		if (inStoreLocationGroupDtos.size() == 0) {
			// 没有相同属性 1.托盘数小于出口数的货位组 2.托盘数减去出口数小的
			storeLocationGroupId = findLocationGroupId(findSameTypeLocationGroup, originX, originY);
		} else if (inStoreLocationGroupDtos.size() > 1) {
			// 有两个相同属性的货位组则找 预留货位1.托盘数小于出口数的货位组 2.托盘数减去出口数小的
			storeLocationGroupId = findLocationGroupId(inStoreLocationGroupDtos, originX, originY);
		} else {
			// 只发现一个，直接锁定
			storeLocationGroupId = inStoreLocationGroupDtos.get(0).getStoreLocationGroupId();
		}
		return storeLocationGroupId;
	}

	@Override
	// 找出货位组的货位
	public Integer findLocationId(Integer locationGroupId, String taskProperty1, String taskProperty2, double weight)
			throws Exception {
		Integer sxStoreLocation = null;
		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper.findById(locationGroupId,
				SxStoreLocationGroup.class);
		/**
		 * 优先级最高的是满足重量 1.找到相同属性的 1.1 找到一个出入口直接入相邻货位 1.2 找到两个出入口则找出口近的一侧货位
		 * 
		 * 2.没有相同的属性，找出 2.1一个出入口直接入相邻货位 2.2找出两个出入口找出口近的一侧货位 2.2.1如果没有货位可用，则入另一侧
		 */
		List<SxStoreLocation> sxStoreLocations1 = sxStoreLocationMapper.checkWeight(locationGroupId, weight);
		if (sxStoreLocations1.size() == 0) {
			throw new Exception("所选货位组没有找到入库货位,请检查货位组Id为【" + locationGroupId + "】");
		} else if (sxStoreLocations1.size() == 1) {
			SxStoreLocation sxStoreLocation2 = sxStoreLocations1.get(0);
			sxStoreLocation = sxStoreLocation2.getId();
		} else if (sxStoreLocations1.size() == 2) {
			// 找离入口1属性相同的有托盘的货位
			List<SxStoreLocation> sxStoreLocationsProperty1 = sxStoreLocationMapper.findByProperty1(locationGroupId,
					taskProperty1, taskProperty2);
			// 找离入口2属性相同的有托盘的货位
			List<SxStoreLocation> sxStoreLocationsProperty2 = sxStoreLocationMapper.findByProperty2(locationGroupId,
					taskProperty1, taskProperty2);
			if (sxStoreLocationsProperty1.size() > 0 && sxStoreLocationsProperty2.size() > 0) {
				// 两个入口都是相同属性
				// 找出口近的一侧货位
				if (sxStoreLocationsProperty1.size() == 1 && sxStoreLocationsProperty2.size() == 1) {
					SxStoreLocation sxStoreLocationProperty1 = sxStoreLocationsProperty1.get(0);
					SxStoreLocation sxStoreLocationProperty2 = sxStoreLocationsProperty2.get(0);
					// 比较位置
					int locationNum = sxStoreLocationGroup.getLocationNum();
					sxStoreLocation = chooseBestLocation(locationNum, sxStoreLocationProperty1,
							sxStoreLocationProperty2);
				} else {
					throw new Exception("单出口查询出有两个货位在最外侧！货位组ID为【" + locationGroupId + "】");
				}
			} else if (sxStoreLocationsProperty1.size() > 0 && sxStoreLocationsProperty2.size() == 0) {
				// 入口1属性相同
				// 找到一个出入口直接入相邻货位
				// 找出离出口最近的空货位
				if (sxStoreLocationsProperty1.size() == 1) {
					SxStoreLocation sxStoreLocation2 = sxStoreLocationsProperty1.get(0);
					sxStoreLocation = findLocationAscentId(sxStoreLocation2);
				} else {
					throw new Exception("单出口查询出有两个货位在最外侧！货位组ID为【" + locationGroupId + "】");
				}
			} else if (sxStoreLocationsProperty1.size() == 0 && sxStoreLocationsProperty2.size() > 0) {
				// 入口2属性相同
				if (sxStoreLocationsProperty2.size() == 1) {
					SxStoreLocation sxStoreLocation2 = sxStoreLocationsProperty2.get(0);
					sxStoreLocation = findLocationAscentId(sxStoreLocation2);
				} else {
					throw new Exception("单出口查询出有两个货位在最外侧！货位组ID为【" + locationGroupId + "】");
				}
			} else {
				// 没有属性相同的，混批次
				// 查询离入口最近的有容器的货位
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper
						.findStoreLocation(sxStoreLocationGroup.getId());
				if (sxStoreLocations.size() == 0) {
					// 该货位组上没有托盘
					sxStoreLocation = findEmptyLocation(sxStoreLocationGroup);
				} else {
					if (sxStoreLocationGroup.getInOutNum() == 1) {
						if (sxStoreLocations.size() == 1) {
							// 单入口直接找有货的离入口最近的货位
							sxStoreLocation = findLocationAscentId(sxStoreLocations.get(0));
						}
					} else if (sxStoreLocationGroup.getInOutNum() == 2) {
						// 双入口
						// 判断货位相邻货位是否为null
						if (sxStoreLocations.size() == 1) {
							// 找出离入口最近的货位
							sxStoreLocation = findSingleLocation(sxStoreLocationGroup.getLocationNum(),
									sxStoreLocations.get(0));
						} else if (sxStoreLocations.size() == 2) {
							// 判断查出的货位是否为最左或者最右
							List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations,
									p -> p.getLocationIndex() == 1);
							if (sxStoreLocations2.size() > 0) {
								// 最左侧已满则找另一侧
								SxStoreLocation sxLocation = new SxStoreLocation();
								for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
									if (sxStoreLocations2.get(0).getId() != sxStoreLocation2.getId()) {
										sxLocation = sxStoreLocation2;
									}
								}
								sxStoreLocation = findLocationAscentId(sxLocation);
							} else {
								List<SxStoreLocation> sxStoreLocations3 = ListHelper.where(sxStoreLocations,
										p -> p.getLocationIndex() == sxStoreLocationGroup.getLocationNum());
								if (sxStoreLocations3.size() > 0) {
									// 有则找另一侧
									SxStoreLocation sxLocation = new SxStoreLocation();
									for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
										if (sxStoreLocations3.get(0).getId() != sxStoreLocation2.getId()) {
											sxLocation = sxStoreLocation2;
										}
									}
									sxStoreLocation = findLocationAscentId(sxLocation);
								} else {
									// 如果两个货位都不是最侧货位，找最合适的
									sxStoreLocation = chooseBestLocation(sxStoreLocationGroup.getLocationNum(),
											sxStoreLocations.get(0), sxStoreLocations.get(1));
								}
							}
						}
					} else {
						throw new Exception("货位与容器数据存在异常！！！请检查！货位组ID【" + sxStoreLocationGroup.getId() + "】");
					}
				}
			}
		} else {
			throw new Exception("所选货位组查询到两个以上的入库货位,请检查货位组Id为【" + locationGroupId + "】");
		}

		return sxStoreLocation;
	}

	private Integer chooseBestLocation(int locationNum, SxStoreLocation sxStoreLocationProperty1,
			SxStoreLocation sxStoreLocationProperty2) throws Exception {
		Integer storeLocationId = null;
		StoreLocationDistance storeLocationDistance = new StoreLocationDistance();
		StoreLocationDistance storeLocationDistance1 = new StoreLocationDistance();
		StoreLocationDistance storeLocationDistance2 = new StoreLocationDistance();
		StoreLocationDistance storeLocationDistance3 = new StoreLocationDistance();
		StoreLocationDistance storeLocationDistance4 = new StoreLocationDistance();
		storeLocationDistance1.setStoreLocationId(sxStoreLocationProperty1.getId());
		storeLocationDistance1.setDistance(sxStoreLocationProperty1.getLocationIndex() - 1);
		storeLocationDistance1.setFlag(1);
		storeLocationDistance2.setStoreLocationId(sxStoreLocationProperty1.getId());
		storeLocationDistance2.setDistance(locationNum - sxStoreLocationProperty1.getLocationIndex());
		storeLocationDistance2.setFlag(2);
		storeLocationDistance3.setStoreLocationId(sxStoreLocationProperty2.getId());
		storeLocationDistance3.setDistance(sxStoreLocationProperty2.getLocationIndex() - 1);
		storeLocationDistance3.setFlag(1);
		storeLocationDistance4.setStoreLocationId(sxStoreLocationProperty2.getId());
		storeLocationDistance4.setDistance(locationNum - sxStoreLocationProperty2.getLocationIndex());
		storeLocationDistance4.setFlag(2);
		List<StoreLocationDistance> distances = new ArrayList<StoreLocationDistance>();
		distances.add(storeLocationDistance1);
		distances.add(storeLocationDistance2);
		distances.add(storeLocationDistance3);
		distances.add(storeLocationDistance4);
		for (StoreLocationDistance storeLocationDistancesss : distances) {
			if (storeLocationDistance.getDistance() > storeLocationDistancesss.getDistance()) {
				storeLocationDistance = storeLocationDistancesss;
			}
		}
		if (storeLocationDistance.getStoreLocationId() == sxStoreLocationProperty1.getId()) {
			if (storeLocationDistance.getFlag() == 1) {
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils
						.put("locationIndex", sxStoreLocationProperty1.getLocationIndex() - 1)
						.put("storeLocationGroupId", sxStoreLocationProperty1.getStoreLocationGroupId()).getMap(),
						SxStoreLocation.class);
				storeLocationId = sxStoreLocations.get(0).getId();
			} else {
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils
						.put("locationIndex", sxStoreLocationProperty1.getLocationIndex() + 1)
						.put("storeLocationGroupId", sxStoreLocationProperty1.getStoreLocationGroupId()).getMap(),
						SxStoreLocation.class);
				storeLocationId = sxStoreLocations.get(0).getId();
			}
		} else {
			if (storeLocationDistance.getFlag() == 1) {
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils
						.put("locationIndex", sxStoreLocationProperty2.getLocationIndex() - 1)
						.put("storeLocationGroupId", sxStoreLocationProperty1.getStoreLocationGroupId()).getMap(),
						SxStoreLocation.class);
				storeLocationId = sxStoreLocations.get(0).getId();
			} else {
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils
						.put("locationIndex", sxStoreLocationProperty2.getLocationIndex() + 1)
						.put("storeLocationGroupId", sxStoreLocationProperty1.getStoreLocationGroupId()).getMap(),
						SxStoreLocation.class);
				storeLocationId = sxStoreLocations.get(0).getId();
			}
		}
		return storeLocationId;
	}

	private Integer findEmptyLocation(SxStoreLocationGroup sxStoreLocationGroup) throws Exception {
		// 空货位组找出离出口最远的货位
		Integer storeLocationId = null;
		// 单出口时，直接入最里面的
		if (sxStoreLocationGroup.getEntrance() == 1) {
			// 只有出口1，则入index为最大的
			int locationNum = sxStoreLocationGroup.getLocationNum();
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper
					.findByMap(
							MapUtils.put("locationIndex", locationNum)
							.put("storeLocationGroupId", sxStoreLocationGroup.getId()).getMap(),
							SxStoreLocation.class);
			storeLocationId = sxStoreLocations.get(0).getId();
		} else if (sxStoreLocationGroup.getEntrance() == 2) {
			// 只有出口2，则入index为1的
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(
					MapUtils.put("locationIndex", 1).put("storeLocationGroupId", sxStoreLocationGroup.getId()).getMap(),
					SxStoreLocation.class);
			storeLocationId = sxStoreLocations.get(0).getId();
		} else if (sxStoreLocationGroup.getEntrance() == 3) {
			int locationNum = sxStoreLocationGroup.getLocationNum();
			double ceil = Math.ceil(locationNum / 2);
			int index = new Double(ceil).intValue() + 1;
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper
					.findByMap(
							MapUtils.put("locationIndex", index)
							.put("storeLocationGroupId", sxStoreLocationGroup.getId()).getMap(),
							SxStoreLocation.class);
			storeLocationId = sxStoreLocations.get(0).getId();
		}
		return storeLocationId;
	}

	private Integer findSingleLocation(int locationNum, SxStoreLocation sxStoreLocation) throws Exception {
		Integer storeLocationId = null;
		StoreLocationDistance storeLocationDistance = new StoreLocationDistance();
		StoreLocationDistance storeLocationDistance1 = new StoreLocationDistance();
		StoreLocationDistance storeLocationDistance2 = new StoreLocationDistance();
		storeLocationDistance1.setStoreLocationId(sxStoreLocation.getId());
		storeLocationDistance1.setDistance(sxStoreLocation.getLocationIndex() - 1);
		storeLocationDistance1.setFlag(1);
		storeLocationDistance2.setStoreLocationId(sxStoreLocation.getId());
		storeLocationDistance2.setDistance(locationNum - sxStoreLocation.getLocationIndex());
		storeLocationDistance2.setFlag(2);
		List<StoreLocationDistance> distances = new ArrayList<StoreLocationDistance>();
		distances.add(storeLocationDistance1);
		distances.add(storeLocationDistance2);
		for (StoreLocationDistance storeLocationDistancesss : distances) {
			if (storeLocationDistance.getDistance() > storeLocationDistancesss.getDistance()) {
				storeLocationDistance = storeLocationDistancesss;
			}
		}
		if (storeLocationDistance.getFlag() == 1) {
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(
					MapUtils.put("locationIndex", sxStoreLocation.getLocationIndex() - 1)
					.put("storeLocationGroupId", sxStoreLocation.getStoreLocationGroupId()).getMap(),
					SxStoreLocation.class);
			storeLocationId = sxStoreLocations.get(0).getId();
		} else {
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(
					MapUtils.put("locationIndex", sxStoreLocation.getLocationIndex() + 1)
					.put("storeLocationGroupId", sxStoreLocation.getStoreLocationGroupId()).getMap(),
					SxStoreLocation.class);
			storeLocationId = sxStoreLocations.get(0).getId();
		}

		return storeLocationId;
	}

	private Integer findLocationAscentId(SxStoreLocation sxLocation) throws Exception {
		Integer sxStoreLocation = null;
		Integer storeLocationId1 = sxLocation.getStoreLocationId1();
		Integer storeLocationId2 = sxLocation.getStoreLocationId2();
		if (storeLocationId1 == null) {
			sxStoreLocation = storeLocationId2;
		} else if (storeLocationId2 == null) {
			sxStoreLocation = storeLocationId1;
		} else {
			List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("storeLocationId", storeLocationId1).getMap(),
					SxStore.class);
			// 查询一侧的相邻货位是否有实物，无则入，有则从另一个货位入
			if (sxStores.size() > 0) {
				sxStoreLocation = sxLocation.getStoreLocationId2();
			} else {
				sxStoreLocation = storeLocationId1;
			}
		}
		return sxStoreLocation;
	}

	@Override
	public Integer findLayer(int hoisterId ,List<Integer> layers, int cengEmptyCount, String taskProperty1, String taskProperty2)
			throws Exception {
		Integer layer = null;
		if (layers.size() == 0) {
			FileLogHelper.WriteLog("sxInStoreError", "无可用层");
			throw new Exception("无可用层");
		}
		// step1 .先找层+区域
		List<AllInStoreLocationLayersDto> allInStoreLocationLayersDtos = this.getAllInStore(hoisterId, layers,
				taskProperty1, taskProperty2);
		for (int i = 0; i < allInStoreLocationLayersDtos.size(); i++) {
			AllInStoreLocationLayersDto allInStoreLocationLayersDto = allInStoreLocationLayersDtos.get(i);
			// 判断空货位是否满足配置项
			int layer2 = allInStoreLocationLayersDto.getLayer();
			Integer emptyNum = inStoreMapper.findEmptyLocation(layer2);
			if (emptyNum > cengEmptyCount) {
				layer = allInStoreLocationLayersDto.getLayer();
				break;
			}
		}
		if (layer == null) {
			throw new Exception("入库算法无法查找层");
		}
		return layer;
	}

	@Override
	public List<AllInStoreLocationLayersDto> getAllInStore(int hoisterId, List<Integer> layers, String taskProperty1,
			String taskProperty2) throws Exception {

		String layersStr = StringUtils.join(layers, ",");
		// 找出可用层
		List<AllInStoreLocationLayersDto> allInStoreLocationDtos = inStoreMapper.findStoreLocation(layersStr);
		// 找出属性相同的容器数
		List<AllInStorePropertyCountDto> allInStorePropertyCountDtos = inStoreMapper.findAllPropertyLayer(layersStr,
				taskProperty1, taskProperty2);
		// 找出层的入库任务总数
		List<AllInStoreInTaskCountDto> allInStoreInTaskCountDtos = inStoreMapper.findInTaskLayer(layersStr);
		// 找出层的出库任务总数
		List<AllInStoreOutTaskCountDto> allInStoreOutTaskCountDtos = inStoreMapper.findOutTaskLayer(layersStr);
		// 找出提升机的层任务总数
		/*List<AllInStoreHoisterTaskCountDto> allInStoreHoisterTaskCountDtos = inStoreMapper
				.findHoisterTaskLayer(hoisterId, layersStr);*/
		// 拼接
		for (AllInStoreLocationLayersDto allInStoreLocationDto : allInStoreLocationDtos) {
			// 放入属性相同的容器数
			for (AllInStorePropertyCountDto allInStorePropertyCountDto : allInStorePropertyCountDtos) {
				if (allInStorePropertyCountDto.getLayer() == allInStoreLocationDto.getLayer()) {
					allInStoreLocationDto.setPropertyCount(allInStorePropertyCountDto.getPropertyCount());
				}
			}
			// 入库任务数
			for (AllInStoreInTaskCountDto allInStoreInTaskCountDto : allInStoreInTaskCountDtos) {
				if (allInStoreInTaskCountDto.getLayer() == allInStoreLocationDto.getLayer()) {
					allInStoreLocationDto.setRkTaskCount(allInStoreInTaskCountDto.getInTaskCount());
				}
			}
			// 出库任务数
			for (AllInStoreOutTaskCountDto allInStoreOutTaskCountDto : allInStoreOutTaskCountDtos) {
				if (allInStoreOutTaskCountDto.getLayer() == allInStoreLocationDto.getLayer()) {
					allInStoreLocationDto.setCkTaskCount(allInStoreOutTaskCountDto.getOutTaskCount());
				}
			}
			// 提升机的层任务总数
			/*for (AllInStoreHoisterTaskCountDto allInStoreHoisterTaskCountDto : allInStoreHoisterTaskCountDtos) {
				if (allInStoreHoisterTaskCountDto.getLayer() == allInStoreLocationDto.getLayer()) {
					allInStoreLocationDto.setHoisterTaskCount(allInStoreHoisterTaskCountDto.getTaskCount());
				}
			}*/
		}

		// step5 排序
		getSortList(allInStoreLocationDtos);
		return allInStoreLocationDtos;
	}

	// 查询最好的类型货位组
	private List<InStoreLocationGroupDto> findBestTypeLocationGroup(
			List<InStoreLocationGroupDto> inStoreLocationGroupDtos, List<AreaSortDto> areaSortDtos) throws Exception {
		List<InStoreLocationGroupDto> inStoreLocationGroupDtosUsed = new ArrayList<InStoreLocationGroupDto>();
		getSortTypeList(areaSortDtos);
		for (int i = 0; i < areaSortDtos.size(); i++) {
			AreaSortDto areaSortDto = areaSortDtos.get(i);
			List<Integer> types = areaSortDto.getTypes();
			for (Integer integer : types) {
				List<InStoreLocationGroupDto> where = ListHelper.where(inStoreLocationGroupDtos,
						p -> p.getReservedLocation() == integer);
				inStoreLocationGroupDtosUsed.addAll(where);
			}
			if (inStoreLocationGroupDtosUsed.size() > 0) {
				break;
			}
		}
		return inStoreLocationGroupDtosUsed;
	}

	// 排序
	private void getSortTypeList(List<AreaSortDto> areaSortDtos) {
		Collections.sort(areaSortDtos, new Comparator<AreaSortDto>() {

			@Override
			public int compare(AreaSortDto o1, AreaSortDto o2) {
				return o1.getSortIndex() - o2.getSortIndex();
			}
		});
	}

	// 排序
	private void getSortList(List<AllInStoreLocationLayersDto> allInStoreLocationDtos) {
		Collections.sort(allInStoreLocationDtos, new Comparator<AllInStoreLocationLayersDto>() {

			@Override
			public int compare(AllInStoreLocationLayersDto o1, AllInStoreLocationLayersDto o2) {
				// 按照层入库任务数少的在前面排序
				int a = o1.getRkTaskCount() - o2.getRkTaskCount();
				if (a == 0) {
					// 层入库任务数相同则按照层出库任务数排序
					int b = o1.getCkTaskCount() - o2.getCkTaskCount();
					if (b == 0) {
						// 层出库任务数相同则属性相同容器数排序
						int c = o1.getPropertyCount() - o2.getPropertyCount();
						if (c == 0) {
							// TODO 2020/6/8修改优先级 添加提升机层的任务数排序
							int d = o1.getHoisterTaskCount() - o2.getHoisterTaskCount();
							if (d == 0) {
								// 属性相同容器数相同则按照容器数排序
								return o1.getContainerCount() - o2.getContainerCount();
							}
						}
						return c;
					}
					return b;
				}
				return a;
			}
		});
	}

	// 查询相同属性的货位组
	private List<InStoreLocationGroupDto> findSamePropertyLocationGroup(
			List<InStoreLocationGroupDto> findSameTypeLocationGroup, String taskProperty1, String taskProperty2)
					throws Exception {
		List<InStoreLocationGroupDto> inStoreLocationGroupDtosUsed = new ArrayList<InStoreLocationGroupDto>();
		for (InStoreLocationGroupDto inStoreLocationGroupDto : findSameTypeLocationGroup) {

			inStoreLocationGroupDto.setEntrance1Property1(inStoreLocationGroupDto.getEntrance1Property1() == null ? ""
					: inStoreLocationGroupDto.getEntrance1Property1());
			inStoreLocationGroupDto.setEntrance1Property2(inStoreLocationGroupDto.getEntrance1Property2() == null ? ""
					: inStoreLocationGroupDto.getEntrance1Property2());
			inStoreLocationGroupDto.setEntrance2Property1(inStoreLocationGroupDto.getEntrance2Property1() == null ? ""
					: inStoreLocationGroupDto.getEntrance2Property1());
			inStoreLocationGroupDto.setEntrance2Property2(inStoreLocationGroupDto.getEntrance2Property2() == null ? ""
					: inStoreLocationGroupDto.getEntrance2Property2());
			taskProperty1 = taskProperty1 == null ? "" : taskProperty1;
			taskProperty2 = taskProperty2 == null ? "" : taskProperty2;
			if ((inStoreLocationGroupDto.getEntrance1Property1().equals(taskProperty1)
					&& inStoreLocationGroupDto.getEntrance1Property2().equals(taskProperty2))
					|| (inStoreLocationGroupDto.getEntrance2Property1().equals(taskProperty1)
							&& inStoreLocationGroupDto.getEntrance2Property2().equals(taskProperty2))) {
				inStoreLocationGroupDtosUsed.add(inStoreLocationGroupDto);
			}
		}
		return inStoreLocationGroupDtosUsed;
	}

	// 查询相同属性的货位组
	private List<InStoreLocationGroupDto> findNotSamePropertyLocationGroup(
			List<InStoreLocationGroupDto> findSameTypeLocationGroup, String taskProperty1, String taskProperty2)
					throws Exception {
		List<InStoreLocationGroupDto> inStoreLocationGroupDtosUsed = new ArrayList<InStoreLocationGroupDto>();
		for (InStoreLocationGroupDto inStoreLocationGroupDto : findSameTypeLocationGroup) {

			inStoreLocationGroupDto.setEntrance1Property1(inStoreLocationGroupDto.getEntrance1Property1() == null ? ""
					: inStoreLocationGroupDto.getEntrance1Property1());
			inStoreLocationGroupDto.setEntrance1Property2(inStoreLocationGroupDto.getEntrance1Property2() == null ? ""
					: inStoreLocationGroupDto.getEntrance1Property2());
			inStoreLocationGroupDto.setEntrance2Property1(inStoreLocationGroupDto.getEntrance2Property1() == null ? ""
					: inStoreLocationGroupDto.getEntrance2Property1());
			inStoreLocationGroupDto.setEntrance2Property2(inStoreLocationGroupDto.getEntrance2Property2() == null ? ""
					: inStoreLocationGroupDto.getEntrance2Property2());
			taskProperty1 = taskProperty1 == null ? "" : taskProperty1;
			taskProperty2 = taskProperty2 == null ? "" : taskProperty2;
			if ((!inStoreLocationGroupDto.getEntrance1Property1().equals(taskProperty1)
					|| !inStoreLocationGroupDto.getEntrance1Property2().equals(taskProperty2))
					&& (!inStoreLocationGroupDto.getEntrance2Property1().equals(taskProperty1)
							|| !inStoreLocationGroupDto.getEntrance2Property2().equals(taskProperty2))) {
				inStoreLocationGroupDtosUsed.add(inStoreLocationGroupDto);
			}
		}
		return inStoreLocationGroupDtosUsed;
	}

	// 根据 0.预留货位组1.托盘数小于出口数的货位组 2.托盘数减去出口数小的 3.离目标提升机最近的
	private Integer findLocationGroupId(List<InStoreLocationGroupDto> inStoreLocationGroupDtos, Integer originX,
			Integer originY) throws Exception {
		Integer storeLocationGroupId = null;
		// 算出预留货位

		//			List<InStoreLocationGroupDto> newInStoreLocationGroupDtos = new ArrayList<InStoreLocationGroupDto>();
		for (InStoreLocationGroupDto inStoreLocationGroupDto : inStoreLocationGroupDtos) {
			Integer targetX = inStoreLocationGroupDto.getX();
			Integer targetY = inStoreLocationGroupDto.getY();
			Integer distance = Math.abs(targetX - originX) + Math.abs(targetY - originY);
			int subtract = inStoreLocationGroupDto.getContainerCount() - inStoreLocationGroupDto.getInOutNum();
			inStoreLocationGroupDto.setDistance(distance);
			inStoreLocationGroupDto.setSubtract(subtract);
		}
		storeLocationGroupId = bubblingSort(inStoreLocationGroupDtos);

		return storeLocationGroupId;
	}

	private Integer bubblingSort(List<InStoreLocationGroupDto> inStoreLocationGroupDtos) throws Exception {
		Integer storeLocationGroupId = null;
		// 冒泡排序筛选出离提升机最近的
		if (inStoreLocationGroupDtos.size() == 0) {
			FileLogHelper.WriteLog("sxInStoreError", "货位组排序的集合不能为空");
			throw new Exception("货位组排序的集合不能为空");
		}
		InStoreLocationGroupDto inStoreLocationGroupDto2 = inStoreLocationGroupDtos.get(0);
		// reservedLocation 1.空托盘预留货位、2.理货预留货位、3.不用预留货位
		if (inStoreLocationGroupDtos.size() > 1) {
			for (int i = 1; i < inStoreLocationGroupDtos.size(); i++) {
				InStoreLocationGroupDto inStoreLocationGroupDto = inStoreLocationGroupDtos.get(i);
				inStoreLocationGroupDto2 = chooseBestLocationGroup(inStoreLocationGroupDto2, inStoreLocationGroupDto);
			}
		}
		storeLocationGroupId = inStoreLocationGroupDto2.getStoreLocationGroupId();
		return storeLocationGroupId;
	}

	private InStoreLocationGroupDto chooseBestLocationGroup(InStoreLocationGroupDto inStoreLocationGroupDto2,
			InStoreLocationGroupDto inStoreLocationGroupDto) {
		if (inStoreLocationGroupDto2.getSubtract() > inStoreLocationGroupDto.getSubtract()) {
			inStoreLocationGroupDto2 = inStoreLocationGroupDto;
		} else if (inStoreLocationGroupDto2.getSubtract() == inStoreLocationGroupDto.getSubtract()) {
			if (inStoreLocationGroupDto2.getDistance() > inStoreLocationGroupDto.getDistance()) {
				inStoreLocationGroupDto2 = inStoreLocationGroupDto;
			}
		}
		return inStoreLocationGroupDto2;
	}
}
