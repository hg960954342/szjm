package com.prolog.eis.service.store;

public interface MCSCallBack {

    void container(String containerCode,int targetLayer,int targetX,int targetY,String address) throws Exception;
}
