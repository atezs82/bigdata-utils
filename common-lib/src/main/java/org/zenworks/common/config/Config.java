package org.zenworks.common.config;

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

    public <T> T getConfig(T type, ConfigKey key) {

        if (internalConfiguration.containsKey(type.getClass())) {
            return (T)internalConfiguration.get(type.getClass()).get(key.getCliParam());
        } else {
            return null;
        }
    }

    public <T> boolean isConfig(T type, String key) {
        if (internalConfiguration.containsKey(type.getClass())) {
            return internalConfiguration.get(type.getClass()).containsKey(key);
        }
        return false;
    }

    public <T> boolean isConfig(T type, ConfigKey key) {
        return isConfig(type, key.getCliParam());
    }
	
	public String getStringConfig(String key) {
		return getConfig(STRING, key);		
	}

    public String getStringConfig(ConfigKey key) {
        return getConfig(STRING, key);
    }
	
	public String[] getStringArrayConfig(String key) {
		return getConfig(STRING_ARRAY, key);		
	}

    public String[] getStringArrayConfig(ConfigKey key) {
        return getConfig(STRING_ARRAY, key);
    }

    public boolean isStringConfig(String key) {
		return isConfig(STRING, key);
	}

    public boolean isStringConfig(ConfigKey key) {
        return isConfig(STRING, key);
    }
	
	public boolean isStringArrayConfig(String key) {
		return isConfig(STRING_ARRAY, key);
	}

    public boolean isStringArrayConfig(ConfigKey key) {
        return isConfig(STRING_ARRAY, key);
    }

}
