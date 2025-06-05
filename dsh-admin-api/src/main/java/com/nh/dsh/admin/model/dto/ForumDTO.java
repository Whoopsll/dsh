package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 15:49
 **/

@Data
@Schema(description = "论坛信息编辑dto")
public class ForumDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "用户id")
    @NotNull(message = "用户必须选择")
    private Integer userId;

    @Schema(description = "论坛名称")
    @NotBlank(message = "论坛名不能为空")
    private String name;

    @Schema(description = "课程链接")
    private String courseLink;

    @Schema(description = "书本id")
    @NotNull(message = "书籍必须选择")
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
}
