package com.prolog.eis.dto.pick;

import java.util.List;

/**
 * @author chengxudong
 * @description
 **/
public class PushDataDto {


    /**
     * data : [{"billno":"KPD0000001","barcode":"20201023000001","itemid":"00001","Itemname":"草本","qty":100.03,"lotid":"phid00000001","lotno":"20201023","isover":"N"}]
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

    public static class DataBean  {
        /**
         * billno : KPD0000001
         * barcode : 20201023000001
         * itemid : 00001
         * Itemname : 草本
         * qty : 100.03
         * lotid : phid00000001
         * lotno : 20201023
         * isover : N
         */

        private String billNo;
        private String barCode;
        private String itemId;
        private String ItemName;
        private double qty;
        private String lotId;
        private String lotNo;
        private String isOver;


        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public double getQty() {
            return qty;
        }

        public void setQty(double qty) {
            this.qty = qty;
        }

        public String getLotId() {
            return lotId;
        }

        public void setLotId(String lotId) {
            this.lotId = lotId;
        }

        public String getLotNo() {
            return lotNo;
        }

        public void setLotNo(String lotNo) {
            this.lotNo = lotNo;
        }

        public String getIsOver() {
            return isOver;
        }

        public void setIsOver(String isOver) {
            this.isOver = isOver;
        }
    }
}
