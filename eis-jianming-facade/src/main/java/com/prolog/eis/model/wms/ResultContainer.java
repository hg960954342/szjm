package com.prolog.eis.model.wms;

import java.util.List;

public class ResultContainer {


    /**
     * data : [{"billno":"PDD0000001","tasktype":"2","details":[{"seqno":"1","itemid":"SPBH0001","lotid":"123","containercode":"600001","qty":123.08},{"seqno":"3","itemid":"SPBH0002","lotid":"1232","containercode":"600004","qty":123}]}]
     * size : 1
     *
     * messageID : string
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

    public void setMessageID(String messageID) {
        MessageID = messageID;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * billno : PDD0000001
         * tasktype : 2
         * details : [{"seqno":"1","itemid":"SPBH0001","lotid":"123","containercode":"600001","qty":123.08},{"seqno":"3","itemid":"SPBH0002","lotid":"1232","containercode":"600004","qty":123}]
         */

        private String billno;
        private String type;
        private List<DetailsBean> details;

        public String getBillno() {
            return billno;
        }

        public void setBillno(String billno) {
            this.billno = billno;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

        public static class DetailsBean {
            /**
             * seqno : 1
             * itemid : SPBH0001
             * lotid : 123
             * containercode : 600001
             * qty : 123.08
             */

            private String seqno;
            private String itemid;
            private String lotid;
            private String containercode;
            private double qty;

            public String getSeqno() {
                return seqno;
            }

            public void setSeqno(String seqno) {
                this.seqno = seqno;
            }

            public String getItemid() {
                return itemid;
            }

            public void setItemid(String itemid) {
                this.itemid = itemid;
            }

            public String getLotid() {
                return lotid;
            }

            public void setLotid(String lotid) {
                this.lotid = lotid;
            }

            public String getContainercode() {
                return containercode;
            }

            public void setContainercode(String containercode) {
                this.containercode = containercode;
            }

            public double getQty() {
                return qty;
            }

            public void setQty(double qty) {
                this.qty = qty;
            }
        }
    }
}
