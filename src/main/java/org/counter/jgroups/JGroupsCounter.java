package org.counter.jgroups;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cache.Cache;
import org.jgroups.blocks.atomic.Counter;

/**
 * //TODO document this!
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class JGroupsCounter<K, V> implements Cache<K, V> {

   private final Counter counter;

   public JGroupsCounter(Counter counter) {
      this.counter = counter;
   }

   @Override
   public V put(K key, V value) {
      counter.incrementAndGet();
      return null;
   }

   @Override
   public V get(K key) {
      counter.get();
      return null;
   }

   @Override
   public void clear() {
      counter.set(0);
   }

   @Override
   public int size() {
      return (int) counter.get();
   }

   @Override
   public boolean isEmpty() {
      return true;
   }

   @Override
   public Set<K> keySet() {
      return Collections.emptySet();
   }

   @Override
   public Map<K, V> getContents() {
      return Collections.emptyMap();
   }
}
