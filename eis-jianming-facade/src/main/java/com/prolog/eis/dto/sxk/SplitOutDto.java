package com.prolog.eis.dto.sxk;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author panteng
 * @description: 拆盘机 托盘剁dto
 * @date 2020/2/15 15:08
 */
public class SplitOutDto {

	@ApiModelProperty("四向库库存表")
    private int id;

    @ApiModelProperty("容器编号")
    private String containerNo;

    @ApiModelProperty("货位ID")
    private int storeLocationId;

    @ApiModelProperty("X")
    private int x;

    @ApiModelProperty("层")
    private int layer;

    @ApiModelProperty("Y")
    private int y;

    @ApiModelProperty("预留货位")
    private int reservedLocation;

    @ApiModelProperty("距离")
    private int distance;


    public int getReservedLocation() {
        return reservedLocation;
    }

    public void setReservedLocation(int reservedLocation) {
        this.reservedLocation = reservedLocation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public int getStoreLocationId() {
        return storeLocationId;
    }

    public void setStoreLocationId(int storeLocationId) {
        this.storeLocationId = storeLocationId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
