package com.bo.utils;

import com.bo.redis.KeyPrefix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

/**
 * redis操作类
 */
public class JedisUtil {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JedisPool jedisPool;
    private Jedis getJedis(){
        return jedisPool.getResource();
    }

    /**
     * 设值
     * @param key
     * @param value
     * @return
     */
    public String set(KeyPrefix keyPrefix,String key, String value){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;   //redis实际上存储的是 keyPrefix:redisKey  => redisValue
            return jedis.set(key,value);
        } catch (Exception e) {
            logger.error("set key:{} value:{} error",key,value,e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 设值及过期时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public String set(KeyPrefix keyPrefix,String key, String value, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            return jedis.setex(key, expireTime, value);
        } catch (Exception e) {
            logger.error("set key:{} value:{} expireTime:{} error", key, value, expireTime, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 取值
     * @param key
     * @return
     */
    public String get(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("get key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 自减一
     */
    public Long decr(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.decr(key);
        } catch (Exception e) {
            logger.error("decr key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 自增一
     */
    public Long incr(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.incr(key);
        } catch (Exception e) {
            logger.error("incr key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public Long del(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.del(key.getBytes());
        } catch (Exception e) {
            logger.error("del key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 批量删除
     */
    public Long delBatchByPrefix(KeyPrefix keyPrefix) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            Set<String> sets = findDataByPrefix(keyPrefix);
            for (String key : sets) {
                jedis.del(key.getBytes());
            }
            return 1L;
        } catch (Exception e) {
            logger.error("del key:{} error", keyPrefix, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 通过前缀查询所有的key
     * @param keyPrefix
     * @return
     */
    private Set<String> findDataByPrefix(KeyPrefix keyPrefix) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            String key = keyPrefix.getPrefix()+"*";
            return jedis.keys(key);
        } catch (Exception e) {
            logger.error("find key:{} error", keyPrefix, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.exists(key.getBytes());
        } catch (Exception e) {
            logger.error("exists key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }


    /**
     * 设置key过期时间
     * @param key
     * @param expireTime
     * @return
     */
    public Long expire(KeyPrefix keyPrefix,String key, int expireTime) {
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.expire(key.getBytes(), expireTime);
        } catch (Exception e) {
            logger.error("expire key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    /**
     * 获取剩余时间
     * @param key
     * @return
     */
    public Long ttl(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            String prefix = keyPrefix.getPrefix();
            key = prefix + key;
            jedis = getJedis();
            return jedis.ttl(key);
        } catch (Exception e) {
            logger.error("ttl key:{} error", key, e);
            return null;
        } finally {
            close(jedis);
        }
    }

    private void close(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }
}
