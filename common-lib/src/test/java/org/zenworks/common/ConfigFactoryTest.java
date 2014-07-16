package org.zenworks.common;

import static org.testng.AssertJUnit.assertArrayEquals;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import org.zenworks.common.config.Config;
import org.zenworks.common.config.ConfigFactory;
import org.zenworks.common.config.ConfigKey;

public class ConfigFactoryTest {
	
	@Test
	public void determineStringParameterTypeAndCreateConfig() {
	   ConfigFactory testFactory = new ConfigFactory();
	   
	   testFactory.addParameter(ConfigKey.ZOOKEEPER_FAVORITES.getCliParam()+"=value");

               assertEquals(testFactory.createConfig().getConfig(Config.STRING, ConfigKey.ZOOKEEPER_FAVORITES.getCliParam()), "value");
	   
	}
	
	@Test
	public void determineStringArrayParameterTypeAndCreateConfig() {
	   ConfigFactory testFactory = new ConfigFactory();
	   
	   testFactory.addParameter(ConfigKey.ZOOKEEPER_FAVORITES.getCliParam()+"=value1;value2;value3");
	   
	   assertArrayEquals(testFactory.createConfig().getConfig(Config.STRING_ARRAY, ConfigKey.ZOOKEEPER_FAVORITES.getCliParam()),new String[]{"value1","value2","value3"});
	   
	}
	
	@Test
	public void determineIntegerParameterTypeAndCreateConfig() {
	   ConfigFactory testFactory = new ConfigFactory();
	   
	   testFactory.addParameter(ConfigKey.ZOOKEEPER_FAVORITES.getCliParam()+"=123456");
	   
	   assertEquals(testFactory.createConfig().getConfig(Config.INTEGER, ConfigKey.ZOOKEEPER_FAVORITES.getCliParam()),new Integer(123456));
	   
	}


}
