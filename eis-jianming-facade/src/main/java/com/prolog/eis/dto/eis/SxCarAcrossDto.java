package com.prolog.eis.dto.eis;

import com.prolog.eis.model.caracross.SxCarAcrossTask;
import com.prolog.eis.model.store.SxCarAcross;

import java.io.Serializable;
import java.util.List;

/**
 * 小车跨层表(SxCarAcross)实体类
 *
 * @author panteng
 * @since 2020-04-13 15:49:21
 */
public class SxCarAcrossDto extends SxCarAcross implements Serializable {
    private static final long serialVersionUID = 567482874639713738L;
    private List<SxCarAcrossTask> tasks;

    public List<SxCarAcrossTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<SxCarAcrossTask> tasks) {
        this.tasks = tasks;
    }
}