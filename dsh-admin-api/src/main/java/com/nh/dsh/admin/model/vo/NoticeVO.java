package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:13
 **/

@Data
@Schema(description = "公告管理页面vo")
public class NoticeVO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "圈子id")
    private Integer forumId;

    @Schema(description = "圈子")
    private String forumName;

    @Schema(description = "发布管理员id")
    private Integer adminId;

    @Schema(description = "发布管理员")
    private String adminName;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "生效时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date validTime;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
