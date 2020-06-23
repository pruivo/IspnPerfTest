package org.cache.impl;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.cache.Cache;
import org.cache.CacheFactory;

/**
 * //TODO document this!
 *
 * @author Pedro Ruivo
 * @since 11.0
 */
public class MasterRole<K, V> implements CacheFactory<K, V> {

   @Override
   public void init(String config) throws Exception {
      //no-op
   }

   @Override
   public void destroy() {
      //no-op
   }

   @Override
   public Cache<K, V> create(String cache_name) {
      return new Cache<>() {
         @Override
         public V put(K key, V value) {
            return null;
         }

         @Override
         public V get(K key) {
            return null;
         }

         @Override
         public void clear() {

         }

         @Override
         public int size() {
            return 0;
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
      };
   }


}
