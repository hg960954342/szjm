package com.prolog.eis.model.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundTask {

    private int id;

    private String billno;

    private int wmspush;

    private int reback;

    private int emptycontainer;

    private String containercode;


    private int tasktype;

    private String itemid;


    private float qty;


    private String lotid;


    private String ceng;


    private String agvloc;


    private String ownerid;


    private int taskState;


    private Date createTime;

    private Date startTime;

    private Date rukuTime;

    private Date endTime;


}
