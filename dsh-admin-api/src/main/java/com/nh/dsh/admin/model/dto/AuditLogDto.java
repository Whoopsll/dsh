package com.nh.dsh.admin.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nh.dsh.admin.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "审核日志")
@Data
public class AuditLogDto {
    @Schema(description = "主键")
    private Integer id;
    @Schema(description = "审核id")
    private Integer auditId;
    @Schema(description = "日志内容")
    private String content;
    @Schema(description = "日志执行状态")
    private Integer status;
    @Schema(description = "日志开始时间")
    private LocalDateTime startTime;
}
