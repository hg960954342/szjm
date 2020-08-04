package com.prolog.eis.service.login;

public interface WmsLoginService {

    /**
     * 登录获取token
     */
    void loginWms();

    /**
     * 刷新获取token
     */
    void flushToken();
}
