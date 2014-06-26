package org.zenworks.common;

import java.util.HashMap;
import java.util.Map;

public class ConfigFactory {
	
	private Map<Class<?>,Map<String,Object>> configuration;
	
	public ConfigFactory() {
		configuration = new HashMap<Class<?>,Map<String,Object>>();
		addClassIfNotExistent(String.class);
		addClassIfNotExistent(String[].class);
	}
	
	public void addStringParameter(final String key, final String value) {
	   configuration.get(String.class).put(key,value);
	}
	
	public void addStringParameterWithDelimiter(final String keyWithValue, final String keyValueDelimiter) {
		configuration.get(String.class).put(keyWithValue.split(keyValueDelimiter)[0],keyWithValue.split(keyValueDelimiter)[1]);	
	}
	
	public void addStringArrayParameter(final String key, final String... valueArray) {	   
	   configuration.get(String[].class).put(key, valueArray);
	}
	
	public void addStringArrayParameterWithDelimiter(final String keyWithValues, final String keyValueDelimiter, final String valuesDelimiter) {
		configuration.get(String[].class).put(keyWithValues.split(keyValueDelimiter)[0], keyWithValues.split(keyValueDelimiter)[1].split(valuesDelimiter));
	}
	
	public Config createConfig() {
	   return new Config(configuration);		
	}	
	
	private void addClassIfNotExistent(final Class<?> clazz) {
	   if (!configuration.containsKey(clazz)) {
	      configuration.put(clazz, new HashMap<String,Object>());
	   }
	}

}
