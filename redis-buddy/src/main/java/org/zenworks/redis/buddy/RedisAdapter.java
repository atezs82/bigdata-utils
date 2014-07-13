package org.zenworks.redis.buddy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class RedisAdapter {
		
	Jedis jedis;

    String redisHostPort;
	
	public RedisAdapter(final String host, int port) {		
		jedis = new Jedis(host, port);
        redisHostPort = host+":"+port;
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
		List<String> result = new ArrayList<String>();
		
		for (int index =0;index<getListSize(listKey);index++) {
			result.add(jedis.lindex(listKey, index));
		}
		
		return result;		
	}

    public long getListSize(String listKey) {
        return jedis.llen(listKey);
    }
	
	// MONITOR ABILITY
	
	public Map<String,String> getHashKey(String hashKey) {
		Map<String,String> result = new HashMap<String,String>();
	   for (String key:jedis.hkeys(hashKey)) {
		   result.put(key, jedis.hget(hashKey, key));
		   
	   }
	   return result;
	}

    public long getHashSize(String hashKey) {
        return jedis.hlen(hashKey);
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

    public String getRedisHost() {
       return redisHostPort;
    }
}
