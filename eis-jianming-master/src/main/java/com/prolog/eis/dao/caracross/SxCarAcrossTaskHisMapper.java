package com.prolog.eis.dao.caracross;

import com.prolog.eis.model.caracross.SxCarAcrossTaskHis;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

/**
 * 小车跨层GCS/MCS任务表(SxCarAcrossTask)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-13 14:54:57
 */
public interface SxCarAcrossTaskHisMapper extends BaseMapper<SxCarAcrossTaskHis>{

    /**
     * 转历史
     * @param id
     */
    @Insert("INSERT INTO sx_car_across_task_his SELECT *  from sx_car_across_task t where t.across_id = #{id}")
    void toHistoryByAcrossId(Integer id);
}