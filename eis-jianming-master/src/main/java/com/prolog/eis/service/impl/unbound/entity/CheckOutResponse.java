package com.prolog.eis.service.impl.unbound.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class CheckOutResponse {


    /**
     * data : [{"BILLNO":"PDD0000001","TYPE":"2","details":[{"SEQNO":"1","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600001","QTY":123.08},{"SEQNO":"2","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600002","QTY":12},{"SEQNO":"3","ITEMID":"SPBH0002","LOTID":"1232","CONTAINERCODE":"600004","QTY":123}]}]
     * size : 1
     * MessageID : string
     */

    private int size;
    @JSONField(name="MessageID")
    private String messageID;
    private List<DataBean> data;



    @Setter
    @Getter
    public static class DataBean {
        /**
         * BILLNO : PDD0000001
         * TYPE : 2
         * details : [{"SEQNO":"1","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600001","QTY":123.08},{"SEQNO":"2","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600002","QTY":12},{"SEQNO":"3","ITEMID":"SPBH0002","LOTID":"1232","CONTAINERCODE":"600004","QTY":123}]
         */

        @JSONField(name="BILLNO")
        private String billNo;
        @JSONField(name="TYPE")
        private String type;
        private List<DetailsBean> details;

        @Setter
        @Getter
        public static class DetailsBean {
            /**
             * SEQNO : 1
             * ITEMID : SPBH0001
             * LOTID : 123
             * CONTAINERCODE : 600001
             * QTY : 123.08
             */

            @JSONField(name="SEQNO")
            private String seqNo;
            @JSONField(name="ITEMID")
            private String itemId;
            @JSONField(name="LOTID")
            private String lotId;
            @JSONField(name="CONTAINERCODE")
            private String containerCode;
            @JSONField(name="QTY")
            private double qty;
        }
    }
}
