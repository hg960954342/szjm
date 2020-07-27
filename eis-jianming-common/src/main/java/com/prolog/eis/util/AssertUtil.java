package com.prolog.eis.util;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author panteng
 * @description: 断言
 * @date 2020/5/5 10:48
 */
public class AssertUtil {
    /**
     * 断言这个 boolean 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression boolean 值
     * @param message    消息
     */
    public static void isTrue(boolean expression, String message, Object... params) throws Exception {
        if (!expression) {
            throw new Exception(format(message, params));
        }
    }

    /**
     * 断言这个 boolean 为 false
     * <p>为 true 则抛出异常</p>
     *
     * @param expression boolean 值
     * @param message    消息
     */
    public static void isFalse(boolean expression, String message, Object... params) throws Exception {
        isTrue(!expression, message, params);
    }

    /**
     * 断言这个 object 为 null
     * <p>不为 null 则抛异常</p>
     *
     * @param object  对象
     * @param message 消息
     */
    public static void isNull(Object object, String message, Object... params) throws Exception {
        isTrue(object == null, message, params);
    }

    /**
     * 断言这个 object 不为 null
     * <p>为 null 则抛异常</p>
     *
     * @param object  对象
     * @param message 消息
     */
    public static void notNull(Object object, String message, Object... params) throws Exception {
        isTrue(object != null, message, params);
    }

    /**
     * 断言这个 value 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param value   字符串
     * @param message 消息
     */
    public static void notEmpty(String value, String message, Object... params) throws Exception {
        isTrue(StringUtils.isNotEmpty(value), message, params);
    }

    /**
     * 断言这个 collection 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param collection 集合
     * @param message    消息
     */
    public static void notEmpty(Collection<?> collection, String message, Object... params) throws Exception {
        isTrue(CollectionUtils.isNotEmpty(collection), message, params);
    }

    /**
     * 断言这个 map 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param map     集合
     * @param message 消息
     */
    public static void notEmpty(Map<?, ?> map, String message, Object... params) throws Exception {
        isTrue(MapUtils.isEmpty(map), message, params);
    }

    /**
     * 断言这个 数组 不为 empty
     * <p>为 empty 则抛异常</p>
     *
     * @param array   数组
     * @param message 消息
     */
    public static void notEmpty(Object[] array, String message, Object... params) throws Exception {
        isTrue(ArrayUtils.isNotEmpty(array), message, params);
    }

    /**
     * 安全的进行字符串 format
     *
     * @param target 目标字符串
     * @param params format 参数
     * @return format 后的
     */
    public static String format(String target, Object... params) {
        if (target.contains("%s") && ArrayUtils.isNotEmpty(params)) {
            return String.format(target, params);
        }
        return target;
    }
}
