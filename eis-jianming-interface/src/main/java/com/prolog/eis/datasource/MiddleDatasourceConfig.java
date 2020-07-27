package com.prolog.eis.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.prolog.eis.config.DBConfigMiddle;
 
@Configuration
// basePackages 最好分开配置 如果放在同一个文件夹可能会报错
@MapperScan(basePackages = "com.prolog.eis.dao.middle", sqlSessionTemplateRef = "test2SqlSessionTemplate")
public class MiddleDatasourceConfig {
 
	// 配置数据源
	@Bean(name = "test2DataSource")
	public DataSource testDataSource(DBConfigMiddle testConfig) throws SQLException {
        //用druidXADataSource方式或者上面的Properties方式都可以
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setUrl(testConfig.getUrl());
        druidXADataSource.setUsername(testConfig.getUsername());
        druidXADataSource.setPassword(testConfig.getPassword());
        
        //Atomikos统一管理分布式事务
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setUniqueResourceName("test2DataSource");
        xaDataSource.setXaDataSource(druidXADataSource);
        xaDataSource.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        return xaDataSource;
	}
 
	@Bean(name = "test2SqlSessionFactory")
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("test2DataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}
 
	@Bean(name = "test2SqlSessionTemplate")
	public SqlSessionTemplate testSqlSessionTemplate(
			@Qualifier("test2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}