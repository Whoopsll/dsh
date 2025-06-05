package com.nh.dsh.admin.model.vo;

import com.nh.dsh.admin.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CategoryTreeNode extends BaseEntity {

    @Schema(description = "分类")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "父类ID")
    private int parentId;
    private List<CategoryTreeNode> children;
}
