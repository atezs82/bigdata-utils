package org.zenworks.infinispan.inquisitor;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.ServerStatistics;

import java.util.Map;

public class InfinispanAdapter {

    RemoteCache<Object,Object> cache;

    public InfinispanAdapter(final String infinispanHost) {
        RemoteCacheManager cacheContainer = new RemoteCacheManager(infinispanHost);
        cacheContainer.start();
        cache = cacheContainer.getCache();
    }

    public InfinispanAdapter(final String infinispanHost, final String cacheName) {
        RemoteCacheManager cacheContainer = new RemoteCacheManager(infinispanHost);
        cacheContainer.start();
        cache = cacheContainer.getCache(cacheName);
    }

    public Object getKey(Object key) {
        return cache.get(key);
    }

    public void setKey(Object key, Object value) {
        cache.put(key,value);
    }

    public Map<String,String> getStats() {
        return cache.stats().getStatsMap();
    }

    public void stopAdapter() {
        cache.stop();
    }

}
