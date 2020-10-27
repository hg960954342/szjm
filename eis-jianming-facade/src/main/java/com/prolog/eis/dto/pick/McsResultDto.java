package com.prolog.eis.dto.pick;

/**
 * @author chengxudong
 * @description
 **/
public class McsResultDto {


    /**
     * success : true
     * message : 操作成功
     * code : 200
     * data : null
     */


    private boolean success;
    private String message;
    private String code;
    private Object data;


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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
