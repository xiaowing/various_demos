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
		/* 
		 * According to the definition of StatViewServlet, the white-list of druid StatViewServlet
		 * can be specifed with the format as "xxx.xxx.xxx.xxx/yyy.yyy.yyy.yyy" to limit the access to 
		 * the StatViewServlet.  
		 */
		statViewServletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_ALLOW,  "127.0.0.1");
		statViewServletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME, "admin");
		statViewServletRegistrationBean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD, "password");
		return statViewServletRegistrationBean;
	}
}
