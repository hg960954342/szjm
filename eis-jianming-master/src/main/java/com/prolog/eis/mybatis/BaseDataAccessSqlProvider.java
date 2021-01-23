package com.prolog.eis.mybatis;

import com.prolog.framework.core.annotation.Ignore;
import com.prolog.framework.core.annotation.One;
import com.prolog.framework.core.restriction.FieldSelector;
import com.prolog.framework.core.util.ClassUtils;
import com.prolog.framework.core.util.TableUtils;
import com.prolog.framework.dao.helper.SqlFactory;
import com.prolog.framework.dao.helper.SqlHelper;
import com.prolog.framework.utils.ArrayUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chengxudong
 * @description 添加selectByExample方法
 **/
public class BaseDataAccessSqlProvider<T> extends SqlFactory<T> {

    public String selectByExample(T example){
        Map<String,Object> map =new HashMap<>();
        List<Field> list = ClassUtils.getFields(example.getClass());
        for(Field field:list){
            Object obj=ClassUtils.getFieldValue(field.getName(),example);
            if(null!=obj){
                map.put(field.getName(),obj);
            }
        }
       return this.selectByMap(map);
    }

    /**
     *
     *根据map查询列表
     *
     * @Title: selectByMap
     * @param map
     * @return String
     * @date 2018年4月13日 下午3:29:21
     */
    public String selectOneByMap(Map<String,Object> map){

        Class<?> c = (Class<?>) map.get("c");
        String tableName = TableUtils.getTableName(c);
        String tableAlias = "tb_"+c.getSimpleName().toLowerCase();

        String asstr = getSelectColumns(c,null,tableAlias,null);
        String mainTableSql = tableName.toLowerCase().equals(tableAlias)?tableAlias:String.format("%s %s", tableName,tableAlias);
        String oneSql=getOneSql(c,tableAlias);

        String pstr=getAndMap(map.get("params"),c,tableAlias);
        //select %s from %s %s where %s
        SqlHelper sqlHelper = SqlHelper.SELECT_BY_MAP;
        String sql = String.format(sqlHelper.getSql(), asstr,mainTableSql,oneSql,pstr);

        return sql;
    }

    /**
     *
     *获取查询字段
     *表名.字段（实体）_字段
     * @Title: getSelectColumns
     * @param c
     * @param fieldSelector
     * @param tableAlias
     * @param columnPrefix
     * @return String
     * @date 2018年5月9日 上午9:18:45
     */
    private String getSelectColumns(Class<?> c,Object fieldSelector,String tableAlias,String columnPrefix){
        FieldSelector condition;
        String cstr="";
        if(fieldSelector==null){
            for(Field f : ClassUtils.getFields(c)){
                //不存在忽略标签
                if(!f.isAnnotationPresent(Ignore.class)){
                    cstr += getSelectColumn(f,(FieldSelector)fieldSelector,tableAlias,columnPrefix)+",";
                }
            }
        }else{
            condition = (FieldSelector)fieldSelector;
            String[] ins = condition.getInclude();
            String[] exs = condition.getExclude();

            for(Field f : ClassUtils.getFields(c)){
                String fname = f.getName();
                if(ins!=null){

                    if(ArrayUtils.isContains(ins,fname)){
                        cstr += getSelectColumn(f,(FieldSelector)fieldSelector,tableAlias,columnPrefix)+",";
                    }

                }else if(ins==null && exs!=null){
                    if(!ArrayUtils.isContains(exs,fname)) {
                        cstr += getSelectColumn(f,(FieldSelector)fieldSelector,tableAlias,columnPrefix)+",";
                    }

                }else if(ins==null && exs==null){
                    cstr += getSelectColumn(f,(FieldSelector)fieldSelector,tableAlias,columnPrefix)+",";
                }
            }
        }
        cstr = cstr.substring(0, cstr.length()-1);
        return cstr;
    }

    private String getSelectColumn(Field f, FieldSelector fieldSelector, String tableAlias, String columnPrefix){

        String al = StringUtils.isEmpty(tableAlias)?"":""+tableAlias+".";
        String pr = StringUtils.isEmpty(columnPrefix)?"":columnPrefix+"_";
        String format = al+"%s as %s";

        if(f.isAnnotationPresent(One.class)){
            String alias0 = f.getName();
            //select 语句
            return getSelectColumns(f.getType(),null,alias0,alias0);
        }else {
            return String.format(format,TableUtils.getColumnName(f),f.getName());
        }
    }

    /**
     *
     *获取@one的sql语句
     *
     * @Title: getOneSql
     * @param c
     * @param mainTableAlias
     * @return String
     * @date 2018年5月9日 上午10:05:27
     */
    private String getOneSql(Class<?> c,String mainTableAlias){
        //mainTableAlias不能为空
        String leftJoinFormatter = "left join %s %s on %s=%s ";
        String leftJoinSql="";
        for(Field f:ClassUtils.getFields(c)){
            if(f.isAnnotationPresent(One.class)){
                String tn1 = TableUtils.getTableName(f.getType());
                String alias =  f.getName();
                String tn1_cln = mainTableAlias+"."+TableUtils.getColumnName(f);
                String property = (String) ClassUtils.getAnnotationValue(f, One.class, "property");
                String targetColumn =  TableUtils.getColumnName(property, f.getType());
                leftJoinSql += String.format(leftJoinFormatter, tn1,alias,tn1_cln,alias+"."+targetColumn);
            }
        }
        return leftJoinSql;
    }
    /**
     *
     *组装map为条件语句
     *
     * @Title: getAndMap
     * @param map
     * @param c
     * @return String
     * @date 2018年4月13日 下午3:50:55
     */
    @SuppressWarnings("unchecked")
    private String getAndMap(Object map,Class<?> c,String mainTableAlias){
        String alias = StringUtils.isEmpty(mainTableAlias)?"":mainTableAlias+".";
        Map<String,Object> mymap;
        if(map==null) {
            return "1=1";
        }
        mymap = (Map<String,Object>)map;
        String pstr="1=1 and ";
        Iterator<String> keys = mymap.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            try {
                Field f = c.getDeclaredField(key);
                pstr += alias+ TableUtils.getColumnName(f)+" = #{params."+key+"} and ";
            } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
        }
        pstr = pstr.trim();
        pstr = pstr.endsWith("and")?pstr.substring(0, pstr.length()-3):pstr;

        return pstr;
    }
}
