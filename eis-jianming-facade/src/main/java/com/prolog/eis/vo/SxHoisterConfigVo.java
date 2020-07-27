package com.prolog.eis.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class SxHoisterConfigVo {

	@ApiModelProperty("提升机ID")
    private int sxHoisterId;

    @ApiModelProperty("提升机编号")
    private String hoisterNo;
    @ApiModelProperty("内侧层集合")
    private List<SxLayerHoisterConfigVo> inLayerList;

    @ApiModelProperty("外侧层集合")
    private List<SxLayerHoisterConfigVo> outLayerList;

    public int getSxHoisterId() {
        return sxHoisterId;
    }

    public void setSxHoisterId(int sxHoisterId) {
        this.sxHoisterId = sxHoisterId;
    }

    public String getHoisterNo() {
        return hoisterNo;
    }

    public void setHoisterNo(String hoisterNo) {
        this.hoisterNo = hoisterNo;
    }

    public List<SxLayerHoisterConfigVo> getInLayerList() {
        return inLayerList;
    }

    public void setInLayerList(List<SxLayerHoisterConfigVo> inLayerList) {
        this.inLayerList = inLayerList;
    }

    public List<SxLayerHoisterConfigVo> getOutLayerList() {
        return outLayerList;
    }

    public void setOutLayerList(List<SxLayerHoisterConfigVo> outLayerList) {
        this.outLayerList = outLayerList;
    }
}
