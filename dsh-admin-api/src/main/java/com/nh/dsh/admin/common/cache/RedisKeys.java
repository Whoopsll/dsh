package com.nh.dsh.admin.common.cache;

public class RedisKeys {

    /**
     * 验证码Key
     */
    public static String getCaptchaKey(String key) {
        return "sys:captcha:" + key;
    }

    /**
     * accessToken Key
     */
    public static String getAccessTokenKey(String accessToken) {
        return "sys:access:" + accessToken;
    }

    public static String getManagerIdKey(Integer id) {
        return "sys:manager:" + id;
    }

    /**
     * 微信access_token
     * @return
     */
    public static String getWxAccessToken(Integer configId) {
        return "wx:access_token:" + configId;
    }

}
