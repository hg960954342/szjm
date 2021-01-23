package com.prolog.eis.mybatis;

import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * @author chengxudong
 * @description  根据实体查询
 **/
public interface EisBaseMapper<T> extends BaseMapper<T> {

    /**
     *  添加selectByExample方法
     * @param example
     * @description
     * @return
     */
    @SelectProvider(type = BaseDataAccessSqlProvider.class, method= SqlExtMethod.SELECT_BY_EXAMPLE)
    List<T> selectByExample(T example);
    /**
     *
     *根据map查询单条数据
     *
     * @Title: findByMap
     * @param c
     * @param andMap 可为null
     * @return List<T>
     * @date 2018年4月13日 下午3:25:36
     */
    @SelectProvider(method= SqlExtMethod.SELECTONE_BY_MAP,type= BaseDataAccessSqlProvider.class)
    T findFirstByMap(@Param("params") Map<String,Object> andMap, @Param("c")Class<T> c);
}
