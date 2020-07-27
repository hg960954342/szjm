package com.prolog.eis.dao.caracross;

import com.prolog.eis.model.caracross.SxCarAcrossTask;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 小车跨层GCS/MCS任务表(SxCarAcrossTask)表数据库访问层
 *
 * @author panteng
 * @since 2020-04-13 14:54:57
 */
public interface SxCarAcrossTaskMapper extends BaseMapper<SxCarAcrossTask>{

    /**
     * 查询任务
     * @param task
     * @return
     */

    @Select("SELECT\n" +
            "    id as  id,\n" +
            "                across_id as  acrossId,\n" +
            "                task_id as  taskId,\n" +
            "                floor as  floor,\n" +
            "                task_type as  taskType,\n" +
            "                systype as  systype,\n" +
            "                hoistId as  hoistid,\n" +
            "                send_err_msg as  sendErrMsg,\n" +
            "                send_status as  sendStatus,\n" +
            "                send_count as  sendCount,\n" +
            "                create_time as  createTime,\n" +
            "                finish_time as  finishTime \n" +
            "FROM sx_car_across_task t\n" +
            " where t.across_id = #{task.acrossId} and t.task_type = #{task.taskType}" +
            " and t.systype=#{task.systype} and t.task_status != 3 and t.send_status != 1")
    SxCarAcrossTask findOneSendTask(@Param("task") SxCarAcrossTask task);
}