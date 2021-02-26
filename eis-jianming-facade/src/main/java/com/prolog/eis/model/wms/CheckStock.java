package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("check_stock")
public class CheckStock {

    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("container_code")
    @ApiModelProperty("母托盘编号")
    @JsonProperty("CONTAINERCODE")
    private String containerCode;

    @Column("owner_id")
    @ApiModelProperty("wms业主")
    @JsonProperty("CONSIGNOR")
    private String ownerId;

    @Column("item_id")
    @ApiModelProperty("wms商品id")
    @JsonProperty("ITEMID")
    private String itemId;

    @Column("lot_id")
    @ApiModelProperty("wms批号")
    @JsonProperty("LOTID")
    private String lotId;

    @Column("diff_qty")
    @ApiModelProperty("库存差异数量（重量）")
    @JsonProperty("DIFFQTY")
    private float diffQty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public float getDiffQty() {
        return diffQty;
    }

    public void setDiffQty(float diffQty) {
        this.diffQty = diffQty;
    }
}
