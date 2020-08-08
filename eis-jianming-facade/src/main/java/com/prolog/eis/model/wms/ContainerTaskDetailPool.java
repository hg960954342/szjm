package com.prolog.eis.model.wms;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.sql.Date;

@Table("outbound_task_detail_pool")
public class ContainerTaskDetailPool {



    @Column("ID")
    @ApiModelProperty("主键")
    private Integer id;




    @Column("bill_no")
    @ApiModelProperty("出库单号")
    private String 	billNo ;


    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime ;

    @Column("end_time")
    @ApiModelProperty("创建时间")
    private Date 	endTime ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
