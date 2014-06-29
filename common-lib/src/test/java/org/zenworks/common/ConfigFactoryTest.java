package org.zenworks.common;

import static org.testng.AssertJUnit.assertArrayEquals;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;

public class ConfigFactoryTest {
	
	@Test
	public void determineStringParameterTypeAndCreateConfig() {
	   ConfigFactory testFactory = new ConfigFactory();
	   
	   testFactory.addParameter("stringKey=value");
	   
	   assertEquals(testFactory.createConfig().getConfig(Config.STRING, "stringKey"),"value");
	   
	}
	
	@Test
	public void determineStringArrayParameterTypeAndCreateConfig() {
	   ConfigFactory testFactory = new ConfigFactory();
	   
	   testFactory.addParameter("multiple=value1;value2;value3");
	   
	   assertArrayEquals(testFactory.createConfig().getConfig(Config.STRING_ARRAY, "multiple"),new String[]{"value1","value2","value3"});
	   
	}
	
	@Test
	public void determineIntegerParameterTypeAndCreateConfig() {
	   ConfigFactory testFactory = new ConfigFactory();
	   
	   testFactory.addParameter("intKey=123456");
	   
	   assertEquals(testFactory.createConfig().getConfig(Config.INTEGER, "intKey"),new Integer(123456));
	   
	}


}
