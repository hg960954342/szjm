package com.prolog.eis.service.store.impl;

import org.springframework.stereotype.Service;

@Service
public class StoreLocationServiceImpl {

    /*@Autowired
    private SxStoreLocationGroupMapper sxStoreLocationGroupMapper;
    @Autowired
    private SxStoreLocationMapper sxStoreLocationMapper;
    @Autowired
    private SxStoreTaskFinishService sxStoreTaskFinishService;
    @Autowired
    private SxVerticalLocationGroupMapper sxVerticalLocationGroupMapper;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public void importStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos) throws Exception {
        List<StoreLocationGroupDto> saveList = checkStoreLocationGroup(storeLocationGroupDtos);
        if(CollectionUtils.isEmpty(saveList)){
            return;
        }
        //寻找中间货位
        findXy(saveList);
        //保存货位组
        sxStoreLocationGroupMapper.saveBatchReturnKey(saveList);
        //保存货位
        saveStoreLocation(saveList);
    }

    *//**
     * 效验货位组  目前货位更新  只更新了xy 指定货位索引更新
     * 是否全量更新
     * @param storeLocationGroupDtos
     *//*
    private List<StoreLocationGroupDto> checkStoreLocationGroup(List<StoreLocationGroupDto> storeLocationGroupDtos) throws Exception {
        //查找已存在的货位组
        List<String> groupNos = storeLocationGroupDtos.stream().map(StoreLocationGroupDto::getGroupNo).collect(Collectors.toList());
        String groupNosStr = StringUtils.join(groupNos, ",");
        List<StoreLocationGroupDto> existgroups = sxStoreLocationGroupMapper.findByGroupNos(groupNosStr);
        if(CollectionUtils.isEmpty(existgroups)){
            //没有已存在的货位组
            return storeLocationGroupDtos;
        }
        Map<String, StoreLocationGroupDto> map = existgroups.stream().collect(Collectors.toMap(StoreLocationGroupDto::getGroupNo, Function.identity()));

        for (StoreLocationGroupDto storeLocationGroupDto : storeLocationGroupDtos) {
            if(map.containsKey(storeLocationGroupDto.getGroupNo())){
                StoreLocationGroupDto dto = map.get(storeLocationGroupDto.getGroupNo());
                if(dto.getLocationNum() != storeLocationGroupDto.getLocationNum()){
                    throw new Exception(MessageFormat.format("货位组数量不一致,导入货位组数据:{0},数据库货位组货位数量:{1}", PrologApiJsonHelper.toJson(storeLocationGroupDto), PrologApiJsonHelper.toJson(dto)));
                }
                storeLocationGroupDto.setId(dto.getId());
            }
        }
        //效验通过
        List<StoreLocationGroupDto> list = storeLocationGroupDtos.stream().filter(t -> map.containsKey(t.getGroupNo())).collect(Collectors.toList());
        //更新已存在的货位组
        updateExistData(list);
        List<StoreLocationGroupDto> saceList = storeLocationGroupDtos.stream().filter(t -> !map.containsKey(t.getGroupNo())).collect(Collectors.toList());
        return saceList;
    }

    //更新已存在的货位组
    private void updateExistData(List<StoreLocationGroupDto> list) {
        List<SxStoreLocation> storeLocations = new ArrayList<>();
        list.stream().forEach(t -> {
            List<SxStoreLocation> storeLocation = t.getStoreLocations();
            storeLocation.stream().forEach(k -> k.setStoreLocationGroupId(t.getId()));
            storeLocations.addAll(storeLocation);
        });
        findXy(list);
        //根据id批量更新货位组
        sxStoreLocationGroupMapper.batchUpdateById(list);
        //更新货位
        sxStoreLocationMapper.batchUpdateById(storeLocations);
    }

    private void saveStoreLocation(List<StoreLocationGroupDto> storeLocationGroupDtos) {
        List<SxStoreLocation> storeLocations = new ArrayList<>();
        storeLocationGroupDtos.stream().forEach(t -> {
            List<SxStoreLocation> storeLocation = t.getStoreLocations();
            int id = t.getId();
            storeLocation.stream().forEach(s -> {
                s.setStoreLocationGroupId(id);
                s.setLayer(t.getLayer());
                s.setStoreNo(PrologCoordinateUtils.splicingStr(s.getX(),s.getY(),t.getLayer()));
            });
            storeLocations.addAll(storeLocation);
        });
        sxStoreLocationMapper.saveBatchReturnKey(storeLocations);
        //找相邻货位
        findAdjacentStore(storeLocations);
        //入库货位计算出来
        CompletableFuture.runAsync(() -> {
            try {
                sxStoreTaskFinishService.computeIsInBoundLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void findAdjacentStore(List<SxStoreLocation> storeLocations) {
        Map<Integer, List<SxStoreLocation>> collect = storeLocations.stream().collect(Collectors.groupingBy(SxStoreLocation::getStoreLocationGroupId));
        collect.forEach((k, v) -> {
            if (v.size() > 1) {
                List<SxStoreLocation> locations = v.stream().sorted(Comparator.comparing(SxStoreLocation::getLocationIndex)).collect(Collectors.toList());
                for(int i=0;i<locations.size();i++){
                    SxStoreLocation sxStoreLocation = locations.get(i);
                    if(i != 0){
                        sxStoreLocation.setStoreLocationId1(locations.get(i-1).getId());
                    }
                    if(i != locations.size()-1){
                        sxStoreLocation.setStoreLocationId2(locations.get(i+1).getId());
                    }
                }
            }
        });
        sxStoreLocationMapper.updateAdjacentStore(storeLocations);
    }

    private void findXy(List<StoreLocationGroupDto> storeLocationGroupDtos) {
        for (StoreLocationGroupDto storeLocationGroupDto : storeLocationGroupDtos) {
            List<SxStoreLocation> storeLocations = storeLocationGroupDto.getStoreLocations();
            if (CollectionUtils.isEmpty(storeLocations)) {
                continue;
            }
            storeLocationGroupDto.setInOutNum(storeLocationGroupDto.getEntrance()<3?1:2);
            storeLocationGroupDto.setLocationNum(storeLocations.size());
            int size = storeLocations.size();
            int ceil = (int) Math.ceil(size / 2);
            //获取货位
            SxStoreLocation dto = storeLocations.stream().filter(t -> t.getLocationIndex() == ceil).findFirst().orElse(storeLocations.get(0));
            //赋值
            storeLocationGroupDto.setX(dto.getX());
            storeLocationGroupDto.setY(dto.getY());
            //深度赋值
            depthCalc(storeLocationGroupDto,storeLocations);
        }
    }

    private void depthCalc(StoreLocationGroupDto storeLocationGroupDto, List<SxStoreLocation> storeLocations) {
        // 入口类型：1、仅入口1 向上，2、仅入口2  向下，3、入口1+入口2
        int entrance = storeLocationGroupDto.getEntrance();
        int orginIndex;
        if(entrance == 1){
            orginIndex = storeLocations.stream().min(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
            getDepthByOneOrgin(orginIndex,storeLocations);
        }else if(entrance == 2){
            orginIndex = storeLocations.stream().max(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
            getDepthByOneOrgin(orginIndex,storeLocations);
        }else if(entrance == 3){
            getDepthByTwoOrgin(storeLocations);
        }
    }

    //获取深度  两个入口
    private void getDepthByTwoOrgin(List<SxStoreLocation> storeLocations) {
        int minIndex = storeLocations.stream().min(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
        int maxIndex = storeLocations.stream().max(Comparator.comparing(SxStoreLocation::getLocationIndex)).get().getLocationIndex();
        storeLocations.stream().forEach(t ->{
            int min = Math.min(Math.abs(t.getLocationIndex() - minIndex), Math.abs(t.getLocationIndex() - maxIndex));
            t.setDepth(min);
        });
    }

    //获取深度  一个入口
    private void getDepthByOneOrgin(int orginIndex, List<SxStoreLocation> storeLocations) {
        storeLocations.stream().forEach(t ->{
            t.setDepth(Math.abs(t.getLocationIndex()-orginIndex));
        });
    }

	@Override
	public List<SxStoreLocation> findByGroupId(int groupId) throws Exception {
		List<SxStoreLocation> sxStoreLocations = sxStoreLocationMapper.findByMap(MapUtils.put("storeLocationGroupId", groupId).getMap(), SxStoreLocation.class);
		return sxStoreLocations;
	}

    *//**
     * throws Exception
     * @param verticalLocationDtos
     * @return
     * @throws Exception
     *//*
    @Override
    public List<VerticalLocationDto> importVerticalLocation(List<VerticalLocationDto> verticalLocationDtos) throws Exception {
        //判断数据是否已经导入
        List<VerticalLocationDto> errors = Lists.newArrayList();
        List<String> groupNos = verticalLocationDtos.stream().map(VerticalLocationDto::getGroupNo).distinct().collect(Collectors.toList());
        String join = StringUtils.join(groupNos, ',');

        List<String> existGroupNos = sxVerticalLocationGroupMapper.findByGroupNos(join);
        if(CollectionUtils.isNotEmpty(existGroupNos)){
            verticalLocationDtos.stream().filter(t -> existGroupNos.contains(t.getGroupNo()))
                                 .forEach(k -> {
                                     k.setErrorMsg("该垂直货位已导入，请勿重复导入或停止系统运行，删除垂直货位组数据重新导入");
                                     errors.add(k);
                                 });
        }
        List<VerticalLocationDto> dtoList = verticalLocationDtos.stream().filter(t -> !existGroupNos.contains(t.getGroupNo())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(dtoList)){
            return errors;
        }

        //效验货位组编号是否存在
        List<StoreLocationGroupDto> byGroupNos = sxStoreLocationGroupMapper.findByGroupNos(join);
        List<String> existStoreGroupNos = byGroupNos.stream().map(StoreLocationGroupDto::getGroupNo).collect(Collectors.toList());
        List<VerticalLocationDto> locationDtoList = dtoList.stream().filter(t -> !existStoreGroupNos.contains(t.getGroupNo())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(locationDtoList)){
            locationDtoList.stream().forEach(t -> t.setErrorMsg("该货位组编号在系统中不存在，请检查货位组编号"));
            errors.addAll(locationDtoList);
        }

        List<VerticalLocationDto> locationDtos = dtoList.stream().filter(t -> existStoreGroupNos.contains(t.getGroupNo())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(locationDtos)){
            return errors;
        }

        //效验货位编号是否存在
        List<String> storeNos = locationDtos.stream().map(VerticalLocationDto::getStoreNo).distinct().collect(Collectors.toList());
        String storeNoStr = StringUtils.join(storeNos, ',');
        List<String> existStoreNos = sxStoreLocationMapper.findByStoreNos(storeNoStr);

        List<VerticalLocationDto> dtos = locationDtos.stream().filter(t -> !existStoreNos.contains(t.getStoreNo())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(dtos)){
            dtos.stream().forEach(t -> t.setErrorMsg("该货位编号在系统中不存在，请检查货位编号"));
            errors.addAll(locationDtoList);
        }

        List<VerticalLocationDto> list = locationDtos.stream().filter(t -> existStoreNos.contains(t.getStoreNo())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(list)){
            return errors;
        }

        //导入
        errors.addAll(importVerticalList(list));
        return errors;
    }

    private List<VerticalLocationDto> importVerticalList(List<VerticalLocationDto> list) {
        ArrayList<SxVerticalLocationGroup> newArrayList = Lists.newArrayList();
        Map<String, Double> map = list.stream().collect(Collectors.toMap(VerticalLocationDto::getGroupNo, VerticalLocationDto::getLimitWeight, (K1, K2) -> K1));
        map.forEach((k,v) -> {
            SxVerticalLocationGroup sxVerticalLocationGroup = new SxVerticalLocationGroup();
            sxVerticalLocationGroup.setLimitWeight(v);
            sxVerticalLocationGroup.setGroupNo(k);
            newArrayList.add(sxVerticalLocationGroup);
        });
        sxVerticalLocationGroupMapper.saveBatch(newArrayList);
        Map<String, Integer> integerMap = newArrayList.stream().collect(Collectors.toMap(SxVerticalLocationGroup::getGroupNo, SxVerticalLocationGroup::getId, (K1, K2) -> K1));

        list.stream().forEach(t -> {
            if(integerMap.containsKey(t.getGroupNo())){
                t.setId(integerMap.get(t.getGroupNo()));
            }
        });

        List<VerticalLocationDto> noIds = list.stream().filter(t -> t.getId() == 0).collect(Collectors.toList());
        noIds.stream().forEach(t -> t.setErrorMsg("垂直货位组保存失败,请检查程序"));
        List<VerticalLocationDto> ups = list.stream().filter(t -> t.getId() != 0).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(ups)){
            sxStoreLocationMapper.updateVerticalLocations(ups);
        }
        return noIds;
    }
*/
}
