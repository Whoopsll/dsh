package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:12
 **/

@Data
@Schema(description = "公告编辑dto")
public class NoticeDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "圈子id")
    @NotNull
    private Integer forumId;

    @Schema(description = "发布管理员id")
    private Integer adminId;

    @Schema(description = "标题")
    @NotBlank
    private String title;

    @Schema(description = "内容")
    @NotBlank
    private String content;

    @Schema(description = "生效时间")
    private Date validTime;

    /**
     * 0-不置顶，1,-置顶
     */
    @Schema(description = "是否置顶")
    @NotNull
    private Integer isTop;
}
