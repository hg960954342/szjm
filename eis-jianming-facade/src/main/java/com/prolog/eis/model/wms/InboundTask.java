package com.prolog.eis.model.wms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundTask {


    private String billNo;


    private String containerCode;


    private String taskType;


    private String itemId;


    private Double qty;


    private String lotId;


    private String ceng;


    private String agvLoc;


    private String ownerId;


    private int taskState;


    private Date createTime;


    private Date endTime;


}
