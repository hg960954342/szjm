package com.prolog.eis.service;

import com.prolog.eis.model.wms.ContainerTask;
import com.prolog.eis.model.wms.RepeatReport;

public interface EisCallbackService {
    /**
     * eis 业务入库 回告 wms
     * @param containerCode 托盘编号
     * @throws Exception
     */
    void inBoundReport(String containerCode) ;

    /**
     * eis 业务出库 回告 wms
     * @param containerTask
     * @throws Exception
     */
    void outBoundReport(ContainerTask containerTask);

    /**
     * eis 移库出库 回告 wms
     * @param containerTask
     * @throws Exception
     */
    void moveBoundReport(ContainerTask containerTask);

    /**
     * eis 盘点出库 回告 wms
     * @param billNo 单据号
     * @throws Exception
     */
    void checkBoundReport(String billNo);



    /**
     * 回告成功更新数据
     */
    void updateResport(boolean  isSuccess,RepeatReport repeatReport);

}
