package org.zenworks.infinispan.inquisitor;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import org.apache.lucene.search.Query;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.ServerStatistics;
import org.infinispan.query.Search;

import java.util.Map;

public class InfinispanAdapter {

    RemoteCache<Object,Object> cache;

    final String infinispanHost;

    public InfinispanAdapter(final String infinispanHost) {
        RemoteCacheManager cacheContainer = new RemoteCacheManager(infinispanHost);
        cacheContainer.start();
        cache = cacheContainer.getCache();
        this.infinispanHost = infinispanHost;
    }

    public InfinispanAdapter(final String infinispanHost, final String cacheName) {
        RemoteCacheManager cacheContainer = new RemoteCacheManager(infinispanHost);
        cacheContainer.start();
        cache = cacheContainer.getCache(cacheName);
        this.infinispanHost = infinispanHost;
    }

    public Object getKey(Object key) {
        return cache.get(key);
    }

    public void setKey(Object key, Object value) {
        cache.put(key,value);
    }

    public void deleteKey(Object key) { cache.remove(key); }

    public Map<String,String> getStats() {
        return cache.stats().getStatsMap();
    }

    public void stopAdapter() {
        cache.stop();
    }

    public String getHost() {
        return infinispanHost;
    }
}
