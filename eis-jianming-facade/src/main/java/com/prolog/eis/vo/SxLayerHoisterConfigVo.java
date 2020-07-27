package com.prolog.eis.vo;

import io.swagger.annotations.ApiModelProperty;

public class SxLayerHoisterConfigVo {
	@ApiModelProperty("ID")
    private int id;
    private int layer;
    @ApiModelProperty("侧向值 1内侧 2外侧")
    private int lateral;
    @ApiModelProperty("线体状态(0：无、1：向内、2：向外、3：双向)")
    private int lineState;

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLateral() {
        return lateral;
    }

    public void setLateral(int lateral) {
        this.lateral = lateral;
    }

    public int getLineState() {
        return lineState;
    }

    public void setLineState(int lineState) {
        this.lineState = lineState;
    }
}
