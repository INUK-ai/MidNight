package com.mid.night._core.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, Object> redisTemplate;

    public Object getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    public void setEmailKey(String key, String hashKey, String value, long timeout, TimeUnit unit) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);
        redisTemplate.expire(key, timeout, unit);
    }

    public String getHashValue(String key, String hashKey) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.get(key, hashKey);
    }

    public void deleteHashValue(String key, String hashKey) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.delete(key, hashKey);
    }
}
