package org.wingsdak.spbtmybatis;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.wingsdak.spbtmybatis.utility.Logger;

@SpringBootApplication(scanBasePackages = {"org.wingsdak.spbtmybatis"})
public class Application extends SpringBootServletInitializer implements ApplicationContextAware {
	/*
	 * 通过实现ApplicationContextAware接口的setApplicationContext()方法，
	 * 使得一个内部成员可以在Spring容器启动时指向Spring上下文，
	 * 使得程序中其他未受Spring容器托管的业务类可以借此获得Spring上下文，
	 * 进而通过ApplicationContext#getBean()方法获得在Spring容器中托管的注入对象。
	 */
	private static ApplicationContext appContext;
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		Logger.info("spbtmybatis application started.");
	}
}
