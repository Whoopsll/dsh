package com.nh.dsh.client.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-07 16:22
 **/

@Data
@Schema(name = "用户信息vo")
public class UserInfoVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 8212240698099812005L;

    @Schema(name = "用户ID")
    private Integer id;

    @Schema(name = "微信OpenId")
    private String wxId;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "小程序的openId")
    private String openIdMp;

    @Schema(description = "unionId")
    private String unionId;
}

