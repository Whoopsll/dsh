package com.nh.dsh.admin.wechat.service;

import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.wechat.model.entity.WxMenuModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface WechatService {

    /**
     * 处理来自微信公众号的消息
     * @param request
     * @return
     */
    String handleMessage(HttpServletRequest request);



    Result uploadImage(int configId, MultipartFile file);
}
