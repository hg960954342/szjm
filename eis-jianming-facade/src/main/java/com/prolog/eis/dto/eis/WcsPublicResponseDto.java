package com.prolog.eis.dto.eis;

import java.io.Serializable;

/**
 * @author panteng
 * @description: wms公共请求dto
 * @date 2020/2/2 13:49
 */
public class WcsPublicResponseDto<T> implements Serializable {
    private boolean ret;
    private String msg;
    private String code;
    private T data;

    public static <T> WcsPublicResponseDto<T> newInstance(boolean ret, String msg, T data) {
        return new WcsPublicResponseDto(ret, msg, data,"200");
    }

    public WcsPublicResponseDto(boolean ret, String msg, T data, String code) {
        this.ret = ret;
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
