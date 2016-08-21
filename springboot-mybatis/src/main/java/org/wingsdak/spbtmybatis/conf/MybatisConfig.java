package org.wingsdak.spbtmybatis.conf;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.wingsdak.spbtmybatis.persistence.PgDatabaseMapper;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@MapperScan("org.wingsdak.spbtmybatis.persistence")
public class MybatisConfig {
	@Bean
	public DataSource dataSource(){
		/*
		 * 配置DataSource的Bean
		 */
		DruidDataSource ds = new DruidDataSource();
		ds.setDriverClassName("org.postgresql.Driver");
		ds.setUrl("jdbc:postgresql://10.0.1.200:20480/playdb");
		ds.setUsername("xiaowing");
		ds.setPassword("asdf1234");
		try {
			ds.setFilters("stat");
		} catch (SQLException e) {
			// Skip
			;;
		}
		ds.setInitialSize(1);
		ds.setMinIdle(8);
		ds.setMaxActive(32);
		ds.setMaxWait(20000);
		ds.setTimeBetweenEvictionRunsMillis(60000);
		ds.setMinEvictableIdleTimeMillis(60000);
		return ds;
	}
	
    @Bean
    public DataSourceTransactionManager transactionManager() {
		/*
		 * 配置TransactionManager的Bean
		 * mybatis-spring中必须注入DataSourceTransactionManager
		 */
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
		/*
		 * 配置SqlSessionFactory的Bean
		 */
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        //sessionFactory.setTypeAliasesPackage("org.wingsdak.ftptester.persistence");
        SqlSessionFactory factory = sessionFactory.getObject();
        
        /* 
         * 注册Mapper。
         * 如果不注册Mapper，则会在Spring-Boot启动时报"Type interface XXXMapper is not known to the MapperRegistry"
         */
        factory.getConfiguration().addMapper(PgDatabaseMapper.class);
        
        return factory;
    }
}
