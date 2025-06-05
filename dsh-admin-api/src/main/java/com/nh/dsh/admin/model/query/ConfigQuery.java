package com.nh.dsh.admin.model.query;

import com.nh.dsh.admin.common.model.Query;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "配置服务查询")
public class ConfigQuery extends Query {

    @Schema(description = "服务名称")
    int publisherId;

}
