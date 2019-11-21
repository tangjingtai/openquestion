package com.jt.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
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

    /**
     * 根据key获取缓存对象
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置Key对应的缓存对象
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置key对应的缓存对象，并指定过期时间
     * @param key 键
     * @param value 值
     * @param time 过期时间
     */
    public void set(String key, Object value, long time){
        redisTemplate.opsForValue().set(key, value, time);
    }

    /**
     * 指定key自增
     * @param key 键
     * @return 递增后的值
     */
    public long increment(String key){
        return increment(key, 1);
    }

    /**
     * 指定增量值递增
     * @param key 键
     * @param delta 增量
     * @return 递增之后的值
     */
    public long increment(String key, long delta){
        if(delta < 0){
            throw new RuntimeException("delta must be greater than 0.");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @return 递减之后的值
     */
    public long decrement(String key){
        return decrement(key,-1);
    }

    /**
     * 指定减量值递减
     * @param key 键
     * @param delta 减量
     * @return 递减之后的值
     */
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

    /**
     * 对hash表增加一条记录
     * @param key 键
     * @param hashKey hash表的键
     * @param value hash表的值
     */
    public void hset(String key, String hashKey, Object value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 对hash表增加一条记录，并指定过期时间
     * @param key 键
     * @param hashKey hash表的键
     * @param value hash表的值
     * @param time 过期时间
     */
    public void hset(String key, String hashKey, Object value, long time){
        redisTemplate.opsForHash().put(key, hashKey, value);
        if(time > 0){
            expire(key, time);
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键
     * @param hashKeys hash键集合
     */
    public void hdel(String key, Object... hashKeys){
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * hash递减
     * @param key 键
     * @param hashKey hash表的键
     * @param delta 增量值
     * @return 递增之后的值
     */
    public double hincr(String key, String hashKey, double delta){
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    public Set<Object> sGet(String key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值的集合
     * @return 成功个数
     */
    public long sSet(String key, Object... values){
        return redisTemplate.opsForSet().add(key, values);
    }
}
