package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-30 13:28
 **/

@Data
@TableName("sys_manager_publisher")
@Schema(description = "管理員出版社关联")
public class ManagerPublisherEntity {
    @Schema(description = "管理員id")
    private Integer managerId;
    @Schema(description = "出版社id")
    private Integer publisherId;
}
