package org.zenworks.common.config;

public enum ConfigParam {
	
	REDIS_CONNECT_STRING("redis-connect-string", String.class),
	REDIS_INITIAL_KEYS("redis-initial-keys", String[].class),
	ZOOKEEPER_CONNECT_STRING("zookeeper-connect-string", String[].class);
	
	Class<?> configType;
	
	private ConfigParam(String mapKey, Class<?> configType) {
	   this.configType = configType;
	}

}
