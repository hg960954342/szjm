package com.prolog.eis.service.impl;

import com.prolog.eis.dto.rcs.RcsRequestResultDto;
import com.prolog.eis.model.wms.ContainerTask;

public class ResultAgvDto {

    ContainerTask containerTask;


    RcsRequestResultDto rcsRequestResultDto;


    public ContainerTask getContainerTask() {
        return containerTask;
    }

    public void setContainerTask(ContainerTask containerTask) {
        this.containerTask = containerTask;
    }

    public RcsRequestResultDto getRcsRequestResultDto() {
        return rcsRequestResultDto;
    }

    public void setRcsRequestResultDto(RcsRequestResultDto rcsRequestResultDto) {
        this.rcsRequestResultDto = rcsRequestResultDto;
    }
}
