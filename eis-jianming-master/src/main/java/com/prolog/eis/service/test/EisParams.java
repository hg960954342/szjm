package com.prolog.eis.service.test;

import java.util.List;

public class EisParams{

        private List<CarryListBean> carryList;

        public List<CarryListBean> getCarryList() {
            return carryList;
        }

        public void setCarryList(List<CarryListBean> carryList) {
            this.carryList = carryList;
        }

public static class CarryListBean {
    /**
     * taskId : 71c91cfb5c44462795c0b874ecf88d38
     * type : 2
     * bankId : 0
     * containerNo : 802990
     * address : 0100230022
     * target : 0200210014
     * weight : 0
     * priority : 99
     * status : 0
     */

    private String taskId;
    private int type;
    private int bankId;
    private String containerNo;
    private String address;
    private String target;
    private String weight;
    private String priority;
    private int status;


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
}
