package com.nh.dsh.admin.model.query;


import lombok.EqualsAndHashCode;
import com.nh.dsh.admin.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "字典查询")
public class DictQuery extends Query {
    @Schema(description = "字典名称")
    private String title;
}
