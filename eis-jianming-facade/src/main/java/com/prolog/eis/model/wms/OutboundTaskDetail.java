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
    @JsonProperty("BILLNO")
    private String billNo;

    @Column("seqno")
    @ApiModelProperty("明细行号")
    @JsonProperty("SEQNO")
    private String seqNo;

    @Column("ctreq")
    @ApiModelProperty("是否指定托盘 0不指定 1指定")
    @JsonProperty("CTREQ")
    private int ctReq;

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    @JsonProperty("CONTAINERCODE")
    private int containerCode;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JsonProperty("CONSIGNOR")
    private String ownerId;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    @JsonProperty("ITEMID")
    private String itemId;

    @Column("item_name")
    @ApiModelProperty("商品名称")
    @JsonProperty("SPMCH")
    private String itemName;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    @JsonProperty("LOTID")
    private String lotId;

    @Column("qty")
    @ApiModelProperty("数量（重量）")
    @JsonProperty("QTY")
    private float qty;

    @Column("finish_qty")
    @ApiModelProperty("完成数量（重量）")
    private float finishQty;

    @Column("pick_code")
    @ApiModelProperty("拣选站  指定拣选站  暂时移库出库用到")
    @JsonProperty("PICKCODE")
    private String pickCode;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("end_time")
    @ApiModelProperty("结束时间")
    private Date endTime;


}
