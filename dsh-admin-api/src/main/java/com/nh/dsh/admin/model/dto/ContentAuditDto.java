package com.nh.dsh.admin.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.nh.dsh.admin.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "内容审核表")
@Data
public class ContentAuditDto {
    private Integer id;
    @NotBlank
    @Schema(description = "审核名称")
    private String name;
    @NotNull
    @Schema(description = "审核类型")
    private Integer type;
    @Schema(description = "文件链接")
    private String url;
    @Schema(description = "文件大小")
    private double fileSize;
    @Schema(description = "本地文件信息")
    private String localFileInfo;
    @Schema(description = "文件名")
    private String fileName;
    @Schema(description = "审核定时开始时间")
    private String auditStartTime;
    @Schema(description = "执行类型")
    private Integer executeType;
}
