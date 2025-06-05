package com.nh.dsh.admin.common.config;

import com.nh.dsh.admin.utils.WxUtils;
import com.nh.dsh.admin.wechat.cache.AccessTokenCache;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanInitializer {

    @Autowired
    private AccessTokenCache tokenCache;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        WxUtils.init(tokenCache,restTemplate);
    }
}
