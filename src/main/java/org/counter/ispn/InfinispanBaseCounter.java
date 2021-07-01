package org.counter.ispn;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import org.cache.Cache;

/**
 * HACK! We are perf the counter but pretending to be a cache
 * <p>
 * a put() is converted to increment counter's value and a get() is converted to get counter's value
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public abstract class InfinispanBaseCounter<K, V> implements Cache<K, V> {

   private final Function<?, V> applyFunction = v -> null;

   @Override
   public V get(K key) {
      return getValue().thenApply(applyFunction()).toCompletableFuture().join();
   }

   @Override
   public int size() {
      return 1;
   }

   @Override
   public boolean isEmpty() {
      return false;
   }

   @Override
   public Set<K> keySet() {
      return Collections.emptySet();
   }

   @Override
   public Map<K, V> getContents() {
      return Collections.emptyMap();
   }

   abstract CompletionStage<Long> getValue();

   <T> Function<T, V> applyFunction() {
      return (Function<T, V>) applyFunction;
   }
}
