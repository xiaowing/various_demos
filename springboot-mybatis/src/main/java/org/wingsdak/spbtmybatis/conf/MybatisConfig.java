package org.wingsdak.spbtmybatis.conf;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.wingsdak.spbtmybatis.persistence.PgDatabaseMapper;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@MapperScan("org.wingsdak.spbtmybatis.persistence")
@PropertySource("classpath:db.properties") //指明从db.properties文件中获取属性值
public class MybatisConfig {
	@Bean
	public DataSource dataSource(@Value("${datasource.url}") String url, 
			@Value("${datasource.user}") String user,
			@Value("${datasource.password}") String pwd,
			@Value("${datasource.initialSize}") int initialSize,
			@Value("${datasource.minIdle}") int minIdle,
			@Value("${datasource.maxActive}") int maxActive,
			@Value("${datasource.maxWait}") int maxWait,
			@Value("${datasource.timeBetweenEvictionRunsMillis}") int timeBetweenEvictionRunsMillis,
			@Value("${datasource.minEvictableIdleTimeMillis}") int minEvictableIdleTimeMillis){
		/*
		 * 配置DataSource的Bean
		 */
//		DruidDataSource ds = new DruidDataSource();
//		ds.setDriverClassName("org.postgresql.Driver");
//		ds.setUrl("jdbc:postgresql://10.0.1.200:20480/playdb");
//		ds.setUsername("xiaowing");
//		ds.setPassword("asdf1234");
//		try {
//			ds.setFilters("stat");
//		} catch (SQLException e) {
//			// Skip
//			;;
//		}
//		ds.setInitialSize(1);
//		ds.setMinIdle(8);
//		ds.setMaxActive(32);
//		ds.setMaxWait(20000);
//		ds.setTimeBetweenEvictionRunsMillis(60000);
//		ds.setMinEvictableIdleTimeMillis(60000);
//		return ds;
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(pwd);
		try {
			ds.setFilters("stat");
		} catch (SQLException e) {
			// Skip
			;;
		}
		ds.setInitialSize(initialSize);
		ds.setMinIdle(minIdle);
		ds.setMaxActive(maxActive);
		ds.setMaxWait(maxWait);
		ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		return ds;
	}
	
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource ds) {
		/*
		 * 配置TransactionManager的Bean
		 * mybatis-spring中必须注入DataSourceTransactionManager
		 */
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception {
		/*
		 * 配置SqlSessionFactory的Bean
		 */
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        SqlSessionFactory factory = sessionFactory.getObject();
        
        /* 
         * 注册Mapper。
         * 如果不注册Mapper，则会在Spring-Boot启动时报
         * "Type interface XXXMapper is not known to the MapperRegistry"
         */
        factory.getConfiguration().addMapper(PgDatabaseMapper.class);
        
        return factory;
    }
}
