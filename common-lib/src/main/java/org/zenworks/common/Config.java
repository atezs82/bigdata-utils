package org.zenworks.common;

import java.util.Map;

public class Config {
	
	private Map<Class<?>,Map<String,Object>> internalConfiguration;
	
	public Config(Map<Class<?>,Map<String,Object>> configuration) {
		this.internalConfiguration = configuration;
	}
	
	public <T> T getConfig(T type, String key) {
		return (T)internalConfiguration.get(type).get(key);
	}
	
	

}
