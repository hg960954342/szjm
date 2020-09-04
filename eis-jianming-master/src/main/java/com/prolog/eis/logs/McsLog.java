package com.prolog.eis.logs;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table("mcs_log")
public class McsLog {

  @Id
  @ApiModelProperty("主键")
  @AutoKey(type = AutoKey.TYPE_IDENTITY)
  private int id;

  @Column("interface_address")
  @ApiModelProperty("接口地址")
  private String interfaceAddress;

  @Column("params")
  @ApiModelProperty("接口参数")
  private String params;

  @Column("error")
  @ApiModelProperty("错误消息")
  private String error;

  @Column("result")
  @ApiModelProperty("接口返回结果")
  private String result;

  @Column("create_time")
  @ApiModelProperty("创建时间")
  private Date createTime;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getInterfaceAddress() {
    return interfaceAddress;
  }

  public void setInterfaceAddress(String interfaceAddress) {
    this.interfaceAddress = interfaceAddress;
  }

  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
