package com.nh.dsh.client.wechat.service;

import com.nh.dsh.client.wechat.model.WeChatConfig;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeChatJsSdkService {
    @Resource
    private WeChatConfig weChatConfig;
    @Resource
    private RestTemplate restTemplate;
    private Map<String,String> cache = new ConcurrentHashMap<>();

    // 获取 Access Token
    public String getAccessToken() {
        if (cache.containsKey("access_token")) {
            return cache.get("access_token");
        }

        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                weChatConfig.getAppId(), weChatConfig.getAppSecret()
        );

        Map<String, String> response = restTemplate.getForObject(url, Map.class);
        String accessToken = response.get("access_token");

        // 缓存 Access Token，默认有效期为 2 小时
        cache.put("access_token", accessToken);
        return accessToken;
    }

    // 获取 JSAPI Ticket
    public String getJsapiTicket() {
        if (cache.containsKey("jsapi_ticket")) {
            return cache.get("jsapi_ticket");
        }

        String accessToken = getAccessToken();
        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi",
                accessToken
        );

        Map<String, String> response = restTemplate.getForObject(url, Map.class);
        String jsapiTicket = response.get("ticket");

        // 缓存 JSAPI Ticket，默认有效期为 2 小时
        cache.put("jsapi_ticket", jsapiTicket);
        return jsapiTicket;
    }

    // 生成签名
    public Map<String, String> generateSignature(String url) {
        String jsapiTicket = getJsapiTicket();
        String nonceStr = generateNonceStr();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        String stringToSign = "jsapi_ticket=" + jsapiTicket +
                "&noncestr=" + nonceStr +
                "&timestamp=" + timestamp +
                "&url=" + url;

        String signature = sha1(stringToSign);

        Map<String, String> result = new HashMap<>();
        result.put("appId", weChatConfig.getAppId());
        result.put("nonceStr", nonceStr);
        result.put("timestamp", timestamp);
        result.put("signature", signature);
        return result;
    }

    // 生成随机字符串
    private String generateNonceStr() {
        return Long.toHexString(System.nanoTime());
    }

    // SHA1 加密
    private String sha1(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA1 加密失败", e);
        }
    }

}
