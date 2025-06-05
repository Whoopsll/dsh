package com.nh.dsh.admin.wechat.controller;

import com.itextpdf.text.Element;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.websocket.WebSocketService;
import com.nh.dsh.admin.wechat.service.WechatService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WechatController {
    private static final Logger log = LoggerFactory.getLogger(WebSocketService.class);
    private static final String TOKEN = "wangfeng888";
    @Resource
    WechatService wechatService;

    // 处理微信服务器的验证请求
    @GetMapping
    public String verifySignature(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {

        // 验证签名
        if (checkSignature(signature, timestamp, nonce)) {
            log.info("验证通过===========");
            return echostr; // 验证通过，返回 echostr
        }
        log.info("验证error===========");
        return "error";
    }

    @PostMapping("/callback")
    public String handleWeChatEvent(@RequestBody String requestBody) {
        // 解析 XML 消息
        try {
            System.out.println(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    // 处理消息和事件的 POST 请求
    @PostMapping
    public String handleMessage(HttpServletRequest request)  {
        log.info("收到消息===========");
        log.info(wechatService.handleMessage(request));
        return wechatService.handleMessage(request);
    }

    // 签名校验方法
    private boolean checkSignature(String signature, String timestamp, String nonce) {
        try {
            String[] params = new String[] { TOKEN, timestamp, nonce };
            Arrays.sort(params); // 字典排序
            String concatenated = String.join("", params);
            String hashed = DigestUtils.sha1Hex(concatenated); // SHA1 加密
            System.out.println("校验完成=====" + signature);
            return hashed.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    @Schema(name = "上传图片到素材管理")
    @PostMapping("/uploadResource/{id}")
    Result uploadImage(@PathVariable("id") int config, @RequestParam("file")MultipartFile file) {
        return wechatService.uploadImage(config, file);
    }
}
