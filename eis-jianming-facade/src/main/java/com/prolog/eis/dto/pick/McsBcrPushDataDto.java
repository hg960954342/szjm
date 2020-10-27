package com.prolog.eis.dto.pick;

/**
 * @author chengxudong
 * @description
 **/
public class McsBcrPushDataDto {


    /**
     * success : true
     * message : 操作成功
     * code : 200
     * data : {"bcrPointValue":"2"}
     */

    private boolean success;
    private String message;
    private String code;
    private DataBean data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        /**
         * bcrPointValue : 2
         */

        private String bcrPointValue;

        public String getBcrPointValue() {
            return bcrPointValue;
        }

        public void setBcrPointValue(String bcrPointValue) {
            this.bcrPointValue = bcrPointValue;
        }
    }
}
