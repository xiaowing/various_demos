package org.wingsdak.sessmgr.conf;

import org.apache.catalina.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.stereotype.Component;
import org.wingsdak.sessmgr.redissession.RedisSessionManager;

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
