package com.nh.dsh.admin.security.cache;

import com.alibaba.fastjson2.JSON;
import com.nh.dsh.admin.common.cache.RedisCache;
import com.nh.dsh.admin.common.cache.RedisKeys;
import com.nh.dsh.admin.security.user.ManagerDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class QuartzTaskStoreCache {
    private final RedisCache redisCache;

    /**
     * 设置定时器结束标志
     * @param key
     * @param value
     */
    public void saveTaskFlag(String key, boolean value) {
        if (redisCache.get(key) != null) {
            redisCache.delete(String.valueOf(redisCache.get(key)));
        }
        redisCache.set(key, value);
    }

    public boolean getTaskFlag(String key) {
        Object result = redisCache.get(key);
        if (result == null) {
            return false;
        } else {
            return Boolean.parseBoolean(result.toString());
        }
    }

    public void deleteTaskFlag(String key) {
        redisCache.delete(key);
    }
}
