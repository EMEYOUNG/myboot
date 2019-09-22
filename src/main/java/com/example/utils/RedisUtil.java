package com.example.utils;

import com.example.controller.UserLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 插入单个缓存对象
     * @param key 键
     * @param value 值
     * @return 操作结果
     */
    public boolean put(String key,Object value)
    {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            return true;
        } catch (Exception e) {
            logger.error(String.format("An exception occurs when redis saves data. detail messages: %d"),e.getMessage());
            return false;
        }
    }

    /**
     * 设置带有超时时间的缓存对象
     * @param key 键
     * @param value 值
     * @param expire 过期时间
     * @return 操作结果
     */
    public boolean put(String key,Object value,long expire)
    {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expire, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            logger.error(String.format("An exception occurs when redis saves data. detail messages: %d"),e.getMessage());
            return false;
        }
    }

    /**
     * 取缓存
     * @param key 键
     * @return 缓存对象
     */
    public Object get(String key)
    {
        ValueOperations<String, Object> operations  = redisTemplate.opsForValue();
        Object result = operations.get(key);
        if(null == result)
            return null;
        else
            return result;
    }


    /**
     * 多个删除
     * @param keys 键列表
     */
    public void remove(final String... keys)
    {
        for (String key : keys) remove(key);
    }

    /**
     * 单个删除
     * @param key 键
     */
    public void remove(final String key)
    {
        if (redisTemplate.hasKey(key)) redisTemplate.delete(key);
    }

    /**
     * 是否存在
     * @param key 键
     * @return 是否存在，true为存在，false反之
     */
    public boolean extis(final String key)
    {
        return redisTemplate.hasKey(key);
    }
}
