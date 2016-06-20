package org.wingsdak.sessmgr.controllers;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wingsdak.sessmgr.beans.Greeting;
import org.wingsdak.sessmgr.beans.ResultVO;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path="/greeting", method=RequestMethod.GET)
    public ResultVO greeting(HttpSession session, @RequestParam(value="name", defaultValue="World") String name) {
        
    	Greeting data = new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    	
    	data.setSessionId(session.getId());
    	Object attribute = session.getAttribute("Attribute");
    	if (attribute == null){
    		session.setAttribute("Attribute", name);
    	}
    	else{
    		data.setAttribute((String)attribute);
    	}
    	Calendar c = Calendar.getInstance();
    	c.setTimeInMillis(session.getLastAccessedTime());
    	data.setLastVisit(c.getTime().toString());
    	
    	ResultVO result = new ResultVO();
    	result.setReturnValue(ResultVO.RESULT_OK);
    	result.setMessage("");
    	result.setData(data);
    	
    	return result;
    }
}
