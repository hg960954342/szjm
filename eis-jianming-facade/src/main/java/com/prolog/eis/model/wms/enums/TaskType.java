package com.prolog.eis.model.wms.enums;


/**
 * 任务类型
 */
public interface TaskType {





      enum IN
  {




  };




    enum OUT
  {


     orderBound(1);







     private int bound;

    public int getBound() {
      return bound;
    }

    public void setBound(int bound) {
      this.bound = bound;
    }


    OUT(int bound) {
      this.bound = bound;
    }
  }


}
