package com.prolog.eis;


import com.prolog.framework.authority.core.annotation.EnablePrologEmptySecurityServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication()
@EnableTransactionManagement
@EnableScheduling
@EnablePrologEmptySecurityServer
@MapperScan(value = "com.prolog.eis.dao")
@EnableAsync
public class Application {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(30000);
        httpRequestFactory.setConnectTimeout(30000);
        httpRequestFactory.setReadTimeout(30000);
        return new RestTemplate(httpRequestFactory);
    }



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
