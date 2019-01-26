package com.walker.test.configuration;

import com.walker.test.exception.CustomJedisException;
import com.walker.test.common.SysConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JedisUtil {

    private static Logger log = LoggerFactory.getLogger(JedisUtil.class);

    @Autowired
    private JedisPool jedisPool;


    public byte[] get(byte[] key) {
        if (null == key)
            return null;
        byte[] value = null;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.QUERY_ERROR, e);
            throw new CustomJedisException(SysConstant.RedisMessage.QUERY_ERROR);
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return value;
    }

    public byte[] setWithExpire(byte[] key, byte[] value, int expire) {
        if (null == key)
            return null;
        Jedis jedis = null;
        byte[] oldValue = null;
        try {
            jedis = jedisPool.getResource();
            oldValue = jedis.get(key);
            jedis.set(key, value);
            if (expire > 0)
                jedis.expire(key, expire);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.SET_ERROR, e);
            throw new CustomJedisException(SysConstant.RedisMessage.SET_ERROR);
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return oldValue;
    }

    public byte[] delete(byte[] key) {
        if (null == key)
            return null;
        Jedis jedis = null;
        byte[] oldValue = null;
        try {
            jedis = jedisPool.getResource();
            oldValue = jedis.get(key);
            jedis.del(key);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.DEL_ERROR, e);
            throw new CustomJedisException(SysConstant.RedisMessage.DEL_ERROR);
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return oldValue;
    }

    public void expire(byte[] key, int i) {
        if (null == key)
            return;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (i > 0)
                jedis.expire(key, i);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.EXPIRE_ERROR, e);
            throw new CustomJedisException(SysConstant.RedisMessage.EXPIRE_ERROR);
        } finally {
            if (null != jedis)
                jedis.close();
        }
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = null;
        byte[] oldValue = null;
        try {
            jedis = jedisPool.getResource();
            oldValue = jedis.get(key);
            jedis.set(key, value);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.SET_ERROR, e);
            throw new CustomJedisException(SysConstant.RedisMessage.SET_ERROR);
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return oldValue;
    }

    public Set<byte[]> keys(byte[] pattern) {
        Set<byte[]> keys = new HashSet<>();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            ScanParams scan = new ScanParams();
            scan.count(SysConstant.ShiroConfig.DEFAULT_COUNT);
            scan.match(pattern);
            byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;
            ScanResult<byte[]> scanResult;
            do {
                scanResult = jedis.scan(cursor, scan);
                keys.addAll(scanResult.getResult());
                cursor = scanResult.getCursorAsBytes();
            } while (scanResult.getStringCursor().compareTo(ScanParams.SCAN_POINTER_START) > 0);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.KEYS_ERROR,e);
            throw new CustomJedisException(SysConstant.RedisMessage.KEYS_ERROR);
        } finally {
            if (null != jedis)
                jedis.close();
        }
        return keys;
    }

    public long dbSize(byte[] pattern){
        long dbSize = 0L;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            ScanParams params = new ScanParams();
            params.count(SysConstant.ShiroConfig.DEFAULT_COUNT);
            params.match(pattern);
            byte[] cursor = ScanParams.SCAN_POINTER_START_BINARY;
            ScanResult<byte[]> scanResult;
            do{
                scanResult = jedis.scan(cursor,params);
                List<byte[]> results = scanResult.getResult();
                for(byte[] result: results)
                    dbSize++;
                cursor = scanResult.getCursorAsBytes();
            }while(scanResult.getStringCursor().compareTo(ScanParams.SCAN_POINTER_START) > 0);
        } catch (Exception e) {
            log.error(SysConstant.RedisMessage.KEYS_ERROR,e);
            throw new CustomJedisException(SysConstant.RedisMessage.KEYS_ERROR);
        } finally {
            if(null != jedis)
                jedis.close();
        }
        return dbSize;
    }

}
