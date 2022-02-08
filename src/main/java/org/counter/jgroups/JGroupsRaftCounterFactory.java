package org.counter.jgroups;

import java.io.File;
import java.util.Objects;

import org.cache.Cache;
import org.cache.CacheFactory;
import org.jgroups.JChannel;
import org.jgroups.protocols.raft.ELECTION;
import org.jgroups.protocols.raft.RAFT;
import org.jgroups.protocols.raft.Role;
import org.jgroups.raft.blocks.CounterService;
import org.jgroups.stack.Protocol;

/**
 * TODO!
 */
public class JGroupsRaftCounterFactory implements CacheFactory<Object, Object>, RAFT.RoleChange {

   private JChannel channel;
   private CounterService counterService;

   @Override
   public void init(String config) throws Exception {
      channel = new JChannel(config);

      File logDir = new File(raft().logName());
      if (logDir.exists()) {
         if (!logDir.delete()) {
            throw new IllegalStateException();
         }
      } else {
         File parent = logDir.getParentFile();
         if (!parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException();
         }
      }

      channel.connect("raft-counter");
      counterService = new CounterService(channel);

      System.out.println("[RAFT COUNTER] waiting for Leader election");
      long electionInterval = election().electionMinInterval();
      synchronized (this) {
         while (raft().leader() == null) {
            this.wait(electionInterval);
         }
      }
      System.out.println("[RAFT COUNTER] leader is " + raft().leader());
   }

   @Override
   public void destroy() {
      if (channel != null) {
         channel.disconnect();
         channel.close();
      }
   }

   @Override
   public Cache<Object, Object> create(String cache_name) {
      try {
         return new JGroupsCounter<>(counterService.getOrCreateCounter(cache_name, 0L));
      } catch (RuntimeException e) {
         throw e;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private <T extends Protocol> T protocol(Class<T> tClass) {
      return Objects.requireNonNull(channel.getProtocolStack().findProtocol(tClass));
   }

   private RAFT raft() {
      return protocol(RAFT.class);
   }

   private ELECTION election() {
      return protocol(ELECTION.class);
   }

   @Override
   public synchronized void roleChanged(Role role) {
      this.notifyAll();
   }
}
