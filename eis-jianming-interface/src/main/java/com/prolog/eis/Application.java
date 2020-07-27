package com.prolog.eis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.prolog.eis.config.DBConfigEis;
import com.prolog.eis.config.DBConfigMiddle;
import com.prolog.framework.authority.core.annotation.EnablePrologEmptySecurityServer;
import com.prolog.framework.microservice.annotation.EnablePrologService;

@SpringBootApplication()
@EnableTransactionManagement
@EnableScheduling
@EnablePrologService(loadBalanced=false)
@EnablePrologEmptySecurityServer
@EnableAsync
@EnableAspectJAutoProxy
@EnableConfigurationProperties(value = {DBConfigEis.class, DBConfigMiddle.class})
public class Application {
	@Bean
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(5000);
		httpRequestFactory.setConnectTimeout(5000);
		httpRequestFactory.setReadTimeout(5000);
		return new RestTemplate(httpRequestFactory);
	}
	
	
	public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
}
