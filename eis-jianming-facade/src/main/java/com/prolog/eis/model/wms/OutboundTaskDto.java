package com.prolog.eis.model.wms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboundTaskDto {

    private List<OutboundTask> data;

    private int size;

    @JsonProperty("MessageID")
    private String messageId;
}
