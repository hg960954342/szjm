package com.prolog.eis.util;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
    /**
     * 获取Bean中value存在的Map
     * @param bean
     * @return
     */
    public static Map<String,Object> describe(Object bean){
        {

            if (bean == null) {
                throw new IllegalArgumentException("No bean specified");
            }
            PropertyUtilsBean  propertyUtilsBean= BeanUtilsBean.getInstance().getPropertyUtils();
            Map description = new HashMap();
            if (bean instanceof DynaBean) {

                DynaProperty[] descriptors =
                        ((DynaBean) bean).getDynaClass().getDynaProperties();
                for (int i = 0; i < descriptors.length; i++) {

                    String name = descriptors[i].getName();
                    try {
                        Object value=propertyUtilsBean.getProperty(bean, name);
                        if(null!=value) {
                            description.put(name, propertyUtilsBean.getProperty(bean, name));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            } else {
                PropertyDescriptor[] descriptors =
                        propertyUtilsBean.getPropertyDescriptors(bean);
                for (int i = 0; i < descriptors.length; i++) {
                    String name = descriptors[i].getName();

                    if (descriptors[i].getReadMethod() != null&&!name.endsWith("class")) {
                        try {
                            Object value=propertyUtilsBean.getProperty(bean, name);
                            if(null!=value){
                                description.put(name, propertyUtilsBean.getProperty(bean, name));
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            return (description);

        }
    }



}
