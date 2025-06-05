package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@TableName("sys_content_audit")
@Schema(description = "内容审核表格")
@Data
public class ContentAuditEntity extends BaseEntity{
    @Schema(description = "审核名称")
    private String name;
    @Schema(description = "审核类型")
    private Integer type;
    @Schema(description = "文件链接")
    private String url;
    @Schema(description = "文件大小")
    private double fileSize;
    @Schema(description = "视频时长")
    private Integer duration;
    @Schema(description = "本地文件信息")
    private String localFileInfo;
    @Schema(description = "文件名")
    private String fileName;
    @Schema(description = "审核开始时间")
    private String auditStartTime;
    @Schema(description = "执行结果")
    private Integer executeResult;
    @Schema(description = "执行状态")
    private Integer executeStatus;
    @Schema(description = "执行类型")
    private Integer executeType;
}
