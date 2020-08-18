package com.prolog.eis.service.enums;

public interface OutBoundEnum {


    enum TargetType{
         AGV(1),
         SSX(2);

         private int number;

        TargetType(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }


}
