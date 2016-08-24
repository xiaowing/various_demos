package org.wingsdak.spbtmybatis.conf;

import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.druid.support.http.StatViewServlet;

@Configuration
@Import({EmbeddedServletContainerAutoConfiguration.class })
public class ServletConfig {
	@Bean
	public StatViewServlet dispatchDruidServlet() {
		return new StatViewServlet();
	}
	
	@Bean
	public ServletRegistrationBean registerServlet(){
		return new ServletRegistrationBean(dispatchDruidServlet(), "/druid/*");
	}
}
