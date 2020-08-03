package com.prolog.eis.model.wms;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table("wms_eis_idempotent")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
