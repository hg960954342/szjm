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

@Table("sys_log_business")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysBusinessLog {

  @Id
  @ApiModelProperty("主键")
  @AutoKey(type = AutoKey.TYPE_IDENTITY)
  private int id;

  @Column("class_name")
  @ApiModelProperty("代码类")
  private String className;

  @Column("class_simple_name")
  @ApiModelProperty("调用类简名")
  private String classSimpleName;

  @Column("class_method")
  @ApiModelProperty("调用方法")
  private String classMethod;

  @Column("line_number")
  @ApiModelProperty("调用的行数")
  private String lineNumber;

  @Column("error")
  @ApiModelProperty("错误消息")
  private String error;


  @Column("create_time")
  @ApiModelProperty("创建时间")
  private Date createTime;


}
