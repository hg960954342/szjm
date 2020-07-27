package com.prolog.eis;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.prolog.framework.authority.core.annotation.EnablePrologEmptySecurityServer;
import com.prolog.framework.microservice.annotation.EnablePrologService;

@SpringBootApplication()
@EnableTransactionManagement
@EnableScheduling
@EnablePrologService(loadBalanced=false)
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

//	@Bean
//	public TaskScheduler taskScheduler() {
//		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
//		taskScheduler.setPoolSize(1);
//		return taskScheduler;
//	}

	public static void main( String[] args )
	{
		SpringApplication.run(Application.class, args);
	}
}
