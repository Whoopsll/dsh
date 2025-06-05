package com.nh.dsh.client.utils;

import com.alibaba.fastjson2.JSONObject;
import com.nh.dsh.client.common.exception.ServerException;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-08-28 17:26
 **/

public class BaiduAiUtil {
    public static Logger logger = org.slf4j.LoggerFactory.getLogger(BaiduAiUtil.class);
    public static final String API_KEY = "G5eTfwsnHtfq6dfvbrxQETVd";
    public static final String SECRET_KEY = "wbqmr2BYboBNZ82vGMc5GGGt17r8aqul";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String []args) {
        String text = "其他额外属性，比如人员名称等，如果通行失败，记录数据，比如二维码的原始值:{\"srcData\":\"1234567890\"}";
        System.out.println(checkTextInfo(text));
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    public static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        JSONObject resObj = JSONObject.parseObject(response.body().string());
        String accessToken = resObj.getString("access_token");
        if (StringUtils.isBlank(accessToken)) {
            logger.error("获取access_token失败");
            throw new ServerException("获取access_token失败");
        }
        return accessToken;
    }

    /**
     * 审核文本信息
     *
     * @return {@link String }
     */
    public static JSONObject checkTextInfo(String text)  {
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        try {
            String param = "text=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAccessToken();

            String result = HttpUtil.post(url, accessToken, param);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            logger.error("审核文本信息失败", e);
            return null;
        }
    }

    /**
     * 审核图片
     *
     * @return {@link String }
     */
    public static JSONObject checkImageInfo(String imgUrl) {
        String url = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        try {
            String imgParam = URLEncoder.encode(imgUrl, StandardCharsets.UTF_8);

            String param = "imgUrl=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAccessToken();

            String result = HttpUtil.post(url, accessToken, param);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            logger.error("审核文本信息失败", e);
            return null;
        }
    }

}
