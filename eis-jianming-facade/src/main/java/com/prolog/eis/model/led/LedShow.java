package com.prolog.eis.model.led;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("led_port")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedShow {

    @Id
    @ApiModelProperty("主键")
    @AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;

    @Column("led_ip")
    @ApiModelProperty("led ip")
    private String ledIp;

    @Column("port")
    @ApiModelProperty("端口")
    private int port;

    @Column("message")
    @ApiModelProperty("led屏信息")
    private String message;
}
