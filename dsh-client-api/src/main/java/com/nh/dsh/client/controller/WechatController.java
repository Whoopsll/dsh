package com.nh.dsh.client.controller;

import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.wechat.service.WeChatJsSdkService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
@AllArgsConstructor
public class WechatController {
    private final WeChatJsSdkService weChatJsSdkService;

    @PostMapping("/token")
    public Result getToken(@RequestParam("url") String url) {
        return Result.ok(weChatJsSdkService.generateSignature(url));
    }
}
