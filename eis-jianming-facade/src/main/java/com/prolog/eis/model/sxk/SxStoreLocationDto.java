package com.prolog.eis.model.sxk;

import io.swagger.annotations.ApiModelProperty;

public class SxStoreLocationDto {

	@ApiModelProperty("容器编号")
    private String containerNo;		//容器编号

    @ApiModelProperty("货位ID")
    private Integer storeLocationId;		//货位ID

    @ApiModelProperty("库存状态(10：入库中、 20：已上架、 30：出库中、31:待出库、40：移位中)")
    private int storeState;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getStoreLocationId() {
        return storeLocationId;
    }

    public void setStoreLocationId(Integer storeLocationId) {
        this.storeLocationId = storeLocationId;
    }

    public int getStoreState() {
        return storeState;
    }

    public void setStoreState(int storeState) {
        this.storeState = storeState;
    }
}
