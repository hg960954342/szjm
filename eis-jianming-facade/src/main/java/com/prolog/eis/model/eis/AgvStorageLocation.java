package com.prolog.eis.model.eis;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * agv 位置存储实体类
 */
@Table("agv_storagelocation")
public class AgvStorageLocation {
    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("ceng")
    @ApiModelProperty("楼层")
    private int ceng;

    @Column("x")
    @ApiModelProperty("坐标x")
    private int x;

    @Column("y")
    @ApiModelProperty("坐标y")
    private int y;

    @Column("location_type")
    @ApiModelProperty("位置类型")
    private int locationType;

    @Column("tally_code")
    @ApiModelProperty("wms货位")
    private String tallyCode;

    @Column("task_lock")
    @ApiModelProperty("任务锁")
    private int taskLock;

    @Column("lock")
    @ApiModelProperty("锁定")
    private int lock;

    @Column("device_no")
    @ApiModelProperty("设备编号")
    private String deviceNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCeng() {
        return ceng;
    }

    public void setCeng(int ceng) {
        this.ceng = ceng;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLocationType() {
        return locationType;
    }

    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }

    public String getTallyCode() {
        return tallyCode;
    }

    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }

    public int getTaskLock() {
        return taskLock;
    }

    public void setTaskLock(int taskLock) {
        this.taskLock = taskLock;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
}
