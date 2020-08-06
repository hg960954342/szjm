package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("billno")
    private String billNo;

    @Column("wms_push")
    @ApiModelProperty("是否wms下发，0不是，1是")
    private int wmsPush;

    @Column("reback")
    @ApiModelProperty("是否回传，0不回传，1回传")
    private int reBack;

    @Column("empty_container")
    @ApiModelProperty("0任务托  1空托")
    private int emptyContainer;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    @JsonProperty("tasktype")
    private int taskType;

    @Column("sfreq")
    @ApiModelProperty("站点要求 0 无   1有")
    @JsonProperty("sfreq")
    private int sfReq;

    private float qty;

    @Column("pick_code")
    @ApiModelProperty("拣选站")
    @JsonProperty("pickcode")
    private String pickCode;

    @Column("ownerid")
    @ApiModelProperty("wms业主")
    @JsonProperty("ownerid")
    private String ownerId;


    private List<OutboundTaskDetail> details;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;




}
