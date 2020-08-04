package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 回告mws 重发表 实体类
 */
@Table("repeat_report")
public class RepeatReport {
    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("report_data")
    @ApiModelProperty("回告数据")
    private String reportData;

    @Column("report_type")
    @ApiModelProperty("回告类型")
    private int reportType;

    @Column("report_url")
    @ApiModelProperty("回告地址")
    private String reportUrl;

    @Column("message")
    @ApiModelProperty("返回信息")
    private String message;

    @Column("report_count")
    @ApiModelProperty("回告次数")
    private int reportCount;

    @Column("report_state")
    @ApiModelProperty("回告状态")
    private int reportState;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @Column("send_time")
    @ApiModelProperty("发送时间")
    private Date sendTime;

    @Column("end_time")
    @ApiModelProperty("结束")
    private Date endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReportData() {
        return reportData;
    }

    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }


    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getReportState() {
        return reportState;
    }

    public void setReportState(int reportState) {
        this.reportState = reportState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "RepeatReport{" +
                "id=" + id +
                ", reportData='" + reportData + '\'' +
                ", reportType=" + reportType +
                ", reportUrl='" + reportUrl + '\'' +
                ", message='" + message + '\'' +
                ", reportCount=" + reportCount +
                ", reportState=" + reportState +
                ", createTime=" + createTime +
                ", sendTime=" + sendTime +
                ", endTime=" + endTime +
                '}';
    }
}
