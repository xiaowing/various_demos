package org.wingsdak.spbtmybatis.conf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.druid.support.http.StatViewServlet;

@Configuration
@Import({EmbeddedServletContainerAutoConfiguration.class })
public class ServletConfig {
	
	// 向内置容器定义Servlet
	@Bean
	public StatViewServlet dispatchDruidServlet() {
		return new StatViewServlet();
	}
	
	// 向内置容器注册上述Servlet以及该Servlet对应的url Mapping以及该Servlet初始化时的参数
	@Bean
	public ServletRegistrationBean registerServlet(){
		ServletRegistrationBean statViewServletRegistrationBean = 
				new ServletRegistrationBean(dispatchDruidServlet(), "/druid/*");
		statViewServletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_ALLOW,  "localhost/127.0.0.1");
		statViewServletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME, "admin");
		statViewServletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD, "password");
		return statViewServletRegistrationBean;
	}
}
