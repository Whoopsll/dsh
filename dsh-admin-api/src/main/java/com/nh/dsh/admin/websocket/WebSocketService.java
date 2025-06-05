package com.nh.dsh.admin.websocket;

import com.alibaba.fastjson.JSONObject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket 服务类
 */
@ServerEndpoint(value = "/websocket/{param}", encoders = {ServerEncoder.class})
@Component
public class WebSocketService {
    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);

    private static final ConcurrentHashMap<String, WebSocketClient> webSocketMap = new ConcurrentHashMap<>();
    private static final List<String> userNames = new CopyOnWriteArrayList<>();

    private Session session;
    private String userName;  // 记录当前 WebSocket 连接的用户名

    /**
     * 连接建立时触发
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("param") String param) {
        System.out.println("链接socket==="  + param);
        this.session = session;
        // 解析 param
        String[] params = param.split("&");
        if (params.length < 1) {
            log.error("WebSocket 连接失败，参数不正确: {}", param);
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭 WebSocket 失败", e);
            }
            return;
        }

        this.userName = params[0]; // 假设 param[0] 是用户名
        userNames.add(this.userName);
        // 存储连接信息
        WebSocketClient client = new WebSocketClient();
        client.setSession(session);
        client.setUri(session.getRequestURI().toString());
        webSocketMap.put(this.userName, client);

        log.info("用户 {} 连接 WebSocket，当前在线用户数: {}", this.userName, webSocketMap.size());
    }

    /**
     * 连接关闭时触发
     */
    @OnClose
    public void onClose() {
        if (this.userName != null) {
            webSocketMap.remove(this.userName);
            userNames.remove(this.userName);
            log.info("用户 {} 断开 WebSocket 连接，当前在线用户数: {}", this.userName, webSocketMap.size());
        }
    }

    /**
     * 发生错误时触发
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket 连接错误，用户: {}", this.userName, error);
    }

    /**
     * 接收客户端消息
     */
    @OnMessage
    public void onMsg(Session session, String message) {
        log.info("接收到用户 {} 的消息: {}", this.userName, message);
        try {
            JSONObject json = JSONObject.parseObject(message);
            String command = json.getString("command");

            // 根据不同指令执行不同操作
            switch (command) {
                case "PING":
                    sendMessage(this.userName, "PONG");
                    break;
                default:
                    log.warn("未知指令: {}", command);
            }
        } catch (Exception e) {
            log.error("解析消息失败", e);
        }
    }

    /**
     * 向指定用户发送 WebSocket 消息
     */
    public static void sendMessage(String userName, String message) {
        try {
            WebSocketClient webSocketClient = webSocketMap.get(userName);
            if (webSocketClient != null && webSocketClient.getSession().isOpen()) {
                log.info("向用户 {} 发送消息: {}", userName, message);
                webSocketClient.getSession().getBasicRemote().sendText(message);
            } else {
                log.warn("发送消息失败，用户 {} 不在线或连接已关闭", userName);
            }
        } catch (IOException e) {
            log.error("发送 WebSocket 消息失败", e);
        }
    }

    /**
     * 向所有在线用户发送广播消息
     */
    public static void broadcast(String message) {
        System.out.println("websocket发送信息===");
        for (String userName : webSocketMap.keySet()) {
            sendMessage(userName, message);
        }
    }
}
