package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 10:25
 **/

@Data
@TableName(value = "comment", autoResultMap = true)
@Schema(description = "评论对象")
public class CommentEntity {
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "论坛id")
    private Integer forumId;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "父评论")
    private Integer parentId;

    /**
     *  0： 'question' 或1 ： 'answer'
     */
    @Schema(description = "类型")
    private Integer type;

    /**
     *  0： '图片' 或1 ： '语音'
     */
    @Schema(description = "文件类型")
    private Integer fileType;

    @Schema(description = "文件")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> files;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "0:未删除  1:已删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    @JsonIgnore
    private Integer deleteFlag;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
