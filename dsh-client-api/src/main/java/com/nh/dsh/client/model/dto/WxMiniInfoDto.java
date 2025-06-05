package com.nh.dsh.client.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxMiniInfoDto {
    @Schema(description = "微信小程序的openId")
    private String wxOpenId;
    @Schema(description = "微信小程序的unionId")
    private String unionId;
    @Schema(description = "微信小程序的sessionKey")
    private String sessionKey;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "头像")
    private String avatar;
}
