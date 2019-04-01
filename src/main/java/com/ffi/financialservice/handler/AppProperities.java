package com.ffi.financialservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class AppProperities {
	
	@Autowired
	private Environment env;

	public String getPropertyValue(String configKey) {
		return env.getProperty(configKey);
	}
}
