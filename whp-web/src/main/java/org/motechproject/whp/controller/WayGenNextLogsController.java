package org.motechproject.whp.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value="/wgn")
public class WayGenNextLogsController {
	
	 
	    private Properties diagnosticProperties;
	    private RestTemplate restTemplate;
	    

	    @Autowired
	    public WayGenNextLogsController(RestTemplate restTemplate, Properties diagnosticProperties) {
	    	this.restTemplate = restTemplate;
	    	this.diagnosticProperties = diagnosticProperties;
	    }

	    @RequestMapping(method = RequestMethod.GET, value = "/logs/")
	    @ResponseBody
	    public String getWayGenNextLogs(HttpServletRequest request) {
	    	return restTemplate.getForEntity(getWgnLogsURL(), String.class).getBody();
	    }
	    
	    private String getWgnLogsURL() {
	         return diagnosticProperties.getProperty("link.Internal.WayGenNextLogs");
	    }
	    


}
