package com.prolog.eis.mybatis;

import com.prolog.framework.dao.helper.SqlFactory;
import com.prolog.framework.utils.MapUtils;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dubux
 */
@Component
public class BaseJdbcTemplate {

    @Resource
    JdbcTemplate jdbcTemplate;

    SqlFactory sqlFactory = new SqlFactory();


    public Map<String, Object> findById(Object id, Class classz) {
        Map<String, Object> map = MapUtils.put("c", classz).put("id", id).getMap();
        String sql = sqlFactory.selectById(map);
        sql = StringUtils.replace(sql, String.format("#{%s}", id), (id instanceof String) ? String.format("'%s'", id) :
                id + "");
        return jdbcTemplate.queryForMap(sql);

    }

    public void save(Object obj) {
        String sql = sqlFactory.insert(obj);
        Map<String, Object> andMap = beanToMap(obj);
        sql = sqlBuilder(sql, "#{%s}", andMap);
        jdbcTemplate.update(sql);
    }


    public List<Map<String, Object>> findByMap(Map<String, Object> andMap, Class classz) {
        Map<String, Object> map = MapUtils.put("params", andMap).put("c", classz).getMap();
        String sql = sqlFactory.selectByMap(map);
        sql = sqlBuilder(sql, "#{params.%s}", andMap);
        return jdbcTemplate.queryForList(sql);

    }

    /**
     * 替换SQL中的参数
     *
     * @param sql
     * @param format
     * @param paramMap
     * @return
     */
    private String sqlBuilder(String sql, String format, Map<String, Object> paramMap) {
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            sql = StringUtils.replace(sql, String.format(format, entry.getKey()),
                    (entry.getValue() instanceof String) ? String.format("'%s'", entry.getValue()) :
                            entry.getValue() + "");
        }
        return sql;
    }


    /**
     * 将对象装换为map
     *
     * @param bean
     * @return
     */
    @SneakyThrows
    private Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> map = BeanUtils.describe(bean);
        return map;
    }


}
