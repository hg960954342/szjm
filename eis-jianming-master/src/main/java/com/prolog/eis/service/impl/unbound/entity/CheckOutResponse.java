package com.prolog.eis.service.impl.unbound.entity;

import java.util.List;

public class CheckOutResponse {


    /**
     * data : [{"BILLNO":"PDD0000001","TYPE":"2","details":[{"SEQNO":"1","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600001","QTY":123.08},{"SEQNO":"2","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600002","QTY":12},{"SEQNO":"3","ITEMID":"SPBH0002","LOTID":"1232","CONTAINERCODE":"600004","QTY":123}]}]
     * size : 1
     * MessageID : string
     */

    private int size;
    private String MessageID;
    private List<DataBean> data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessageID() {
        return MessageID;
    }

    public void setMessageID(String MessageID) {
        this.MessageID = MessageID;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * BILLNO : PDD0000001
         * TYPE : 2
         * details : [{"SEQNO":"1","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600001","QTY":123.08},{"SEQNO":"2","ITEMID":"SPBH0001","LOTID":"123","CONTAINERCODE":"600002","QTY":12},{"SEQNO":"3","ITEMID":"SPBH0002","LOTID":"1232","CONTAINERCODE":"600004","QTY":123}]
         */

        private String BILLNO;
        private String TYPE;
        private List<DetailsBean> details;

        public String getBILLNO() {
            return BILLNO;
        }

        public void setBILLNO(String BILLNO) {
            this.BILLNO = BILLNO;
        }

        public String getTYPE() {
            return TYPE;
        }

        public void setTYPE(String TYPE) {
            this.TYPE = TYPE;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * SEQNO : 1
             * ITEMID : SPBH0001
             * LOTID : 123
             * CONTAINERCODE : 600001
             * QTY : 123.08
             */

            private String SEQNO;
            private String ITEMID;
            private String LOTID;
            private String CONTAINERCODE;
            private double QTY;

            public String getSEQNO() {
                return SEQNO;
            }

            public void setSEQNO(String SEQNO) {
                this.SEQNO = SEQNO;
            }

            public String getITEMID() {
                return ITEMID;
            }

            public void setITEMID(String ITEMID) {
                this.ITEMID = ITEMID;
            }

            public String getLOTID() {
                return LOTID;
            }

            public void setLOTID(String LOTID) {
                this.LOTID = LOTID;
            }

            public String getCONTAINERCODE() {
                return CONTAINERCODE;
            }

            public void setCONTAINERCODE(String CONTAINERCODE) {
                this.CONTAINERCODE = CONTAINERCODE;
            }

            public double getQTY() {
                return QTY;
            }

            public void setQTY(double QTY) {
                this.QTY = QTY;
            }
        }
    }
}
