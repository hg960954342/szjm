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

@Table("eis_log_interface")
public class EisInterfaceLog {

  @Id
  @ApiModelProperty("主键")
  @AutoKey(type = AutoKey.TYPE_IDENTITY)
  private int id;

  @Column("url")
  @ApiModelProperty("Eis系统接口地址")
  private String url;

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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
