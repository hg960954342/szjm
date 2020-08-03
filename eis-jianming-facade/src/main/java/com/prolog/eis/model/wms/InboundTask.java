package com.prolog.eis.model.wms;

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

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    private String containercode;

    @Column("task_type")
    @ApiModelProperty("任务托暂未定   空托的情况 0空托垛入库  1空托碟")
    private int tasktype;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    private String itemid;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    private float qty;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    private String lotid;

    @Column("ceng")
    @ApiModelProperty("入库楼层")
    private String ceng;

    @Column("agv_loc")
    @ApiModelProperty("Agv搬运点")
    private String agvloc;

    @Column("ownerid")
    @ApiModelProperty("wms业主")
    private String ownerid;

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
