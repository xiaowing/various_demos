package org.wingsdak.spbtmybatis.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class GlobalConfig {
    /*
     * 通过将PropertySourcesPlaceholderConfigurer注入使得使用@Value注解且使用"${}"占位符的代码
     * 可以在Spring上下文启动时获取所需的properties文件上的属性值
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
