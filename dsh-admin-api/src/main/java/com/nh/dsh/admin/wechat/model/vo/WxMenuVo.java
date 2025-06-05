package com.nh.dsh.admin.wechat.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class WxMenuVo {
    private String name;
    private List<WxMenuVo> sub_button;
    private String type;
    private String key;
    private String pictureUrl;
    private String textContent;
    private String parentKey;
    private String url;
    private int replyType;
    private String mediaId = "";
}
