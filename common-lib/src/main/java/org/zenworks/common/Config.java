package org.zenworks.common;

import java.util.Map;

public class Config {
	
	public static final Integer INTEGER = new Integer(0);
	public static String STRING = new String();
	public static String[] STRING_ARRAY = new String[]{};
	
	private Map<Class<?>,Map<String,Object>> internalConfiguration;
	
	public Config(Map<Class<?>,Map<String,Object>> configuration) {
		this.internalConfiguration = configuration;
	}
	
	public <T> T getConfig(T type, String key) {
		
		if (internalConfiguration.containsKey(type.getClass())) {
			return (T)internalConfiguration.get(type.getClass()).get(key);	
		} else {
			return null;
		}		
		
	}
	
	public String getStringConfig(String key) {
		return getConfig(STRING, key);		
	}
	
	public String[] getStringArrayConfig(String key) {
		return getConfig(STRING_ARRAY, key);		
	}			

	public <T> boolean isConfig(T type, String key) {
		if (internalConfiguration.containsKey(type.getClass())) {
			return internalConfiguration.get(type.getClass()).containsKey(key);
		}
		return false;
	}
	
	public boolean isStringConfig(String key) {
		return isConfig(STRING, key);
	}
	
	public boolean isStringArrayConfig(String key) {
		return isConfig(STRING_ARRAY, key);
	}	

}
