package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : 意见反馈
 * @create : 2024-06-19 09:46
 **/

@Data
@TableName("feedback")
@Schema(description = "意见反馈对象")
public class FeedbackEntity extends BaseEntity {

    @Schema(description = "内容")
    private String content;

    @Schema(description = "图片")
    private String img;

    @Schema(description = "用户id")
    private Integer userId;
}
