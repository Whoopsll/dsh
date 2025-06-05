package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : 论坛实体
 * @create : 2024-06-19 15:49
 **/

@Data
@TableName("forum")
@Schema(description = "论坛对象")
public class ForumEntity extends BaseEntity {

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "论坛名称")
    private String name;

    @Schema(description = "课程链接")
    private String courseLink;

    @Schema(description = "书本id")
    private Integer bookId;

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
    @Schema(description = "第三方书籍id")
    private String otherBookId;
    @Schema(description = "是否置顶:0-不置顶，1-置顶")
    private Integer isTop;
}
