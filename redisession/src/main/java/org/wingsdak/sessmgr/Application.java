package org.wingsdak.sessmgr;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.wingsdak.sessmgr.conf.ServletContainerCustomizer;
import org.wingsdak.sessmgr.redissession.RedisSessionHandlerValve;


//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
//@ImportResource("classpath:applicationContext.xml")
public class Application {

    @SuppressWarnings("deprecation")
	public static void main(String[] args) {
        // default rookie code.
    	//SpringApplication.run(Application.class, args);
    	
    	SpringApplication app = new SpringApplication(Application.class);
    	app.run(args);
    	
    }
    

    
    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatFactory() {
        return new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
                    Tomcat tomcat) {
                //tomcat.enableNaming();
            	System.out.println("Enterring TomcatEmbeddedServletContainerFactory.getTomcatEmbeddedServletContainer()...");
                return super.getTomcatEmbeddedServletContainer(tomcat);
            }
        };
    }
    
    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                if (container instanceof TomcatEmbeddedServletContainerFactory) {
                    TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = (TomcatEmbeddedServletContainerFactory) container;
                    
                    
                    RedisSessionHandlerValve valve = new RedisSessionHandlerValve();
                    List<RedisSessionHandlerValve> valves = 
                    		new ArrayList<RedisSessionHandlerValve>();
                    valves.add(0, valve);
                    tomcatEmbeddedServletContainerFactory.setContextValves(valves);
                    tomcatEmbeddedServletContainerFactory.setPort(28080);    // Set the port of embeded tomcat as 28080 for default. 
     
                    System.out.println("Enterring EmbeddedServletContainerCustomizer.customize()...");
                    tomcatEmbeddedServletContainerFactory.addContextCustomizers(
                    		new ServletContainerCustomizer());
                }
            }
        };
    }
}

