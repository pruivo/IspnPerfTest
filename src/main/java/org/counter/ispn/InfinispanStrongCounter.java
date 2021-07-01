package org.counter.ispn;

import java.util.concurrent.CompletionStage;

import org.infinispan.counter.api.StrongCounter;

/**
 * TODO! document this
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class InfinispanStrongCounter<K, V> extends InfinispanBaseCounter<K, V> {

   private final StrongCounter counter;

   public InfinispanStrongCounter(StrongCounter counter) {
      this.counter = counter;
   }

   @Override
   public V put(K key, V value) {
      return counter.incrementAndGet().thenApply(applyFunction()).join();
   }

   @Override
   public void clear() {
      counter.reset().join();
   }

   @Override
   CompletionStage<Long> getValue() {
      return counter.getValue();
   }
}
