package com.prolog.eis.service.sxk.impl;

import com.prolog.eis.dao.sxk.SxStoreLocationGroupMapper;
import com.prolog.eis.dao.sxk.SxStoreLocationMapper;
import com.prolog.eis.dao.sxk.SxStoreMapper;
import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.sxk.SxStoreLocation;
import com.prolog.eis.model.sxk.SxStoreLocationGroup;
import com.prolog.eis.service.sxk.SxStoreTaskFinishService;
import com.prolog.eis.util.ListHelper;
import com.prolog.framework.utils.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class SxStoreTaskFinishServiceImpl implements SxStoreTaskFinishService {

	@Autowired
	private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
	@Autowired
	private SxStoreLocationMapper sxStoreLocationMapper;
	@Autowired
	private SxStoreMapper sxStoreMapper;
	//@Autowired
	//private SxVerticalLocationGroupMapper sxVerticalLocationGroupMapper;
	//@Autowired
	//private SxStoreRelationMapper sxStoreRelationMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void inBoundTaskFinish(String containerNo) throws Exception {
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
				SxStore.class);
		if (sxStores.size() == 1) {
			SxStore sxStore = sxStores.get(0);
			Integer storeLocationId = sxStore.getStoreLocationId();
			SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
			// 修改库存状态为已上架
			sxStoreMapper.updateContainerGround(containerNo);
			SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
					.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
			int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
			sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
					MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);
		} else if (sxStores.size() > 1) {
			throw new Exception("查询出多个托盘");
		} else if (sxStores.size() == 0) {
			throw new Exception("库存中没有查到托盘");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void outBoundTaskFinish(String containerNo) throws Exception {
		List<SxStore> sxStores = sxStoreMapper.findByMap(MapUtils.put("containerNo", containerNo).getMap(),
				SxStore.class);
		if (sxStores.size() == 1) {
			SxStore sxStore = sxStores.get(0);
			Integer storeLocationId = sxStore.getStoreLocationId();
			SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
			// 根据出库任务类型转换
			sxStoreMapper.deleteByContainer(containerNo);
			sxStoreLocationMapper.updateMapById(storeLocationId, MapUtils.put("actualWeight", 0).getMap(),
					SxStoreLocation.class);
			computeLocation(sxStore);
			SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
					.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
			int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
			sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
					MapUtils.put("ascentLockState", 0).getMap(), SxStoreLocationGroup.class);
		} else if (sxStores.size() > 0) {
			throw new Exception("查询出多个托盘");
		} else if (sxStores.size() == 0) {
			throw new Exception("库存中没有查到托盘");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void moveTaskFinish(Integer locationiId) throws Exception {
		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(locationiId, SxStoreLocation.class);
		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
				.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
		// 解除移位锁
		sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroup.getId(), MapUtils.put("readyOutLock", 0).getMap(),
				SxStoreLocationGroup.class);
	}

	@Override
	@Transactional
	public void computeLocation(SxStore sxStore) throws Exception {
		Integer storeLocationId = sxStore.getStoreLocationId();
		SxStoreLocation sxStoreLocation = sxStoreLocationMapper.findById(storeLocationId, SxStoreLocation.class);
		SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
				.findById(sxStoreLocation.getStoreLocationGroupId(), SxStoreLocationGroup.class);
		int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
		if (sxStoreLocationGroup.getEntrance() == 1) {
			// 入库朝上的 找最小的有货的索引值，其他的货位减索引值
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findMinHaveStore(sxStoreLocationGroupId);
			Integer haveStoreIndex = 0;
			if (sxStoreLocations.size() > 0) {
				SxStoreLocation sxStoreLocation3 = sxStoreLocations.get(0);
				for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
					int index = sxStoreLocation2.getLocationIndex();
					int count = Math.abs(index - sxStoreLocation3.getLocationIndex());
					sxStoreLocation2.setDeptNum(count);
					if (count == 0) {
						haveStoreIndex = index - 1;
					}
					sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
							MapUtils.put("deptNum", count).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations, p -> p.getDeptNum() == 0);
				// 找移位数位0的，应该为1个
				List<SxStore> sxStores2 = sxStoreMapper.findByMap(
						MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
				SxStore sxStore2 = sxStores2.get(0);
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance1Property1", sxStore2.getTaskProperty1())
								.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
						SxStoreLocationGroup.class);
			} else {
				// 找索引最大的货位
				haveStoreIndex = sxStoreLocationGroup.getLocationNum();
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
						SxStoreLocationGroup.class);
			}
			List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
					.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", haveStoreIndex).getMap(),
					SxStoreLocation.class);
			if (sxStoreLocations2.size() == 1) {
				sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
						MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
			}
			List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
					haveStoreIndex);
			if (sxStoreLocations3.size() > 0) {
				List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
				String idstr = StringUtils.join(ids, ",");
				sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
			}
		} else if (sxStoreLocationGroup.getEntrance() == 2) {
			Integer haveStoreIndex = 0;
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findMaxHaveStore(sxStoreLocationGroupId);
			if (sxStoreLocations.size() > 0) {
				SxStoreLocation sxStoreLocation3 = sxStoreLocations.get(0);
				for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
					int index = sxStoreLocation2.getLocationIndex();
					int count = Math.abs(index - sxStoreLocation3.getLocationIndex());
					sxStoreLocation2.setDeptNum(count);
					if (count == 0) {
						haveStoreIndex = index + 1;
					}
					sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
							MapUtils.put("deptNum", count).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations, p -> p.getDeptNum() == 0);
				// 找移位数位0的，应该为1个
				List<SxStore> sxStores2 = sxStoreMapper.findByMap(
						MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
				SxStore sxStore2 = sxStores2.get(0);
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
								.put("entrance2Property2", sxStore2.getTaskProperty2()).getMap(),
						SxStoreLocationGroup.class);
			} else {
				haveStoreIndex = 1;
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(
						MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", 1).getMap(),
						SxStoreLocation.class);
				sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
						MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance2Property1", null).put("entrance2Property2", null).getMap(),
						SxStoreLocationGroup.class);
			}
			List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
					.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", haveStoreIndex).getMap(),
					SxStoreLocation.class);
			if (sxStoreLocations2.size() == 1) {
				sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
						MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
			}
			List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
					haveStoreIndex);
			if (sxStoreLocations3.size() > 0) {
				List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
				String idstr = StringUtils.join(ids, ",");
				sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
			}

		} else if (sxStoreLocationGroup.getEntrance() == 3) {
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findHaveStore(sxStoreLocationGroupId);
			if (sxStoreLocations.size() > 0) {
				Integer bigHaveStoreIndex = 0;
				Integer smallHaveStoreIndex = 0;
				for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
					int index = sxStoreLocation2.getLocationIndex();
					Integer bigCount = sxStoreLocationMapper.findBigIndexCount(sxStoreLocationGroupId, index);
					Integer smallCount = sxStoreLocationMapper.findSmallIndexCount(sxStoreLocationGroupId, index);
					if (bigCount > smallCount) {
						sxStoreLocation2.setDeptNum(smallCount);
						if (smallCount == 0) {
							smallHaveStoreIndex = index - 1;
						}
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", smallCount).getMap(), SxStoreLocation.class);
					} else if (bigCount == smallCount && bigCount == 0) {
						smallHaveStoreIndex = index - 1;
						bigHaveStoreIndex = index + 1;
						sxStoreLocation2.setDeptNum(0);
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", 0).getMap(), SxStoreLocation.class);
					} else {
						if (bigCount == 0) {
							bigHaveStoreIndex = index + 1;// 8
						}
						sxStoreLocation2.setDeptNum(bigCount);
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", bigCount).getMap(), SxStoreLocation.class);
					}
				}
				// 找出移库数为0的两个或一个，将入库属性赋值
				List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations, p -> p.getDeptNum() == 0);
				if (sxStoreLocations2.size() == 1) {
					List<SxStore> sxStores2 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
					SxStore sxStore2 = sxStores2.get(0);
					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
							MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
									.put("entrance2Property2", sxStore2.getTaskProperty2())
									.put("entrance1Property1", sxStore2.getTaskProperty1())
									.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
							SxStoreLocationGroup.class);
				} else if (sxStoreLocations2.size() == 2) {
					SxStoreLocation sxStoreLocation2 = sxStoreLocations2.get(0);
					SxStoreLocation sxStoreLocation3 = sxStoreLocations2.get(1);
					List<SxStore> sxStores2 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocation2.getId()).getMap(), SxStore.class);
					SxStore sxStore2 = sxStores2.get(0);
					List<SxStore> sxStores3 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocation3.getId()).getMap(), SxStore.class);
					SxStore sxStore3 = sxStores3.get(0);
					if (sxStoreLocation2.getLocationIndex() < sxStoreLocation3.getLocationIndex()) {
						sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
								MapUtils.put("entrance2Property1", sxStore3.getTaskProperty1())
										.put("entrance2Property2", sxStore3.getTaskProperty2())
										.put("entrance1Property1", sxStore2.getTaskProperty1())
										.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
								SxStoreLocationGroup.class);
					} else {
						sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
								MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
										.put("entrance2Property2", sxStore2.getTaskProperty2())
										.put("entrance1Property1", sxStore3.getTaskProperty1())
										.put("entrance1Property2", sxStore3.getTaskProperty2()).getMap(),
								SxStoreLocationGroup.class);
					}
				} else {
					throw new Exception("移库数的货位有误，货位组ID为【" + sxStoreLocationGroupId + "】");
				}
				// 重新计算入库货位
				List<SxStoreLocation> sxStoreLocationsBig = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", bigHaveStoreIndex).getMap(), SxStoreLocation.class);
				List<SxStoreLocation> sxStoreLocationsSmall = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", smallHaveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocationsBig.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocationsBig.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				if (sxStoreLocationsSmall.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocationsSmall.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndexTwo(sxStoreLocationGroupId,
						bigHaveStoreIndex, smallHaveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			} else {
				// 奇数货位则最中间为入库货位
				int locationNum = sxStoreLocationGroup.getLocationNum();
				if (locationNum % 2 == 0) {
					int index1 = locationNum / 2;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index1).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					int index2 = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index2).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations3.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocations33 = sxStoreLocationMapper
							.findNotIndexTwo(sxStoreLocationGroupId, index1, index2);
					if (sxStoreLocations33.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations33, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				} else {
					int index = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
							index);
					if (sxStoreLocations3.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				}

				sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
						MapUtils.put("entrance2Property1", null).put("entrance2Property2", null)
								.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
						SxStoreLocationGroup.class);

			}
		}
		// 计算入库货位
		/*Integer verticalLocationGroupId = sxStoreLocation.getVerticalLocationGroupId();
		if (verticalLocationGroupId != null) {
			// 计算垂直货位重量
			List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(
					MapUtils.put("verticalLocationGroupId", verticalLocationGroupId).getMap(), SxStoreLocation.class);
			SxVerticalLocationGroup sxVerticalLocationGroup = sxVerticalLocationGroupMapper
					.findById(verticalLocationGroupId, SxVerticalLocationGroup.class);
			// 实际重量为0的赋值 限重-实际重量不为0的总和
			double totalActualWeight = 0.0;
			List<Integer> ids = new ArrayList<Integer>();
			for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
				if (sxStoreLocation2.getActualWeight() != 0.0) {
					totalActualWeight = sxStoreLocation2.getActualWeight();
					sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
							MapUtils.put("limitWeight", sxStoreLocation2.getActualWeight()).getMap(),
							SxStoreLocation.class);
				} else {
					ids.add(sxStoreLocation2.getId());
				}
			}
			double limitWeight = sxVerticalLocationGroup.getLimitWeight();
			double locationlimitWeight = limitWeight - totalActualWeight;
			String idstr = StringUtils.join(ids, ",");
			sxVerticalLocationGroupMapper.updateLimitWeight(idstr, locationlimitWeight);
		}*/
	}

	@Override
	public void computeIsInBoundLocationTest() throws Exception {
		List<SxStoreLocationGroup> sxStoreLocationGroups = sxStoreLocationGroupMapper
				.findByMap(new HashMap<String, Object>(), SxStoreLocationGroup.class);
		for (SxStoreLocationGroup sxStoreLocationGroup1 : sxStoreLocationGroups) {
			SxStoreLocationGroup sxStoreLocationGroup = sxStoreLocationGroupMapper
					.findById(sxStoreLocationGroup1.getId(), SxStoreLocationGroup.class);
			int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
			if (sxStoreLocationGroup.getEntrance() == 1) {
				// 入库朝上的 找最小的有货的索引值，其他的货位减索引值
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findMinHaveStore(sxStoreLocationGroupId);
				Integer haveStoreIndex = 0;
				if (sxStoreLocations.size() > 0) {
					SxStoreLocation sxStoreLocation3 = sxStoreLocations.get(0);
					for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
						int index = sxStoreLocation2.getLocationIndex();
						int count = Math.abs(index - sxStoreLocation3.getLocationIndex());
						sxStoreLocation2.setDeptNum(count);
						if (count == 0) {
							haveStoreIndex = index - 1;
						}
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", count).getMap(), SxStoreLocation.class);
					}
					List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations,
							p -> p.getDeptNum() == 0);
					// 找移位数位0的，应该为1个
					List<SxStore> sxStores2 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
					SxStore sxStore2 = sxStores2.get(0);
					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
							MapUtils.put("entrance1Property1", sxStore2.getTaskProperty1())
									.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
							SxStoreLocationGroup.class);
				} else {
					// 找索引最大的货位
					haveStoreIndex = sxStoreLocationGroup.getLocationNum();
					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
							MapUtils.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
							SxStoreLocationGroup.class);
				}
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", haveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocations2.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
						haveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			} else if (sxStoreLocationGroup.getEntrance() == 2) {
				Integer haveStoreIndex = 0;
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findMaxHaveStore(sxStoreLocationGroupId);
				if (sxStoreLocations.size() > 0) {
					SxStoreLocation sxStoreLocation3 = sxStoreLocations.get(0);
					for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
						int index = sxStoreLocation2.getLocationIndex();
						int count = Math.abs(index - sxStoreLocation3.getLocationIndex());
						sxStoreLocation2.setDeptNum(count);
						if (count == 0) {
							haveStoreIndex = index + 1;
						}
						sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
								MapUtils.put("deptNum", count).getMap(), SxStoreLocation.class);
					}
					List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations,
							p -> p.getDeptNum() == 0);
					// 找移位数位0的，应该为1个
					List<SxStore> sxStores2 = sxStoreMapper.findByMap(
							MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(), SxStore.class);
					SxStore sxStore2 = sxStores2.get(0);
					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
							MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
									.put("entrance2Property2", sxStore2.getTaskProperty2()).getMap(),
							SxStoreLocationGroup.class);
				} else {
					haveStoreIndex = 1;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", 1).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
							MapUtils.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
							SxStoreLocationGroup.class);
				}
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", haveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocations2.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
						haveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}

			} else if (sxStoreLocationGroup.getEntrance() == 3) {
				List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findHaveStore(sxStoreLocationGroupId);
				if (sxStoreLocations.size() > 0) {
					Integer bigHaveStoreIndex = 0;
					Integer smallHaveStoreIndex = 0;
					for (SxStoreLocation sxStoreLocation2 : sxStoreLocations) {
						int index = sxStoreLocation2.getLocationIndex();
						Integer bigCount = sxStoreLocationMapper.findBigIndexCount(sxStoreLocationGroupId, index);
						Integer smallCount = sxStoreLocationMapper.findSmallIndexCount(sxStoreLocationGroupId, index);
						if (bigCount > smallCount) {
							sxStoreLocation2.setDeptNum(smallCount);
							if (smallCount == 0) {
								smallHaveStoreIndex = index - 1;
							}
							sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
									MapUtils.put("deptNum", smallCount).getMap(), SxStoreLocation.class);
						} else if (bigCount == smallCount && bigCount == 0) {
							smallHaveStoreIndex = index - 1;
							bigHaveStoreIndex = index + 1;
							sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
									MapUtils.put("deptNum", 0).getMap(), SxStoreLocation.class);
						} else {
							if (bigCount == 0) {
								bigHaveStoreIndex = index + 1;
							}
							sxStoreLocationMapper.updateMapById(sxStoreLocation2.getId(),
									MapUtils.put("deptNum", bigCount).getMap(), SxStoreLocation.class);
						}
					}
					// 找出移库数为0的两个或一个，将入库属性赋值
					List<SxStoreLocation> sxStoreLocations2 = ListHelper.where(sxStoreLocations,
							p -> p.getDeptNum() == 0);
					if (sxStoreLocations2.size() == 1) {
						List<SxStore> sxStores2 = sxStoreMapper.findByMap(
								MapUtils.put("storeLocationId", sxStoreLocations2.get(0).getId()).getMap(),
								SxStore.class);
						SxStore sxStore2 = sxStores2.get(0);
						sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
								MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
										.put("entrance2Property2", sxStore2.getTaskProperty2())
										.put("entrance1Property1", sxStore2.getTaskProperty1())
										.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
								SxStoreLocationGroup.class);
					} else if (sxStoreLocations2.size() == 2) {
						SxStoreLocation sxStoreLocation2 = sxStoreLocations2.get(0);
						SxStoreLocation sxStoreLocation3 = sxStoreLocations2.get(1);
						List<SxStore> sxStores2 = sxStoreMapper.findByMap(
								MapUtils.put("storeLocationId", sxStoreLocation2.getId()).getMap(), SxStore.class);
						SxStore sxStore2 = sxStores2.get(0);
						List<SxStore> sxStores3 = sxStoreMapper.findByMap(
								MapUtils.put("storeLocationId", sxStoreLocation3.getId()).getMap(), SxStore.class);
						SxStore sxStore3 = sxStores3.get(0);
						if (sxStoreLocation2.getLocationIndex() < sxStoreLocation3.getLocationIndex()) {
							sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
									MapUtils.put("entrance2Property1", sxStore3.getTaskProperty1())
											.put("entrance2Property2", sxStore3.getTaskProperty2())
											.put("entrance1Property1", sxStore2.getTaskProperty1())
											.put("entrance1Property2", sxStore2.getTaskProperty2()).getMap(),
									SxStoreLocationGroup.class);
						} else {
							sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
									MapUtils.put("entrance2Property1", sxStore2.getTaskProperty1())
											.put("entrance2Property2", sxStore2.getTaskProperty2())
											.put("entrance1Property1", sxStore3.getTaskProperty1())
											.put("entrance1Property2", sxStore3.getTaskProperty2()).getMap(),
									SxStoreLocationGroup.class);
						}
					} else {
						throw new Exception("移库数的货位有误，货位组ID为【" + sxStoreLocationGroupId + "】");
					}
					// 重新计算入库货位
					List<SxStoreLocation> sxStoreLocationsBig = sxStoreLocationMapper
							.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
									.put("locationIndex", bigHaveStoreIndex).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocationsSmall = sxStoreLocationMapper
							.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
									.put("locationIndex", smallHaveStoreIndex).getMap(), SxStoreLocation.class);
					if (sxStoreLocationsBig.size() == 1) {
						sxStoreLocationMapper.updateMapById(sxStoreLocationsBig.get(0).getId(),
								MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					}
					if (sxStoreLocationsSmall.size() == 1) {
						sxStoreLocationMapper.updateMapById(sxStoreLocationsSmall.get(0).getId(),
								MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					}
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper
							.findNotIndexTwo(sxStoreLocationGroupId, bigHaveStoreIndex, smallHaveStoreIndex);
					if (sxStoreLocations3.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				} else {
					// 奇数货位则最中间为入库货位
					int locationNum = sxStoreLocationGroup.getLocationNum();
					if (locationNum % 2 == 0) {
						int index1 = locationNum / 2;
						List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
								.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
										.put("locationIndex", index1).getMap(), SxStoreLocation.class);
						sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
								MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
						int index2 = locationNum / 2 + 1;
						List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper
								.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
										.put("locationIndex", index2).getMap(), SxStoreLocation.class);
						sxStoreLocationMapper.updateMapById(sxStoreLocations3.get(0).getId(),
								MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
						List<SxStoreLocation> sxStoreLocations33 = sxStoreLocationMapper
								.findNotIndexTwo(sxStoreLocationGroupId, index1, index2);
						if (sxStoreLocations33.size() > 0) {
							List<Integer> ids = ListHelper.select(sxStoreLocations33, p -> p.getId());
							String idstr = StringUtils.join(ids, ",");
							sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
						}
					} else {
						int index = locationNum / 2 + 1;
						List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
								.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
										.put("locationIndex", index).getMap(), SxStoreLocation.class);
						sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
								MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
						List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper
								.findNotIndex(sxStoreLocationGroupId, index);
						if (sxStoreLocations3.size() > 0) {
							List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
							String idstr = StringUtils.join(ids, ",");
							sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
						}
					}
					sxStoreLocationGroupMapper.updateMapById(sxStoreLocationGroupId,
							MapUtils.put("entrance2Property1", null).put("entrance2Property2", null)
									.put("entrance1Property1", null).put("entrance1Property2", null).getMap(),
							SxStoreLocationGroup.class);

				}
			}
		}
	}

	@Override
	@Transactional
	public void computeIsInBoundLocation() throws Exception {
		List<SxStoreLocationGroup> sxStoreLocationGroups = sxStoreLocationGroupMapper
				.findByMap(new HashMap<String, Object>(), SxStoreLocationGroup.class);
		for (SxStoreLocationGroup sxStoreLocationGroup : sxStoreLocationGroups) {
			if (sxStoreLocationGroup.getEntrance() == 3) {
				// 奇数货位则最中间为可用货位
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				int locationNum = sxStoreLocationGroup.getLocationNum();
				if (locationNum % 2 == 0) {
					int index1 = locationNum / 2;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index1).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					int index2 = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index2).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations3.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocationsNotIndex = sxStoreLocationMapper
							.findNotIndexTwo(sxStoreLocationGroupId, index1, index2);
					if (sxStoreLocationsNotIndex.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocationsNotIndex, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				} else {
					int index = locationNum / 2 + 1;
					List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper.findByMap(MapUtils
							.put("storeLocationGroupId", sxStoreLocationGroupId).put("locationIndex", index).getMap(),
							SxStoreLocation.class);
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("deptNum", 0).put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
					List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
							index);
					if (sxStoreLocations3.size() > 0) {
						List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
						String idstr = StringUtils.join(ids, ",");
						sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
					}
				}
			} else if (sxStoreLocationGroup.getEntrance() == 1) {
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				int haveStoreIndex = sxStoreLocationGroup.getLocationNum();
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", haveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocations2.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
						haveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			} else if (sxStoreLocationGroup.getEntrance() == 2) {
				int sxStoreLocationGroupId = sxStoreLocationGroup.getId();
				int haveStoreIndex = 1;
				List<SxStoreLocation> sxStoreLocations2 = sxStoreLocationMapper
						.findByMap(MapUtils.put("storeLocationGroupId", sxStoreLocationGroupId)
								.put("locationIndex", haveStoreIndex).getMap(), SxStoreLocation.class);
				if (sxStoreLocations2.size() == 1) {
					sxStoreLocationMapper.updateMapById(sxStoreLocations2.get(0).getId(),
							MapUtils.put("isInBoundLocation", 1).getMap(), SxStoreLocation.class);
				}
				List<SxStoreLocation> sxStoreLocations3 = sxStoreLocationMapper.findNotIndex(sxStoreLocationGroupId,
						haveStoreIndex);
				if (sxStoreLocations3.size() > 0) {
					List<Integer> ids = ListHelper.select(sxStoreLocations3, p -> p.getId());
					String idstr = StringUtils.join(ids, ",");
					sxStoreLocationMapper.updateNotIsInboundLocation(idstr);
				}
			}
		}
	}
}
