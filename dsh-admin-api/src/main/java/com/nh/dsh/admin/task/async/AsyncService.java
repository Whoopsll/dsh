package com.nh.dsh.admin.task.async;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.nh.dsh.admin.common.constant.PythonAuditInterface;
import com.nh.dsh.admin.enums.AuditTypeEnum;
import com.nh.dsh.admin.mapper.AuditLogMapper;
import com.nh.dsh.admin.mapper.ContentAuditMapper;
import com.nh.dsh.admin.model.entity.AuditLogEntity;
import com.nh.dsh.admin.model.entity.ContentAuditEntity;
import com.nh.dsh.admin.utils.HttpUtils;
import com.nh.dsh.admin.websocket.WebSocketMessageVo;
import com.nh.dsh.admin.websocket.WebSocketService;
import io.swagger.v3.oas.annotations.Webhook;
import jakarta.annotation.Resource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AsyncService {
    @Resource
    ContentAuditMapper contentAuditMapper;
    @Resource
    AuditLogMapper auditLogMapper;

    @Async
    public void asyncAuditTask(int taskId, Integer userId) {
        System.out.println("用户ID===" + userId);
        ContentAuditEntity contentAuditEntity = contentAuditMapper.selectById(taskId);
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setStartTime(LocalDateTime.now());
        auditLogEntity.setAuditId(taskId);
        String requestUrl = "";
        WebSocketMessageVo messageVo = new WebSocketMessageVo();
        int type = contentAuditEntity.getType();
        if(type == AuditTypeEnum.BOOK_TEXT.getValue()) {
            //图书文字审核
            requestUrl = PythonAuditInterface.BOOK_TEXT_API;
        } else if(type == AuditTypeEnum.BOOK_PICTURE.getValue()) {
            requestUrl = PythonAuditInterface.BOOK_PICTURE__API;
        }
        //离线视频
        if(type == AuditTypeEnum.OFFLINE_VIDEO.getValue()) {
            requestUrl = PythonAuditInterface.OFFLINE_VIDEO__API;
        }
        //短视频
        if(type == AuditTypeEnum.SHORT_VIDEO.getValue()) {
            requestUrl = PythonAuditInterface.SHORT_VIDEO__API;
        }
        File file = null;
        String errorLog = "";
        try {
            String url = contentAuditEntity.getUrl();
            String projectRoot = System.getProperty("user.dir");
            String suffix = url.substring(url.lastIndexOf(".") + 1);
            //异步执行审核任务
            file = HttpUtils.downloadFile(url, projectRoot + "/task_" + taskId + "." + suffix);
            // 1. 读取本地文件
            FileSystemResource resource = new FileSystemResource(file);
            // 2. 构造请求体
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", resource);
            // 3. 设置请求头（Content-Type）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            // 4. 封装 HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            // 5. 发送 POST 请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
            String html = response.getBody();
            //记录下审核返回的日志
            errorLog = html;
            //转json
            System.out.println("log====" + html);
            Gson gson = new Gson();
            Map map = new HashMap();
            try {
                map = gson.fromJson(html, Map.class);
            } catch (Exception e) {

            }
            if (map.get("审核日志") == null) {
                auditLogEntity.setContent(html);
            } else {
                String log = map.get("审核日志").toString();
                if(log.contains(".pdf")) {
                    log = contentAuditEntity.getName() + "审核报告" + log.substring(log.lastIndexOf(".pdf"));
                }

                auditLogEntity.setContent(log);
            }
            //更新计划状态和结果
            contentAuditEntity.setExecuteResult(1);
        } catch (Exception e) {
            e.printStackTrace();
            auditLogEntity.setContent("文件审核异常");
            contentAuditEntity.setExecuteResult(1);
        } finally {
            contentAuditEntity.setExecuteStatus(2);
            auditLogEntity.setStatus(2);
            auditLogEntity.setDeleteFlag(0);
            auditLogMapper.insert(auditLogEntity);
            contentAuditMapper.updateById(contentAuditEntity);
            messageVo.setResult(contentAuditEntity.getExecuteResult());
            messageVo.setTaskId(taskId);
            messageVo.setUserId(userId);
            messageVo.setStatus(2);
            Gson gson = new Gson();
            WebSocketService.broadcast(gson.toJson(messageVo));
            if (file != null) {
                file.delete();
            }
        }
    }

}
