package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description : 用户
 * @create : 2024-06-18 15:05
 **/

@Data
@TableName("wuser")
@Schema(description = "用户")
public class UserEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "微信昵称")
    private String wxName;

    @Schema(description = "openid")
    private String wxId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "open_id_mp")
    private String openIdMp;

    @Schema(description = "union_id")
    private String unionId;

    @Schema(description = "0:未删除  1:已删除")
    @TableLogic
    @JsonIgnore
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleteFlag;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
