package com.prolog.eis.service.impl.unbound.entity;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("checkout_task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutTask {

    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private String id;
    @Column("container_code")
    @ApiModelProperty("接口地址")
    private String containerCode;
    @Column("bill_no")
    @ApiModelProperty("接口地址")
    private String billNo;
    @Column("state")
    @ApiModelProperty("接口地址")
    private String state;






}
