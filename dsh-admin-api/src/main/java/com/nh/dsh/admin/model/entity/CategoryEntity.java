package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : 分类
 * @create : 2024-06-17 14:35
 **/

@Data
@TableName("category")
@Schema(description = "分类对象")
public class CategoryEntity extends BaseEntity{

    @Schema(description = "分类")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "父类ID")
    private int parentId;
}
