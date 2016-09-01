package org.wingsdak.spbtmybatis.conf;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedTomcatConfig {

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatFactory() {
        return new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
                    Tomcat tomcat) {
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
                    TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory = 
                    		(TomcatEmbeddedServletContainerFactory) container;
                    // Set the port of embeded tomcat as 28080 for default. 
                    tomcatEmbeddedServletContainerFactory.setPort(28080);
                }
            }
        };
    }
}
