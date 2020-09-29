package com.prolog.eis.service.test;

import java.util.List;

public class RcsParams {


    /**
     * positionCodePath : [{"positionCode":"057200AB054000","type":"00"},{"positionCode":"054320AB048300","type":"00"}]
     * tokenCode :
     * podDir :
     * agvCode :
     * data :
     * reqTime : 2020 09 23 14:09:37
     * priority : 1
     * wbCode :
     * reqCode : 42368bf18c714ead806c5b6a197ad73d
     * taskCode : 42368bf18c714ead806c5b6a197ad73d
     * clientCode :
     * podCode :
     * taskTyp : F01
     * interfaceName : genAgvSchedulingTask
     */

    private String tokenCode;
    private String podDir;
    private String agvCode;
    private String data;
    private String reqTime;
    private String priority;
    private String wbCode;
    private String reqCode;
    private String taskCode;
    private String clientCode;
    private String podCode;
    private String taskTyp;
    private String interfaceName;
    private List<PositionCodePathBean> positionCodePath;

    public String getTokenCode() {
        return tokenCode;
    }

    public void setTokenCode(String tokenCode) {
        this.tokenCode = tokenCode;
    }

    public String getPodDir() {
        return podDir;
    }

    public void setPodDir(String podDir) {
        this.podDir = podDir;
    }

    public String getAgvCode() {
        return agvCode;
    }

    public void setAgvCode(String agvCode) {
        this.agvCode = agvCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWbCode() {
        return wbCode;
    }

    public void setWbCode(String wbCode) {
        this.wbCode = wbCode;
    }

    public String getReqCode() {
        return reqCode;
    }

    public void setReqCode(String reqCode) {
        this.reqCode = reqCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getPodCode() {
        return podCode;
    }

    public void setPodCode(String podCode) {
        this.podCode = podCode;
    }

    public String getTaskTyp() {
        return taskTyp;
    }

    public void setTaskTyp(String taskTyp) {
        this.taskTyp = taskTyp;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public List<PositionCodePathBean> getPositionCodePath() {
        return positionCodePath;
    }

    public void setPositionCodePath(List<PositionCodePathBean> positionCodePath) {
        this.positionCodePath = positionCodePath;
    }

    static class PositionCodePathBean {
        /**
         * positionCode : 057200AB054000
         * type : 00
         */

        private String positionCode;
        private String type;

        public String getPositionCode() {
            return positionCode;
        }

        public void setPositionCode(String positionCode) {
            this.positionCode = positionCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
