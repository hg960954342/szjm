package com.prolog.eis.model.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTask {

    private int id;

    private String billno;

    private int wmspush;

    private int reback;

    private int emptycontainer;

    private int tasktype;

    private int sfreq;

    private String pickcode;

    private String ownerid;

    private List<OutboundTaskDetail> details;

    private Date createTime;

    private Date endTime;




}
