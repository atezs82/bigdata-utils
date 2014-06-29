package org.zenworks.common;

import java.util.HashMap;
import java.util.Map;

public class ConfigFactory {
	
	final String DEFAULT_NAME_VALUE_SEPARATOR="=";
	final String DEFAULT_MULTIPLE_VALUE_SEPARATOR = ";";
	
	private Map<Class<?>,Map<String,Object>> configuration;
	
	public ConfigFactory() {
		configuration = new HashMap<Class<?>,Map<String,Object>>();
		addClassIfNotExistent(String.class);
		addClassIfNotExistent(String[].class);
		addClassIfNotExistent(Integer.class);
	}
	
	public void addParameter(final String parameterNameAndValue) {
		final String key = parameterNameAndValue.split(DEFAULT_NAME_VALUE_SEPARATOR)[0];
		final String value = parameterNameAndValue.split(DEFAULT_NAME_VALUE_SEPARATOR)[1];
		
		if (value.contains(DEFAULT_MULTIPLE_VALUE_SEPARATOR)) {
		   addStringArrayParameter(key, value.split(DEFAULT_MULTIPLE_VALUE_SEPARATOR));
		} else if (isInteger(value)) {
			addIntegerParameter(key, Integer.valueOf(value));
		} else {
			addStringParameter(key, value);
		}
		
	}
	
	private void addIntegerParameter(String key, Integer valueOf) {
		configuration.get(Integer.class).put(key, valueOf);
	}

	public void addStringParameter(final String key, final String value) {
	   configuration.get(String.class).put(key,value);
	}
	
	public void addStringArrayParameter(final String key, final String... valueArray) {	   
	   configuration.get(String[].class).put(key, valueArray);
	}
	
	public Config createConfig() {
	   return new Config(configuration);		
	}	
	
	private void addClassIfNotExistent(final Class<?> clazz) {
	   if (!configuration.containsKey(clazz)) {
	      configuration.put(clazz, new HashMap<String,Object>(0));
	   }
	}
	
	private boolean isInteger(String possibleInteger) {
		try {
			Integer.valueOf(possibleInteger);
			return true;
		} catch(NumberFormatException exc) {
			return false;
		}
	}

	


}
