package com.prolog.eis;

import com.prolog.eis.filter.UrlFilter;
import com.prolog.framework.authority.core.annotation.EnablePrologEmptySecurityServer;
import com.prolog.framework.microservice.annotation.EnablePrologService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnablePrologService(loadBalanced=false)
@EnablePrologEmptySecurityServer
@MapperScan("com.prolog.eis.dao")
@EnableAsync
@EnableAspectJAutoProxy
public class Master {
	@Bean
	public FilterRegistrationBean registFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new UrlFilter());
		registration.addUrlPatterns("/*");
		registration.setName("UrlFilter");
		registration.setOrder(1);
		return registration;
	}
	@Bean
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(60000);
		httpRequestFactory.setReadTimeout(60000);
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8)); // 支持中文编码
		return restTemplate;
	}
	
	
	public static void main( String[] args )
    {
    	SpringApplication.run(Master.class, args);
    }
}
