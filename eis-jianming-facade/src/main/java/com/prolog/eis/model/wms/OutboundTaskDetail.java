package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table("outbound_task_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTaskDetail {

    @Column("id")
    @ApiModelProperty("主键")
    private int id;

    @Column("bill_no")
    @ApiModelProperty("入库单号")
    private String billno;

    @Column("seqno")
    @ApiModelProperty("明细行号")
    private String seqno;

    @Column("ctreq")
    @ApiModelProperty("是否指定托盘 0不指定 1指定")
    private int ctreq;

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    private int containercode;

    @Column("ownerid")
    @ApiModelProperty("wms业主")
    private String ownerid;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    private String itemid;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    private String lotid;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    private float qty;

    @Column("finish_qty")
    @ApiModelProperty("完成数量（重量）")
    private float finishqty;

    @Column("pick_code")
    @ApiModelProperty("拣选站  指定拣选站  暂时移库出库用到")
    private String pickcode;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;
}
