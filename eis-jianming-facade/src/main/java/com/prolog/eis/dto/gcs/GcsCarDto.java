package com.prolog.eis.dto.gcs;

/**
 * @author panteng
 * @description:
 * @date 2020/5/30 18:49
 */
public class GcsCarDto {


    /**
     * id : 1
     * name : RGV1
     * rgvType : 1
     * floor : 1
     * auto : false
     * ip : 20.20.20.1
     * area : 1
     * remark : null
     * waitNode : 0100270036
     * dumpEnergy : 36
     * dumpVoltage : 3206
     * alarm : 0
     * currCoord : 0100120010
     * containerId : null
     * createTime : 2020-04-20 15:19:53
     * updateTime : 2020-05-30 18:42:35
     * online : true
     * use : true
     * up : false
     * leisure : true
     */

    private String id;
    private String name;
    private String rgvType;
    private int floor;
    private boolean auto;
    private String ip;
    private String area;
    private Object remark;
    private String waitNode;
    private int dumpEnergy;
    private int dumpVoltage;
    private int alarm;
    private String currCoord;
    private Object containerId;
    private String createTime;
    private String updateTime;
    private boolean online;
    private boolean use;
    private boolean up;
    private boolean leisure;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRgvType() {
        return rgvType;
    }

    public void setRgvType(String rgvType) {
        this.rgvType = rgvType;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getWaitNode() {
        return waitNode;
    }

    public void setWaitNode(String waitNode) {
        this.waitNode = waitNode;
    }

    public int getDumpEnergy() {
        return dumpEnergy;
    }

    public void setDumpEnergy(int dumpEnergy) {
        this.dumpEnergy = dumpEnergy;
    }

    public int getDumpVoltage() {
        return dumpVoltage;
    }

    public void setDumpVoltage(int dumpVoltage) {
        this.dumpVoltage = dumpVoltage;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public String getCurrCoord() {
        return currCoord;
    }

    public void setCurrCoord(String currCoord) {
        this.currCoord = currCoord;
    }

    public Object getContainerId() {
        return containerId;
    }

    public void setContainerId(Object containerId) {
        this.containerId = containerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isLeisure() {
        return leisure;
    }

    public void setLeisure(boolean leisure) {
        this.leisure = leisure;
    }
}
