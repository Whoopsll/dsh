package com.nh.dsh.admin.model.query;

import com.nh.dsh.admin.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "角色查询")
public class SysRoleQuery extends Query {
    @Schema(description = "角色名称")
    private String name;

}
