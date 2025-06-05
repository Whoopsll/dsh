package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("t_config")
@Schema(description = "配置信息表")
public class ConfigEntity extends BaseEntity{

    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "配置类型：O: 公众号配置，1:其他配置")
    private Integer type;

    @Schema(description = "账号")
    private String account;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "出版社id")
    private int publisherId;
    @Schema(description = "公众号logo")
    private String logo;
    @Schema(description = "资源获取方式")
    private int resourceGetType;
}
