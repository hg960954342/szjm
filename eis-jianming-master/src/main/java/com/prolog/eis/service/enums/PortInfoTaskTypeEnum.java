package com.prolog.eis.service.enums;

/**
 * 出入库taskType类型枚举
 */
public enum PortInfoTaskTypeEnum {


    TASK(1), // 1任务托
    PACKING_MATERIA(2),//2包材
    EMPTY_BUTTRESS(3),//3 空托剁或者空托叠
    EMPTY_TRAY(4), //4空托盘
    QUALITY_TESTING(5); //5质检

    private Integer taskType;

    PortInfoTaskTypeEnum(Integer taskType) {
        this.taskType = taskType;
    }

    public Integer getTaskType() {
        return taskType;
    }
}
