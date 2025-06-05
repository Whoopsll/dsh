package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuditDto {

    @Schema(name = "id")
    private Integer id;

    @Schema(name = "审核名称")
    @NotBlank
    private String name;

    @Schema(name = "审核类型")
    @NotBlank
    private Integer type;

    @Schema(name = "运行状态,0:关闭，1:开启")
    private Integer status;
    @Schema(name = "服务类型")
    private Integer serviceType;
    @Schema(name = "定时任务时间")
    private String executionTime;
    @Schema(description = "账号")
    private String account;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "服务地址")
    private String serviceAddress;
    @Schema(description = "邮箱地址")
    private String email;
}
