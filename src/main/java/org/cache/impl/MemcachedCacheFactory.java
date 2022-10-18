package org.cache.impl;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.cache.Cache;
import org.cache.CacheFactory;

import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * TODO! document this
 */
public class MemcachedCacheFactory<K, V> implements CacheFactory<K, V> {

   static final long TIMEOUT = TimeUnit.SECONDS.toMillis(30);
   private static final List<InetSocketAddress> SERVERS = Arrays.asList(
         new InetSocketAddress("192.168.1.4", 11221),
         new InetSocketAddress("192.168.1.4", 11321),
         new InetSocketAddress("192.168.1.4", 11421)
   );

   private MemcachedClient client;

   @Override
   public void init(String config) throws Exception {
      DefaultConnectionFactory d = new DefaultConnectionFactory() {
         @Override
         public long getOperationTimeout() {
            return TIMEOUT;
         }
      };
      client = new MemcachedClient(d, SERVERS);
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
