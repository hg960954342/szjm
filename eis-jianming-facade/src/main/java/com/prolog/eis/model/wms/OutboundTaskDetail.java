package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("billno")
    private String billNo;

    @Column("seqno")
    @ApiModelProperty("明细行号")
    @JsonProperty("seqno")
    private String seqNo;

    @Column("ctreq")
    @ApiModelProperty("是否指定托盘 0不指定 1指定")
    @JsonProperty("ctreq")
    private int ctReq;

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    @JsonProperty("containercode")
    private int containerCode;

    @Column("ownerid")
    @ApiModelProperty("wms业主")
    @JsonProperty("ownerid")
    private String ownerId;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    @JsonProperty("itemid")
    private String itemId;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    @JsonProperty("lotid")
    private String lotId;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    @JsonProperty("qty")
    private float qty;

    @Column("finish_qty")
    @ApiModelProperty("完成数量（重量）")
    private float finishQty;

    @Column("pick_code")
    @ApiModelProperty("拣选站  指定拣选站  暂时移库出库用到")
    @JsonProperty("pickcode")
    private String pickCode;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;
}
