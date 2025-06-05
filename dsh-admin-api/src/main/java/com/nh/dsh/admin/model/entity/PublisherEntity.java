package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : 出版社
 * @create : 2024-06-29 15:35
 **/
@Data
@TableName("publisher")
@Schema(description = "出版社")
public class PublisherEntity extends BaseEntity {

    @Schema(description = "分类")
    private String name;

    @Schema(description = "描述")
    private String description;
}
