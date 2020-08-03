package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Table("outbound_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTask {

    @Column("id")
    @ApiModelProperty("主键")
    private int id;

    @Column("bill_no")
    @ApiModelProperty("入库单号")
    private String billno;

    @Column("wms_push")
    @ApiModelProperty("是否wms下发，0不是，1是")
    private int wmspush;

    @Column("reback")
    @ApiModelProperty("是否回传，0不回传，1回传")
    private int reback;

    @Column("empty_container")
    @ApiModelProperty("0任务托  1空托")
    private int emptycontainer;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    private int tasktype;

    @Column("sfreq")
    @ApiModelProperty("站点要求 0 无   1有")
    private int sfreq;

    private float qty;

    @Column("pick_code")
    @ApiModelProperty("拣选站")
    private String pickcode;

    @Column("ownerid")
    @ApiModelProperty("wms业主")
    private String ownerid;


    private List<OutboundTaskDetail> details;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;




}
