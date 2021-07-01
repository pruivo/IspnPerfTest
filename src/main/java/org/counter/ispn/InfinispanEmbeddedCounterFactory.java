package org.counter.ispn;

import org.cache.Cache;
import org.cache.CacheFactory;
import org.infinispan.counter.EmbeddedCounterManagerFactory;
import org.infinispan.counter.api.CounterConfiguration;
import org.infinispan.counter.api.CounterManager;
import org.infinispan.counter.api.CounterType;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * HACK! We are perf the counter but pretending to be a cache
 * <p>
 * a put() is converted to increment counter's value and a get() is converted to get counter's value
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class InfinispanEmbeddedCounterFactory<K, V> implements CacheFactory<K, V> {

   protected EmbeddedCacheManager cacheManager;
   protected CounterManager counterManager;

   public InfinispanEmbeddedCounterFactory() {
   }

   @Override
   public void init(String config) throws Exception {
      cacheManager = new DefaultCacheManager(config);
      counterManager = EmbeddedCounterManagerFactory.asCounterManager(cacheManager);
   }

   @Override
   public void destroy() {
      cacheManager.stop();
   }

   @Override
   public Cache<K, V> create(String cache_name) {
      CounterConfiguration config = counterManager.getConfiguration(cache_name);
      if (config == null) {
         throw new IllegalArgumentException("Counter " + cache_name + " not found!");
      }
      return config.type() == CounterType.WEAK ?
            new InfinispanWeakCounter<>(counterManager.getWeakCounter(cache_name)) :
            new InfinispanStrongCounter<>(counterManager.getStrongCounter(cache_name));
   }
}
