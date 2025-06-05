package com.nh.dsh.admin.utils;

import com.nh.dsh.admin.common.constant.Constant;
import com.nh.dsh.admin.model.entity.BookResourceEntity;
import com.nh.dsh.admin.security.cache.QuartzTaskStoreCache;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class HttpUtils {
    // 定义线程池
    private static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    private static QuartzTaskStoreCache taskStoreCache;
    private static RestTemplate restTemplate;
    private static HttpEntity<String> entity;

    public static List<Map> batchPing(List<BookResourceEntity> ips, QuartzTaskStoreCache cache){
        taskStoreCache = cache;
        if (restTemplate == null) {
            restTemplate = getRestTemplate();
        }
        AtomicBoolean stopFlag = new AtomicBoolean(false);
        // 开始并发测试
        List<CompletableFuture<Map>> futures = ips.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> testUrl(item, stopFlag), EXECUTOR))
                .collect(Collectors.toList());

        // 等待所有任务完成，并收集结果
        return futures.stream()
                .map(CompletableFuture::join) // 等待每个 CompletableFuture 完成
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Map testUrl(BookResourceEntity resourceEntity, AtomicBoolean stopFlag) {
        Map map = new HashMap();
        try {
            boolean taskFlag = taskStoreCache.getTaskFlag(Constant.AUDIT_TASK_IS_STOP_KEY);
            if(taskFlag) {
                //如果此时为停止，则返回null
                return null;
            }
            URI uri = UriComponentsBuilder.fromHttpUrl(resourceEntity.getLink()).build().toUri();
            if (resourceEntity.getLink().contains("baidu")) {
                System.out.println(uri.toURL());
            }
            ResponseEntity<String> response = restTemplate.postForEntity(uri,entity, String.class);
            // 检查状态码是否是2xx的成功状态
            // if (!response.getStatusCode().is2xxSuccessful()) {
            //     map.put("id", resourceEntity.getId());
            //     map.put("result", 1);//有异常
            //     map.put("reason", "网址无法访问");//异常原因
            //     map.put("isCanAccess", 0);//
            //     map.put("time", LocalDateTime.now());//审核时间
            //     return map;
            // }
            map.put("id", resourceEntity.getId());
            map.put("result", 0);//有异常
            map.put("reason", "");//异常原因
            map.put("isCanAccess", 1);//是否阻断
            map.put("time", LocalDateTime.now());//审核时间
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("id", resourceEntity.getId());
            map.put("result", 1);//有异常
            map.put("reason", "网址无法访问");//异常原因
            map.put("isCanAccess", 0);//
            map.put("time", LocalDateTime.now());//审核时间
            return map;
        }
    }

    @NotNull
    @Contract(" -> new")
    private static RestTemplate getRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(4000);
        factory.setConnectTimeout(4000);
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.set("Connection", "keep-alive");
        headers.set("Cache-Control", "max-age=0");
        entity = new HttpEntity<>(headers);
        return new RestTemplate(factory);
    }

    public static File downloadFile(String fileUrl, String savePath) throws Exception {
        restTemplate = getRestTemplate();
        // 发送 HTTP 请求获取文件流
        byte[] fileBytes = restTemplate.getForObject(fileUrl, byte[].class);
        if (fileBytes == null) {
            throw new RuntimeException("无法下载文件：" + fileUrl);
        }

        // 保存到本地
        File file = new File(savePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        }
        return file;
    }
}
