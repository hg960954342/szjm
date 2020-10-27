package com.prolog.eis.dto.pick;

import java.util.List;

/**
 * @author chengxudong
 * @description
 **/
public class WmsPickResultDto {


    /**
     * data : [{"billno":"KPD0000001","barcode":"12200001","port":"1","porttime":"2020-01-01 12:02:02"}]
     * size : 1
     * messageID : string
     */

    private int size;
    private String messageID;
    private List<DataBean> data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean   {
        /**
         * billno : KPD0000001
         * barcode : 12200001
         * port : 1
         * porttime : 2020-01-01 12:02:02
         */

        private String billno;
        private String barcode;
        private String port;
        private String porttime;

        public String getBillno() {
            return billno;
        }

        public void setBillno(String billno) {
            this.billno = billno;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getPorttime() {
            return porttime;
        }

        public void setPorttime(String porttime) {
            this.porttime = porttime;
        }
    }
}
