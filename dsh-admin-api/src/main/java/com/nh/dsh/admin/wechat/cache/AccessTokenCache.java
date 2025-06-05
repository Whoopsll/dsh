package com.nh.dsh.admin.wechat.cache;

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
public class AccessTokenCache {
    private final RedisCache redisCache;

    /**
     * 存储微信access_token
     * @param accessToken
     */
    public void saveToken(String accessToken,int configId) {
        String key = RedisKeys.getWxAccessToken(configId);
        if(redisCache.get(key) == null) {
            // 若access_token未过期，不重新存储
            redisCache.set(key, accessToken, RedisCache.HOUR_TWO_EXPIRE);
        }
    }

    /**
     * 获取微信access_token
     * @return
     */
    public String getAccessToken(int configId) {
        String key = RedisKeys.getWxAccessToken(configId);
        return JSON.to(String.class, redisCache.get(key));
    }
}
