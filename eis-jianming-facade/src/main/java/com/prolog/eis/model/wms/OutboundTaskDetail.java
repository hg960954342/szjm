package com.prolog.eis.model.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTaskDetail {

    private int id;

    private String seqno;

    private int ctreq;

    private int containercode;

    private String ownerid;

    private String itemid;

    private String lotid;

    private float qty;

    private String pickcode;

    private Date createTime;

    private Date endTime;
}
