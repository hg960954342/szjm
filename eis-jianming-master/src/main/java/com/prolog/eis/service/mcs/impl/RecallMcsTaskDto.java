package com.prolog.eis.service.mcs.impl;

import com.prolog.eis.model.mcs.MCSTask;

public class RecallMcsTaskDto {

    MCSTask mcsTask;
    boolean isSuccess;

    public MCSTask getMcsTask() {
        return mcsTask;
    }

    public void setMcsTask(MCSTask mcsTask) {
        this.mcsTask = mcsTask;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
