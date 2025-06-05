package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description : 论坛实体
 * @create : 2024-06-19 15:49
 **/

@Data
@TableName("forum")
@Schema(description = "论坛对象")
public class ForumEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "论坛名称")
    private String name;

    @Schema(description = "课程链接")
    private String courseLink;

    @Schema(description = "书本id")
    private Integer bookId;
    @Schema(description = "第三方书本id")
    private String otherBookId;

    @Schema(description = "作者答缺省价")
    private Double price;

    @Schema(description = "作者简介")
    private String writerIntroduction;

    @Schema(description = "语音介绍url")
    private String voiceIntroduction;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "头像")
    private String avatar;

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
