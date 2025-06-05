package com.nh.dsh.admin.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.dsh.admin.enums.ResourceTypeEnum;
import com.nh.dsh.admin.wechat.cache.AccessTokenCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.math3.geometry.partitioning.Region.Location.BOUNDARY;

public class WxUtils {

    private static int resourceId = 0;
    private static WxUtils instance;
    private static AccessTokenCache tokenCache;
    private static RestTemplate restTemplate;

    public static void init(AccessTokenCache accessTokenCache,RestTemplate restTemplate1) {
        if (tokenCache == null) {
            tokenCache = accessTokenCache;
        }
        if (restTemplate == null) {
            restTemplate = restTemplate1;
        }
    }

    /**
     * 获取微信公众号链接
     * @param type
     * @return
     */
    public static String getWxLink(String type, int id, String key,String secret,int configId) {
        try {

            String  accessToken = getAccessToken(key,secret,configId);
            String url = String.format("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s", accessToken);
            // 构造请求头
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            // 构造 Java 对象
            Map<String, Object> requestBodyMap = new HashMap<>();
            requestBodyMap.put("action_name", "QR_LIMIT_STR_SCENE");
            Map<String, Object> sceneMap = new HashMap<>();
            sceneMap.put("scene_str", type + "_" + id);
            Map<String, Object> actionInfoMap = new HashMap<>();
            actionInfoMap.put("scene", sceneMap);
            requestBodyMap.put("action_info", actionInfoMap);
            // 转换为 JSON 字符串
            String requestBody = new ObjectMapper().writeValueAsString(requestBodyMap);
            HttpEntity entity = new HttpEntity(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            System.out.println("getWxLink===" + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject result = JSON.parseObject(response.getBody());
                String wxLink = result.getString("url");
                if (wxLink != null) {
                    return wxLink;
                }
                return null;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取accessToken
     * @param key
     * @param secret
     * @return
     */
    public static String getAccessToken(String key, String secret, int configId) {
        try {
            String accessToken = tokenCache.getAccessToken(configId);
            if(StringUtils.isNotBlank(accessToken)) {
                accessToken = accessToken.replaceAll("\"", "");
                return accessToken;
            }
            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", key, secret);
            URI uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri();
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                JSONObject result = JSON.parseObject(response.getBody());
                accessToken = result.getString("access_token");
                if (accessToken != null) {
                    tokenCache.saveToken(accessToken,configId);
                    return accessToken;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 获取自定义菜单
     * @param key
     * @param secret
     * @return
     */
    public static JSONObject getCustomMenu(String key, String secret, int configId) {
        String accessToken = getAccessToken(key, secret, configId);
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/get?access_token=%s",accessToken);
        String uri = UriComponentsBuilder.fromHttpUrl(url).build().toUriString();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject result = JSON.parseObject(response.getBody());
            return result;
        }
        return null;
    }

    /**
     * 创建菜单
     * @param configId 获取access_token
     * @param key appid
     * @param secret appsecret
     * @param body 参数体
     * @return
     */
    public static boolean createCustomMenu(int configId, String key,String secret, String body) {
        String accessToken = getAccessToken(key, secret, configId);
        String url = String.format("https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s",accessToken);
        String uri = UriComponentsBuilder.fromHttpUrl(url).build().toUriString();
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 封装请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject result = JSON.parseObject(response.getBody());
            Integer errcode = result.getInteger("errcode");
            if (errcode == 0) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static String uploadImageAssets(String key,String secret,int configId, MultipartFile file) {
        try {
            String accessToken = getAccessToken(key,secret,configId);
            // 微信素材上传 URL
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=image";
            // 创建请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Spring 会自动处理边界和Content-Type

            // 创建表单数据
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("type", "image");

            // 计算 Content-Length
            byte[] requestBody = createMultipartBody(file); // 创建请求体
            headers.setContentLength(requestBody.length); // 设置 Content-Length

            // 创建 HttpEntity 实例（包括头和表单数据）
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(requestBody, headers);

            // 发送 POST 请求并接收响应
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);

            // 输出响应结果
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
               return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 计算 multipart 请求体的内容（包括文件和字段）
    private static byte[] createMultipartBody(MultipartFile multipartFile) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 写入文本字段（type）
            writeField(outputStream, "type", "image");

            // 写入文件字段（media）
            writeFile(outputStream, "media", multipartFile);

            // 写入结束边界
            String boundaryEnd = "--" + BOUNDARY + "--\r\n";
            outputStream.write(boundaryEnd.getBytes(StandardCharsets.UTF_8));

            // 返回请求体字节数组
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error creating multipart body", e);
        }
    }

    // 写入字段
    private static void writeField(ByteArrayOutputStream outputStream, String name, String value) throws IOException {
        String partHeader = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"\r\n" +
                "\r\n" + value + "\r\n";
        outputStream.write(partHeader.getBytes(StandardCharsets.UTF_8));
    }

    // 写入文件
    private static void writeFile(ByteArrayOutputStream outputStream, String name, MultipartFile file) throws IOException {
        String partHeader = "--" + BOUNDARY + "\r\n" +
                "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + file.getOriginalFilename() + "\"\r\n" +
                "Content-Type: " + file.getContentType() + "\r\n" +
                "\r\n";
        outputStream.write(partHeader.getBytes(StandardCharsets.UTF_8));

        // 写入文件内容
        byte[] buffer = new byte[1024];
        int bytesRead;
        try (InputStream inputStream = file.getInputStream()) {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        outputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
    }

    public static String getWxImageAssetsById(int configId, String key,String secret, String mediaId) {
        String accessToken = getAccessToken(key, secret, configId);
        String url = String.format("https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=%s",accessToken);
        String uri = UriComponentsBuilder.fromHttpUrl(url).build().toUriString();
        // 创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.put("media_id", mediaId);
        // 封装请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(body.toJSONString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, requestEntity, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject result = JSON.parseObject(response.getBody());
            System.out.println(result);
        }
        return null;
    }
}
