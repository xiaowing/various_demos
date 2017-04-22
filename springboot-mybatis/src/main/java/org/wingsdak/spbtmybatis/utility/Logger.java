package org.wingsdak.spbtmybatis.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.log4j.LogManager;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.spi.LoggerRepository;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.Proxy;

public class Logger {
    private static org.slf4j.Logger logger;
    private static final String PROXY_LOG_NAME = Logger.class.getName();
    
    static {
        try {
            Enhancer eh = new Enhancer();
            eh.setSuperclass(org.apache.log4j.Logger.class);
            eh.setCallbackType(LogInterceptor.class);
            Class c = eh.createClass();
            Enhancer.registerCallbacks(c, (Callback[]) new LogInterceptor[]{new LogInterceptor()});

            Constructor<org.apache.log4j.Logger> constructor = c.getConstructor(String.class);
            org.apache.log4j.Logger loggerProxy = constructor.newInstance(Logger.class.getName());

            LoggerRepository loggerRepository = LogManager.getLoggerRepository();
            org.apache.log4j.spi.LoggerFactory lf = ReflectionUtil.getFieldValue(loggerRepository, "defaultFactory");
            Object loggerFactoryProxy = Proxy.newProxyInstance(LoggerFactory.class.getClassLoader(), 
            		new Class[]{LoggerFactory.class}, 
            		(InvocationHandler) new NewLoggerHandler(loggerProxy)
            );

            ReflectionUtil.setFieldValue(loggerRepository, "defaultFactory", loggerFactoryProxy);
            logger = org.slf4j.LoggerFactory.getLogger(Logger.class.getName());
            ReflectionUtil.setFieldValue(loggerRepository, "defaultFactory", lf);
        } catch (Exception e) {
            throw new RuntimeException("Failed during initializing Logger", e);
        }
    }

    private static class LogInterceptor implements MethodInterceptor {
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            // only Intercept the log method
            if (objects.length != 4 || !method.getName().equals("log"))
                return methodProxy.invokeSuper(o, objects);
            objects[0] = PROXY_LOG_NAME;
            return methodProxy.invokeSuper(o, objects);
        }
    }

    private static class NewLoggerHandler implements InvocationHandler {
        private final org.apache.log4j.Logger proxyLogger;

        public NewLoggerHandler(org.apache.log4j.Logger proxyLogger) {
            this.proxyLogger = proxyLogger;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return proxyLogger;
        }
    }

    public static void debug(String msg) {
        logger.debug(msg);
    }
    
    public static void debug(String format, Object... arguments){
    	logger.debug(String.format(format, arguments));
    }
    
    public static void info(String msg) {
        logger.info(msg);
    }
    
    public static void info(String format, Object... arguments){
    	logger.info(String.format(format, arguments));
    }
    
    public static void warn(String msg){
    	logger.warn(msg);
    }
    
    public static void error(String msg){
    	logger.error(msg);
    }
    
    public static void error(String format, Object... arguments){
    	logger.error(String.format(format, arguments));
    }
    
    public static void error(Throwable t, String format, Object... arguments) {
    	String msg = String.format(format, arguments);
        logger.error(msg, t);
    }
    
    public static void error(Throwable t){
    	logger.error("", t);
    }
    
    public static void fatal(String msg){
    	logger.error(String.format("!!!FATAL!!!    %s", msg));
    }
    
    public static void fatal(String format, Object... arguments) {
    	String msg = String.format(format, arguments);
    	Logger.fatal(msg);
    }
}
