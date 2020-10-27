package com.prolog.eis.dto.pick;

import java.util.List;

/**
 * @author chengxudong
 * @description
 **/
public class SendMcsPickTaskDto {


    private List<CarryListBean> carryList;

    public List<CarryListBean> getCarryList() {
        return carryList;
    }

    public void setCarryList(List<CarryListBean> carryList) {
        this.carryList = carryList;
    }

    public static class CarryListBean  {
        /**
         * billNo : 1029150001
         * containerNo : 1029150002
         */

        private String billNo;
        private String containerNo;

        public String getBillNo() {
            return billNo;
        }

        public void setBillNo(String billNo) {
            this.billNo = billNo;
        }

        public String getContainerNo() {
            return containerNo;
        }

        public void setContainerNo(String containerNo) {
            this.containerNo = containerNo;
        }
    }
}
