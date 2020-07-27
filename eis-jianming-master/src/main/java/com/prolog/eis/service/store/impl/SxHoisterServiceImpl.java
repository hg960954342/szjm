package com.prolog.eis.service.store.impl;

import org.springframework.stereotype.Service;

@Service
public class SxHoisterServiceImpl{

	/*@Autowired
	private SxHoisterMapper sxHoisterMapper;
	
	@Override
	public void update(int id,String hoisterNo,int islock,int hoisterType) throws Exception{
		if(id==0) {
			throw new Exception("提升机的Id不能为空");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(hoisterNo)) {
			map.put("hoisterNo", hoisterNo);
		}
		if(hoisterType!=0) {
			map.put("hoisterType", hoisterType);
		}
		if(islock!=0) {
			map.put("isLock", islock);
		}
		sxHoisterMapper.updateMapById(id,map, SxHoister.class);
	}

	@Override
	public void add(String hoisterNo,int isLock,int hoisterType) throws Exception {
		List<SxHoister> sxHoisters = sxHoisterMapper.findByMap(MapUtils.put("hoisterNo", hoisterNo).getMap(), SxHoister.class);
		if(sxHoisters.size()>0) {
			throw new Exception("提升机编号不能重复");
		}
		
		if(StringUtils.isBlank(hoisterNo)) {
			throw new Exception("提升机编号不能为空");
		}
		if(hoisterType==0) {
			throw new Exception("提升机类型不能为空");
		}
		if(isLock==0) {
			throw new Exception("提升机锁定不能为空");
		}
		
		SxHoister sxHoister = new SxHoister();
		sxHoister.setHoisterNo(hoisterNo);
		sxHoister.setIsLock(isLock);
		sxHoister.setHoisterType(hoisterType);
		sxHoister.setErrorStatus(2);
		sxHoister.setCreateTime(PrologDateUtils.parseObject(new Date()));
		sxHoisterMapper.save(sxHoister);
	}
	
	@Override
	public List<SxHoister> getAllSxHoisters(){
		return sxHoisterMapper.findByMap(new HashMap<String, Object>(), SxHoister.class);
	}
	
	@Override
	public void deleteSxHoister(int id){
		sxHoisterMapper.deleteById(id, SxHoister.class);
	}*/
}
