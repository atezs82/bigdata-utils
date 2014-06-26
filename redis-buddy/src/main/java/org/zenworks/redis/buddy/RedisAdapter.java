package org.zenworks.redis.buddy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisAdapter {
		
	Jedis jedis;
	
	public RedisAdapter(final String host, int port) {		
		jedis = new Jedis(host, port);		
	}
	
	public List<String> getKeys() {		
		return getKeys("*");		
	}
	
	public List<String> getKeys(final String query) {
		return Arrays.asList(jedis.keys(query).toArray(new String[] {}));
	}
	
	
	public String getType(String key) {
		return jedis.type(key);
	}
	
	public List<String> getListContents(String listKey) {
		long size = jedis.llen(listKey);
		List<String> result = new ArrayList<String>();
		
		for (int index =0;index<size;index++) {
			result.add(jedis.lindex(listKey, index));
		}
		
		return result;		
	}
	
	// MONITOR ABILITY
	
	public Map<String,String> getHashKey(String hashKey) {
		Map<String,String> result = new HashMap<String,String>();
	   for (String key:jedis.hkeys(hashKey)) {
		   result.put(key, jedis.hget(hashKey, key));
		   
	   }
	   return result;
	}
	
	public void delete(final String key) {
		jedis.del(key);
	}
	
	public String getStringKey(String stringKey) {
		return jedis.get(stringKey);
	}
	
	public void flushAll() {
		jedis.flushAll();
	}
}
