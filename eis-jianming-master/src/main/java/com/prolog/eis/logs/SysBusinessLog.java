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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getClassSimpleName() {
    return classSimpleName;
  }

  public void setClassSimpleName(String classSimpleName) {
    this.classSimpleName = classSimpleName;
  }

  public String getClassMethod() {
    return classMethod;
  }

  public void setClassMethod(String classMethod) {
    this.classMethod = classMethod;
  }

  public String getLineNumber() {
    return lineNumber;
  }

  public void setLineNumber(String lineNumber) {
    this.lineNumber = lineNumber;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
