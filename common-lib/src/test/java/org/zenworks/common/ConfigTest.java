package org.zenworks.common;

import static org.testng.AssertJUnit.assertArrayEquals;
import static org.testng.AssertJUnit.assertEquals;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;
import org.zenworks.common.config.Config;

public class ConfigTest {
	
	@Test
	public void getOutString() {
		
		Map<Class<?>,Map<String,Object>> testMap = new HashMap<Class<?>,Map<String,Object>>();
		Map<String,Object> stringMap = new HashMap<String,Object>();
		stringMap.put("stringKey", "value");
		testMap.put(String.class, stringMap);
		
		Config testConfig = new Config(testMap);
		
		assertEquals(testConfig.getConfig(Config.STRING, "stringKey"), new String("value"));
		
	}
	
	@Test
	public void getOutStringArray() {
		Map<Class<?>,Map<String,Object>> testMap = new HashMap<Class<?>,Map<String,Object>>();
		Map<String,Object> stringMap = new HashMap<String,Object>();
		stringMap.put("arrayKey", new String[]{"value1","value2","value3"});
		testMap.put(String[].class,stringMap);
			
		Config testConfig = new Config(testMap);
		
		assertArrayEquals(testConfig.getConfig(Config.STRING_ARRAY, "arrayKey"), new String[]{"value1","value2","value3"});
		
	}
	
	@Test
	public void returnNullIfNotExisting() {
		Map<Class<?>,Map<String,Object>> testMap = new HashMap<Class<?>,Map<String,Object>>();
		
		Config testConfig = new Config(testMap);
		
		assertEquals(testConfig.getConfig(Config.STRING, "not-existing"),null);
	}
	
	@Test
	public void checkMissingParameter() {
		Map<Class<?>,Map<String,Object>> testMap = new HashMap<Class<?>,Map<String,Object>>();
		
		Config testConfig = new Config(testMap);
		
		assertEquals(testConfig.isConfig(Config.STRING, "not-existing"),false);
	}

	
	@Test
	public void checkExistingParameter() {
		
		Map<Class<?>,Map<String,Object>> testMap = new HashMap<Class<?>,Map<String,Object>>();
		Map<String,Object> stringMap = new HashMap<String,Object>();
		stringMap.put("stringKey", "value");
		testMap.put(String.class, stringMap);
		
		Config testConfig = new Config(testMap);
		
		assertEquals(testConfig.isConfig(Config.STRING, "stringKey"), true);
		
	}
	
	

}
