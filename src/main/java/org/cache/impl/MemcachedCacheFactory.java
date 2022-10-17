package org.cache.impl;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.cache.Cache;
import org.cache.CacheFactory;

import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * TODO! document this
 */
public class MemcachedCacheFactory<K, V> implements CacheFactory<K, V> {

   static final long TIMEOUT = TimeUnit.MINUTES.toMillis(1);

   private MemcachedClient client;

   @Override
   public void init(String config) throws Exception {
      DefaultConnectionFactory d = new DefaultConnectionFactory() {
         @Override
         public long getOperationTimeout() {
            return TIMEOUT;
         }
      };
      client = new MemcachedClient(d, Collections.singletonList(new InetSocketAddress("127.0.0.1", 11221)));
   }

   @Override
   public void destroy() {
      if (client != null) {
         client.shutdown();
         client = null;
      }
   }

   @Override
   public Cache<K, V> create(String cache_name) {
      return new MemcachedCache<>(client);
   }
}
