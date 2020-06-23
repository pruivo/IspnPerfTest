package org.cache.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.cache.Cache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ClusterConfiguration;
import org.infinispan.client.hotrod.impl.transport.netty.ChannelFactory;

/**
 * @author Bela Ban
 * @since x.y
 */
public class HotrodCache<K,V> implements Cache<K,V> {
    private final RemoteCache<K,V> rc;
    private final List<String> clusters;

    public HotrodCache(RemoteCache<K, V> rc, List<String> clusters) {
        this.rc=rc;
        this.clusters = clusters;
    }

    public V put(K key, V value) {
        return rc.put(key, value);
    }

    public V get(K key) {
        return rc.get(key);
    }

    public void clear() {
        rc.clear();
    }

    public int size() {
        return rc.size();
    }

    public boolean isEmpty() {
        return rc.isEmpty();
    }

    public Set<K> keySet() {
        return rc.keySet();
    }

    public Map<K,V> getContents() {
        Map<K,V> contents=new HashMap<>();
        for(Map.Entry<K,V> entry: rc.entrySet())
            contents.put(entry.getKey(), entry.getValue());
        return contents;
    }

    @Override
    public boolean isClientServer() {
        return true;
    }

    @Override
    public Collection<String> clusters() {
       return clusters;
    }

    @Override
    public boolean switchToCluster(String id) {
        return this.rc.getRemoteCacheManager().switchToCluster(id);
    }

    @Override
    public void resetCluster() {
        this.rc.getRemoteCacheManager().switchToDefaultCluster();
    }
}
