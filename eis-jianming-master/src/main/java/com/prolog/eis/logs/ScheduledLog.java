package com.prolog.eis.logs;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @description TODO
 * @date 2020/8/26 14:25
 */
@Table("scheduled_log")
public class ScheduledLog {
    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;
    @Column("class_name")
    @ApiModelProperty("类名简写")
    private String className;

    @Column("method_name")
    @ApiModelProperty("方法名")
    private String methodName;
    @Column("run_time")
    @ApiModelProperty("执行时间")
    private String runTime;
    @Column("start_time")
    @ApiModelProperty("开始时间")
    private Date startTime;
    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
