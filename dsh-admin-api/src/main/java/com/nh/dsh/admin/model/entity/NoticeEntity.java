package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:09
 **/

@Data
@TableName("notice")
@Schema(description = "公告对象")
public class NoticeEntity extends BaseEntity {

    @Schema(description = "圈子id")
    private Integer forumId;

    @Schema(description = "发布管理员id")
    private Integer adminId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "生效时间")
    private Date validTime;

    @Schema(description = "是否置顶")
    private Integer isTop;
}
