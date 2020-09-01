package com.prolog.eis.model.led;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("led_port")
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLedIp() {
        return ledIp;
    }

    public void setLedIp(String ledIp) {
        this.ledIp = ledIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
