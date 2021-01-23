package com.prolog.eis.mybatis;

import com.prolog.framework.dao.custom.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chengxudong
 * @description Mybatis 使用Map结果集 包括Map List<Map>时候 自动把key设置为驼峰
 * 例如lot_id 转换为lotId ;lotid转换为lotId
 * 需要配置yml文件中      configuration:
 *         map-underscore-to-camel-case: true
 *         才生效 否则不会转换
 **/

public class MyBatisMapKeyConvertConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return new ConfigurationCustomizer() {
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.setObjectWrapperFactory(new MapWrapperFactory());
                configuration.getTypeHandlerRegistry().register(MyDateTypeHandler.class);
            }
        };
    }
}
