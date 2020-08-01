package com.prolog.eis.model.wms;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WmsEisIdempotent {


    private String messageId;


    private Date locDate;


    private String rejson;
}
