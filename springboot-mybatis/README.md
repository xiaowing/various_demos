springboot-mybatis
=============

简介
----

基于Spring-boot技术，搭建了一个RESTful API的Demo。该Demo实现了以下特性:

1. 在Spring-boot中以纯Java Config的形式集成了mybatis(_ORM_) + druid(_DataSource_) + pgJdbc(_JDBC Driver_)
2. 在该Spring-boot应用中启用了**Druid**自带的数据源监控Portal。

学习点
----

1. 将原本以xml形式进行配置的mybatis配置项在Spring Boot中用Java Config的形式进行配置
2. 将原本以xml形式进行定义和注册的mybatis的映射关系(mapper)通过注解的形式定义在Spring-boot应用中
3. 将原本需要在tomcat的*web.xml*文件中进行启用的**Druid**监控Portal通过`@Configuration`以及`@Bean`的注解形式注册至Spring-boot内置的Tomcat中，同时利用代码实现原本需要在*web.xml*中`<servlet>`下配置的`init-param`。
4. 对Spring-boot内置的Tomcat的监听端口实施自定义。
5. 运用**Mockito**对mybatis的Mapper实现进行模拟，并以此做成单元测试。
6. 在该Spring Boot项目中引入properties文件，使得一些行为参数可配置化(比如数据源配置，日志配置，内置Tomcat的监听端口号配置等)
7. 在该Spring Boot项目中集成 log4j，并通过反射与代理机制对`org.slf4j.Logger`实现封装，从而解决直接使用log4j的API时存在的以下问题：
    * 如果在业务逻辑中直接使用org.slf4j.Logger，则需要在每一个意图打日志的类中引入Logger对象
    * 如果单纯只是简单复用`org.slf4j.Logger`而进行封装时又会导致打日志的位置信息不准确(位置信息全变成了封装类)
8. 基于Spring Boot的Executable jar/war包的启动的概要流程如下：
    1. 初始化Spring Application Context
    2. 实例化所有注入的Bean(包括自动Scan的以及显式注入的)
    3. 将指向ApplicationContext的指针赋值给所有实现了`ApplicationContextAware`接口的Bean.
    4. 将代码控制权交给用户代码以完成用户逻辑所需的一些初始化(即 jar模式下的`main()`函数)  

