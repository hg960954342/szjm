package com.prolog.eis.dto.mcs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author panteng
 * @description: MCS设备状态上报
 * @date 2020/5/21 9:58
 */
public class McsEquipStatusDto implements Serializable {
    /**
     * 编号
     */
    private String id;
    /**
     * 编号
     */
    private String name;
    /**
     * 系统编号（PLC1：T01~T04号提升机及输送线状态；PLC2：T05~T06号提升机及输送线状态；PLC3：T21号提升机及输送线状态；）
     */
    private String plcId;
    /**
     * 状态信息（数据格式为byte数组）
     */
    private String content;
    /**
     *生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime;

    public McsEquipStatusDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlcId() {
        return plcId;
    }

    public void setPlcId(String plcId) {
        this.plcId = plcId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
