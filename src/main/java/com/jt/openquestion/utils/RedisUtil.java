package com.jt.openquestion.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 指定缓存失效时间
     * @param key 缓存Key
     * @param time 失效时间，单位：秒
     */
    public void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    public long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public void del(String... keys){
        if(keys==null || keys.length == 0){
            return;
        }
        if(keys.length == 1){
            redisTemplate.delete(keys[0]);
        }else{
            redisTemplate.delete(Arrays.asList(keys));
        }
    }

    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long time){
        redisTemplate.opsForValue().set(key, value, time);
    }

    public long increment(String key){
        return increment(key, 1);
    }

    public long increment(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("delta must be greater than 0.");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decrement(String key){
        return decrement(key,-1);
    }

    public long decrement(String key, long delta){
        if(delta > 0){
            throw new RuntimeException("delta must be less than 0.");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 从缓存的hash对象中获取值
     * @param key 缓存key
     * @param hashKey 缓存中的hashkey
     * @return hashkey对应的值
     */
    public Object hget(String key, String hashKey){
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<Object, Object> hmget(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    public void hmset(String key, Map<String, Object> map, long time){
        redisTemplate.opsForHash().putAll(key, map);
        if(time > 0){
            expire(key, time);
        }
    }

    public void hset(String key, String hashKey, Object value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hset(String key, String hashKey, Object value, long time){
        redisTemplate.opsForHash().put(key, hashKey, value);
        if(time > 0){
            expire(key, time);
        }
    }

    public void hdel(String key, Object... hashKeys){
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public double  hincr(String key, String hashKey, double delta){
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }
}
