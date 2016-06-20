redisession
=============

###简介
基于Spring-boot技术，搭建了一个RESTful API。
其中，该Demo中操纵的Session是用tomcat-redis-session-manager这个第三方库将实际的Session数据都存在了Redis中，
而不是Tomcat服务器的内存里。

###学习点
1. 原有的tomcat-redis-session-manager第三方库的更新
   - 构建系统从Gradle迁移至Maven
   - 升级原有代码中涉及tomcat, jedis的部分API
2. 对Spring boot自带的内置Tomcat通过代码进行自定义
   - 原有的tomcat-redis-session-manager在使用时必须通过Tomcat的 context.xml 配置文件进行导入。但是Spring boot内置Tomcat的意图就是做到"开箱即用"，减少对xml的依赖。因此通过代码对内置Tomcat的行为进行定制。


###代码摘录

下述代码显示了如何在Spring boot启动时对内置Tomcat设置自定义Valve。
```java
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
```

下述代码显示了如何在Spring boot导入自定义的SessionManager。
```java
    @Component
    public class ServletContainerCustomizer implements TomcatContextCustomizer {

        @Override
        public void customize(Context context) {
            // TODO Auto-generated method stub
            RedisSessionManager manager = new RedisSessionManager();
            
            // Setting the backend redis configuration. 
            manager.setHost("10.0.1.110");
            manager.setPort(6379);
            manager.setDatabase(0);
            
            context.setManager(manager);
        }
    }
```

###补足
package org.wingsdak.sessmgr.redissession 下的所有代码，衍生自github的下述开源项目

[jcoleman/tomcat-redis-session-manager](https://github.com/jcoleman/tomcat-redis-session-manager)

该项目是基于以下许可证发布：
```
Copyright (c) 2011-2014 James Coleman

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```