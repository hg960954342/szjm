package com.prolog.eis.model.wms;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;


import java.util.Date;

@Table("wms_eis_idempotent")
public class WmsEisIdempotent {


    @Column("message_id")
    @ApiModelProperty("幂等数")
    private String messageId;

    @Column("loc_date")
    @ApiModelProperty("当前本地时间")
    private Date locDate;

    @Column("rejson")
    @ApiModelProperty("返回json串数据")
    private String rejson;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Date getLocDate() {
        return locDate;
    }

    public void setLocDate(Date locDate) {
        this.locDate = locDate;
    }

    public String getRejson() {
        return rejson;
    }

    public void setRejson(String rejson) {
        this.rejson = rejson;
    }
}
