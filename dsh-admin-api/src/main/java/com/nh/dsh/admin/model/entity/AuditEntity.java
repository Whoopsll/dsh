package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : 书本表
 * @create : 2024-06-17 12:29
 **/

@Data
@TableName("audit")
@Schema(description = "审核管理")
public class AuditEntity extends BaseEntity {

    @Schema(description = "审核项名称")
    private String name;

    @Schema(description = "审核状态：O: 关闭，1:开启")
    private Integer status;

    @Schema(description = "审核分类")
    private Integer type;
    @Schema(description = "执行时间，0000100（周一到周日，0表示未启动，1表示启动）")
    private String executionTime;
    @Schema(description = "服务类型,0:正常服务，1:定时任务")
    private int serviceType;
    @Schema(description = "账号")
    private String account;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "服务地址")
    private String serviceAddress;
    @Schema(description = "邮箱地址")
    private String email;
}
