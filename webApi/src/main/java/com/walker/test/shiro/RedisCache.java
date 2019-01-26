package com.walker.test.shiro;

import com.walker.test.common.SysConstant;
import com.walker.test.configuration.JedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RedisCache<K, V> implements Cache<K, V> {

    @Autowired
    private RedisSerializer serializer;
    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private StringSerializer stringSerializer;
    @Autowired
    private ObjectSerializer objectSerializer;
    private int expireTtl = SysConstant.ShiroConfig.DEFAULT_TTL;

    @Override
    public V get(K k) throws CacheException {
        if (null == k)
            return null;
        byte[] temp = jedisUtil.get(formatKey(k));
        return (V) serializer.objectDeserialize(objectSerializer, temp);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        if (null == k)
            return null;
        byte[] key = formatKey(k);
        byte[] value = serializer.objectSerialize(objectSerializer, v);
        byte[] oldValue = jedisUtil.setWithExpire(key, value, expireTtl);
        return (V) serializer.objectDeserialize(objectSerializer, oldValue);
    }

    @Override
    public V remove(K k) throws CacheException {
        if (null == k)
            return null;
        byte[] oldValue = jedisUtil.delete(formatKey(k));
        return (V) serializer.objectDeserialize(objectSerializer, oldValue);
    }

    @Override
    public void clear() throws CacheException {
        Set<byte[]> keys = null;
        keys = jedisUtil.keys(serializer.stringSerialize(stringSerializer, SysConstant.ShiroConfig.SHIRO_CACHE_PREFIX + "*"));
        if (null == keys || keys.size() == 0)
            return;
        for (byte[] key : keys)
            jedisUtil.delete(key);

    }

    @Override
    public int size() {
        Long size = new Long(jedisUtil.dbSize(serializer.stringSerialize(stringSerializer, SysConstant.ShiroConfig.SHIRO_CACHE_PREFIX + "*")));
        return size.intValue();
    }

    @Override
    public Set<K> keys() {
        Set<K> keys = new HashSet<>();
        Set<byte[]> keySet = jedisUtil.keys(serializer.stringSerialize(stringSerializer, SysConstant.ShiroConfig.SHIRO_CACHE_PREFIX + "*"));
        if (CollectionUtils.isEmpty(keySet))
            return keys;
        for (byte[] key : keySet)
            keys.add((K) serializer.stringDeserialize(stringSerializer, key));
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        Set<byte[]> keySet = jedisUtil.keys(serializer.stringSerialize(stringSerializer, SysConstant.ShiroConfig.SHIRO_CACHE_PREFIX + "*"));
        if (CollectionUtils.isEmpty(keySet))
            return values;
        for (byte[] key : keySet) {
            byte[] value = jedisUtil.get(key);
            values.add((V) serializer.objectDeserialize(objectSerializer, value));
        }
        return values;
    }

    private byte[] formatKey(K k) {
        byte[] emptyBytes = new byte[0];
        if (null == k)
            return emptyBytes;
        if (k instanceof String)
            return serializer.stringSerialize(stringSerializer, SysConstant.ShiroConfig.SHIRO_CACHE_PREFIX + k);
        emptyBytes = serializer.objectSerialize(objectSerializer,k);
        return emptyBytes;
    }

    public int getExpireTtl() {
        return expireTtl;
    }

    public void setExpireTtl(int expireTtl) {
        this.expireTtl = expireTtl;
    }

}
