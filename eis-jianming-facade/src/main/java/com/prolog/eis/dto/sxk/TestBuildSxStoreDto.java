package com.prolog.eis.dto.sxk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


import java.math.BigDecimal;
import java.util.Objects;

@ApiModel("手动生成库存数据实体")
public class TestBuildSxStoreDto {
    @ApiModelProperty(value = "containerCode",required = true)
    private String containerCode;
    @ApiModelProperty(value = "ownerId",required = true)
    private String ownerId;

    @ApiModelProperty(value = "itemId",required = true)
    private String itemId;


    @ApiModelProperty(value = "lotId",required = true)
    private String lotId;

    @ApiModelProperty(value = "qty",required = true)
    private BigDecimal qty;


    @ApiModelProperty(value = "生成的库存在哪一层",required = true)
    private Integer layer;



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

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }
}
