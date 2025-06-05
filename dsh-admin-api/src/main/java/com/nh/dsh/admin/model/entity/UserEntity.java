package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : 用户
 * @create : 2024-06-18 15:05
 **/

@Data
@TableName("wuser")
@Schema(description = "用户")
public class UserEntity extends BaseEntity {

    @Schema(description = "h5的openid")
    private String wxId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "微信昵称")
    private String wxName;
    @Schema(description = "微信union_id")
    private String unionId;
    @Schema(description = "小程序openId")
    private String openIdMp;
}
