package org.counter.ispn;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.infinispan.counter.api.WeakCounter;

/**
 * HACK! We are perf the counter but pretending to be a cache
 * <p>
 * a put() is converted to increment counter's value and a get() is converted to get counter's value
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class InfinispanWeakCounter<K, V> extends InfinispanBaseCounter<K, V> {

   private final WeakCounter counter;

   public InfinispanWeakCounter(WeakCounter counter) {
      this.counter = counter;
   }

   @Override
   public V put(K key, V value) {
      return counter.increment().thenApply(applyFunction()).join();
   }

   @Override
   public void clear() {
      counter.reset().join();
   }

   @Override
   CompletionStage<Long> getValue() {
      return CompletableFuture.completedFuture(counter.getValue());
   }
}
