package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description : 分类
 * @create : 2024-06-17 14:35
 **/

@Data
@TableName("category")
@Schema(description = "分类对象")
public class CategoryEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "分类")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "0:未删除  1:已删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    @JsonIgnore
    private Integer deleteFlag;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
