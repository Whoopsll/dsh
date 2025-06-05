package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:38
 **/

@Data
@Schema(description = "分类编辑dto")
public class CategoryDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "分类")
    @NotBlank(message = "分类名不能为空")
    private String name;

    @Schema(description = "分类描述")
    @NotBlank(message = "分类描述不能为空")
    private String description;
    @Schema(description = "分类描述")
    private int parentId;
}
