package com.prolog.eis.service.rcs.ledshow;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
@Component
public class LedShowDto {


    private Integer id;
    private String pickStation;


   private LedShowDto(Integer id,  String pickStation) {
        this.id = id;
        this.pickStation = pickStation;
    }

    public LedShowDto() {
    }


    public Map<String,LedShowDto> getLedShowDtoMap(){
      Map<String,LedShowDto> map=new HashMap<>();
      map.put("057200AB048300",new LedShowDto(4,"一站"));
      map.put("054320AB048300",new LedShowDto(5,"二站"));
      map.put("051440AB047200",new LedShowDto(6,"三站"));
      map.put("050000AB052600",new LedShowDto(6,"四站"));
      return map;


  }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPickStation() {
        return pickStation;
    }

    public void setPickStation(String pickStation) {
        this.pickStation = pickStation;
    }
}
