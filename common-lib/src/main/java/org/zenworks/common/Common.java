package org.zenworks.common;

import java.util.Map;

public class Common {
	
	static Config config;
	public static final String CONFIG_PARAM_PREFIX = "--";
	
	public static Config initConfig(Map<Class<?>,Map<String,Object>> configContent) {
		config = new Config(configContent);
		return config;
	}
	
	public static Config initConfig(String[] commandlineArguments) {
		ConfigFactory configFactory = new ConfigFactory();
		
		for (String commandLineArgument : commandlineArguments) {
			if (commandLineArgument.startsWith(CONFIG_PARAM_PREFIX)) {
				configFactory.addParameter(commandLineArgument.substring(2));	
			} else {
				configFactory.addParameter(commandLineArgument);
			}			
		}
		
		config= configFactory.createConfig();
		
		return config;
	}
	
	public static Config getConfig() {
		if (config == null) {
			throw new IllegalStateException("Need to initialize configuration first.");
		}
		return config;		
	}

}
