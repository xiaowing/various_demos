springboot-mybatis
=============

###简介

基于Spring-boot技术，搭建了一个RESTful API的Demo。该Demo实现了以下特性:

1. 在Spring-boot中以纯Java Config的形式集成了mybatis(_ORM_) + druid(_DataSource_) + pgJdbc(_JDBC Driver_)
2. 在该Spring-boot应用中启用了**Druid**自带的数据源监控Portal。

###学习点
1. 将原本以xml形式进行配置的mybatis配置项在Spring Boot中用Java Config的形式进行配置
2. 将原本以xml形式进行定义和注册的mybatis的映射关系(mapper)通过注解的形式定义在Spring-boot应用中
3. 将原本需要在tomcat的*web.xml*文件中进行启用的**Druid**监控Portal通过`@Configuration`以及`@Bean`的注解形式注册至Spring-boot内置的Tomcat中，同时利用代码实现原本需要在*web.xml*中`<servlet>`下配置的`init-param`。
4. 对Spring-boot内置的Tomcat的监听端口实施自定义。
5. 运用**Mockito**对mybatis的Mapper实现进行模拟，并以此做成单元测试。

