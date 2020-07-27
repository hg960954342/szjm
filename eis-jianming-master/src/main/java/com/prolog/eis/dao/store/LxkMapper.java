package com.prolog.eis.dao.store;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.prolog.eis.dto.eis.StackerStoreDto;
import com.prolog.eis.dto.eis.StackerZtDto;

public interface LxkMapper {

	@Select("select s.id, s.box_no as boxNo, l.address From stacker_store s left join stacker_store_location l on s.store_location_id = l.id where s.box_no = ${boxNo}")
	StackerStoreDto getLxkStore(@Param("boxNo") String boxNo);

	@Delete("delete from stacker_store s where s.box_no = ${boxNo}")
	void deleteLxkStore(@Param("boxNo") String boxNo);

	@Select("select t.id, t.box_no as boxNo from lxk_zt_store t where t.box_no = ${boxNo}")
	StackerZtDto getLxkZt(@Param("boxNo") String boxNo);

	@Delete("delete from lxk_zt_store s where s.box_no = ${boxNo}")
	void deleteLxkZtStore(@Param("boxNo") String boxNo);
	
}
