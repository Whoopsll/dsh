package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 15:50
 **/

@Data
@Schema(description = "论坛视图vo")
public class ForumVO {

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "论坛名称")
    private String name;

    @Schema(description = "课程链接")
    private String courseLink;

    @Schema(description = "书本id")
    private Integer bookId;

    @Schema(description = "书本")
    private String bookName;

    @Schema(description = "类别")
    private String categoryName;

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

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Schema(description = "是否置顶")
    private Integer isTop;
}
