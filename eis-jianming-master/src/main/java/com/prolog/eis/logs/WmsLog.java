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

@Table("wms_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmsLog {

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


}
