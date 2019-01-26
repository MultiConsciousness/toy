package com.walker.test.shiro;

import com.walker.test.common.SysConstant;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisCache redisCache;
    //private final ConcurrentMap<String,Cache> caches = new ConcurrentHashMap<>();
    private static Logger log = LoggerFactory.getLogger(RedisCacheManager.class);


    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        /*String cacheName = SysConstant.ShiroConfig.SHIRO_CACHE_PREFIX+name;
        log.debug("get cache from map,name: "+cacheName);
        Cache cache = caches.get(cacheName);
        if(null == cache){
            cache = redisCache;
            caches.put(cacheName,cache);
        }*/
        log.debug("get cache from map");
        return redisCache;
    }
}
