package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 09:55
 **/

@Data
public class FeedbackVO {

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "图片")
    private String img;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "昵称")
    private String username;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
