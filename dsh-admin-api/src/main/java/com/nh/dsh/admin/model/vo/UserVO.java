package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 15:12
 **/

@Data
@Schema(description = "用户视图")
public class UserVO {
    @Schema(description = "主键")
    private Integer id;
    @Schema(description = "openId")
    private String wxId;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "微信昵称")
    private String wxName;
    @Schema(description = "小程序openId")
    private String openIdMp;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
