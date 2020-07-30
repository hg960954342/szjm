package com.prolog.eis.dto.sxk;

import io.swagger.annotations.ApiModelProperty;

public class VerticalLocationDto {

	private int id;

    @ApiModelProperty("垂直货位组编号")
    private String groupNo;

    @ApiModelProperty("限重")
    private double limitWeight;

    @ApiModelProperty("货位编号")
    private String storeNo;

    @ApiModelProperty("错误信息")
    private String errorMsg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public double getLimitWeight() {
        return limitWeight;
    }

    public void setLimitWeight(double limitWeight) {
        this.limitWeight = limitWeight;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }
}
