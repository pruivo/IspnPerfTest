package org.cache.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.cache.Cache;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * TODO! document this
 */
public class MemcachedCache<K, V> implements Cache<K, V> {

   private final MemcachedClient client;

   public MemcachedCache(MemcachedClient client) {
      this.client = client;
   }

   @Override
   public V put(K key, V value) {
      await(client.set(String.valueOf(key), 0, value));
      return null;
   }

   @Override
   public V get(K key) {
      return (V) client.get(String.valueOf(key));
   }

   @Override
   public void clear() {
      await(client.flush());
   }

   @Override
   public int size() {
      //unsupported
      return 0;
   }

   @Override
   public boolean isEmpty() {
      // unsupported
      return false;
   }

   @Override
   public Set<K> keySet() {
      //unsupported
      return Collections.emptySet();
   }

   @Override
   public Map<K, V> getContents() {
      //unsupported
      return Collections.emptyMap();
   }

   private static <T> void await(OperationFuture<T> future) {
      try {
         future.get(MemcachedCacheFactory.TIMEOUT, TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
         throw new RuntimeException(e);
      } catch (TimeoutException e) {
         throw new RuntimeException(e);
      } catch (ExecutionException e) {
         throw new RuntimeException(e.getCause());
      }
   }
}
