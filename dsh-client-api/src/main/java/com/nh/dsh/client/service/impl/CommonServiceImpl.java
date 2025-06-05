package com.nh.dsh.client.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.nh.dsh.client.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Flobby
 */
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {

    final static String APP_ID = "wx731630b450248376";
    final static String APP_SECRET = "65f6d70befb283d461486a63922652e6";

    @Override
    public JSONObject wxLogin(String code) {
        String urlForAK = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + APP_ID +
                "&secret=" + APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String accessData = restTemplate.getForObject(urlForAK, String.class);
        log.info("accessData: {}", accessData);
        JSONObject accessObj = JSONObject.parseObject(accessData);
        String openid = accessObj.getString("openid");
        String accessToken = accessObj.getString("access_token");
        String urlForUserInfo = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + accessToken +
                "&openid=" + openid +
                "&lang=zh_CN";
        String userInfo = restTemplate.getForObject(urlForUserInfo, String.class);
        log.info("userInfo: {}", userInfo);
        return JSON.parseObject(userInfo);
    }
}