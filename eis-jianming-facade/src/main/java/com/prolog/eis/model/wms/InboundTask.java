package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table("inbound_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboundTask {


    @Column("id")
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
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

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    @JsonProperty("containercode")
    private String containerCode;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    @JsonProperty("tasktype")
    private int taskType;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    @JsonProperty("itemid")
    private String itemId;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    @JsonProperty("qty")
    private float qty;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    @JsonProperty("lotid")
    private String lotId;

    @Column("ceng")
    @ApiModelProperty("入库楼层")
    @JsonProperty("ceng")
    private String ceng;

    @Column("agv_loc")
    @ApiModelProperty("Agv搬运点")
    @JsonProperty("agvloc")
    private String agvLoc;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JsonProperty("ownerid")
    private String ownerId;

    @Column("task_state")
    @ApiModelProperty("0 创建 1开始 3扫码入库 4完成")
    private int taskState;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("start_time")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @Column("ruku_time")
    @ApiModelProperty("扫码入库时间")
    private Date rukuTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;


}
