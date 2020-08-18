package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("pick_station")
public class PickStation {

  @Column("id")
  @ApiModelProperty("主键")
  private long id;
  @Column("station_no")
  @ApiModelProperty("拣选站编号")
  private String stationNo;
  @Column("auto_supply")
  private long autoSupply;
  @Column("io")
  private long io;
  @Column("task_type")
  private long taskType;
  @Column("islock")
  private String isLock;
  @Column("remarks")
  private String remarks;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStationNo() {
    return stationNo;
  }

  public void setStationNo(String stationNo) {
    this.stationNo = stationNo;
  }

  public long getAutoSupply() {
    return autoSupply;
  }

  public void setAutoSupply(long autoSupply) {
    this.autoSupply = autoSupply;
  }


  public long getIo() {
    return io;
  }

  public void setIo(long io) {
    this.io = io;
  }


  public long getTaskType() {
    return taskType;
  }

  public void setTaskType(long taskType) {
    this.taskType = taskType;
  }


  public String getIsLock() {
    return isLock;
  }

  public void setIsLock(String isLock) {
    this.isLock = isLock;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

}
