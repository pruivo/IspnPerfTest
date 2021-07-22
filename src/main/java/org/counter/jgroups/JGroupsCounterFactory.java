package org.counter.jgroups;

import org.cache.Cache;
import org.cache.CacheFactory;
import org.jgroups.JChannel;
import org.jgroups.blocks.atomic.Counter;
import org.jgroups.blocks.atomic.CounterService;
import org.jgroups.util.Util;

/**
 * //TODO document this!
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class JGroupsCounterFactory implements CacheFactory<Object, Object> {

   private JChannel channel;
   private CounterService counterService;

   @Override
   public void init(String config) throws Exception {
      channel = new JChannel(config);
      channel.connect("counter");
      counterService = new CounterService(channel);
   }

   @Override
   public void destroy() {
      Util.close(channel);
   }

   @Override
   public Cache<Object, Object> create(String cache_name) {
      Counter counter = counterService.getOrCreateCounter(cache_name, 0);
      return new JGroupsCounter<>(counter);
   }
}
