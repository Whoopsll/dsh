package com.nh.dsh.admin.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nh.dsh.admin.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Schema(description = "内容审核表格")
@Data
public class ContentAuditVo {
    private Integer id;
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
    @Schema(description = "审核开始时间")
    private String auditStartTime;
    @Schema(description = "本地文件信息")
    private String localFileInfo;
    @Schema(description = "文件名")
    private String fileName;
    @Schema(description = "执行结果")
    private Integer executeResult;
    @Schema(description = "执行状态")
    private Integer executeStatus;
    @Schema(description = "执行类型")
    private Integer executeType;
    @Schema(description = "日志")
    private String executeLog;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
