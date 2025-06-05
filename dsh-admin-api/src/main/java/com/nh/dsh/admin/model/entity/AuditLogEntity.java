package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("sys_audit_log")
@Schema(description = "审核日志")
@Data
public class AuditLogEntity extends BaseEntity{
    @Schema(description = "审核id")
    private Integer auditId;
    @Schema(description = "日志内容")
    private String content;
    @Schema(description = "日志执行状态")
    private Integer status;
    @Schema(description = "日志开始时间")
    private LocalDateTime startTime;
}
