package com.prolog.eis.dto.mcs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panteng
 * @description: MCS读码器及称重设备状态上报
 * @date 2020/5/21 10:14
 */
public class CodeReadeEquipStatusDto implements Serializable {
    /**
     * 编号
     */
    private String id;

    /**
     * 编号
     */
    private String name;

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     *是否在线（true:在线，false，不在线）
     */
    private boolean isOnline;

    /**
     *生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
