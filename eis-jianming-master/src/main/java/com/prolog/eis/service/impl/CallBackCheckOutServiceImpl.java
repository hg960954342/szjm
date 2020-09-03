package com.prolog.eis.service.impl;

import com.prolog.eis.dao.CheckOutTaskMapper;
import com.prolog.eis.logs.LogServices;
import com.prolog.eis.service.CallBackCheckOutService;
import com.prolog.eis.service.EisCallbackService;
import com.prolog.eis.service.impl.unbound.entity.CheckOutTask;
import com.prolog.framework.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CallBackCheckOutServiceImpl implements CallBackCheckOutService {


    @Autowired
    private CheckOutTaskMapper checkOutTaskMapper;
    @Autowired
    private EisCallbackService eisCallbackService;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;

    public void updateCallBackCheckOut(String containerCode){

        List<CheckOutTask> checkOutTasks= checkOutTaskMapper.findByMap(MapUtils.put("containerCode", containerCode).getMap(), CheckOutTask.class);
        if(checkOutTasks.size()>1)  {
            LogServices.logSysBusiness("存在多条盘点任务错误！");
            return;
        }
        if(checkOutTasks.isEmpty()) return;
        CheckOutTask checkOutTask= checkOutTasks.get(0);
        //开启手动
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        checkOutTask.setState("3");
        checkOutTaskMapper.update(checkOutTask);
        dataSourceTransactionManager.commit(transactionStatus);//提交
       //获取当前托盘的所有盘点托盘
       String billNo= checkOutTask.getBillNo();
       Boolean isEnd=checkOutTaskMapper.getCheckoutTaskIsEnd(billNo);
       //盘点任务已经完成
       if(isEnd){  //写入重发表
           eisCallbackService.checkBoundReport(billNo);
       }



    }
}
