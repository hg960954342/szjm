package com.prolog.eis.util;

/**
 * @author panteng
 * @description: 中外运常量
 * @date 2020/2/17 10:47
 */
public interface QcConstant {

    String POSITION_SEPARATOR = "/";

    String MCS = "MCS";
    String GCS = "GCS";


    //AGV任务开关(0 关闭， 1开启)
    String SYS_KEY_AGV_TASK_SWITCH = "AGV_TASK_SWITCH";
    //四向库全局任务开关(0 关闭， 1开启)
    String SYS_KEY_GLOBAL_TASK_SWITCH = "GLOBAL_TASK_SWITCH";
}
