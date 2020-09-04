package com.prolog.eis.dao;

import com.prolog.eis.model.sxk.SxStore;
import com.prolog.eis.model.wms.CheckStock;
import org.apache.ibatis.annotations.*;

public interface SxStockTaskMapper {

    @Results(id="SxStore" , value= {
            @Result(property = "ownerId",  column = "owner_id"),
            @Result(property = "itemId",  column = "item_id"),
            @Result(property = "lotId",  column = "lot_id")
    })
    @Select("select owner_id,item_id,lot_id from sx_store where CONTAINER_NO =#{containerCode}")
    SxStore queryByCode(String containerCode);


    @Update("update sx_store set qty =qty+#{diffQty} where CONTAINER_NO =#{containerCode}")
    void updateQty(@Param("containerCode") String containerCode,@Param("diffQty") float diffQty);
}
