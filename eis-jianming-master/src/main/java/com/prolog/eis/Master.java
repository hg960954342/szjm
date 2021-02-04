package com.prolog.eis;

import com.prolog.framework.authority.core.annotation.EnablePrologEmptySecurityServer;
import onbon.bx05.Bx5GEnv;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnablePrologEmptySecurityServer
@MapperScan("com.prolog.eis.dao")
@EnableAsync
@EnableAspectJAutoProxy
//@EnablePrologService(loadBalanced=false)
public class Master {


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("EIS RESTful APIs")
                        .description("接口文档")
                        .termsOfServiceUrl("http://localhost:20203/")
                        .contact("EIS")
                        .version("1.0")
                        .build())
                 .select()
                .apis(RequestHandlerSelectors.basePackage("com.prolog.eis"))
                .paths(PathSelectors.any())
                .build();
    }




    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(60000);
        httpRequestFactory.setReadTimeout(60000);
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
        return restTemplate;
    }


    public static void main(String[] args) throws Exception {

        //LED初始化API,此操作只在程序启动时候执行一次即可，多次执行会出现内存错误
        Bx5GEnv.initial();
        SpringApplication.run(Master.class, args);
    }
}
