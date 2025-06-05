package com.nh.dsh.client.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxInfoVo {
    @Schema(description = "微信小程序的openId")
    private String wxOpenId;
    @Schema(description = "微信小程序的unionId")
    private String unionId;
    @Schema(description = "微信小程序的sessionKey")
    private String sessionKey;
}
