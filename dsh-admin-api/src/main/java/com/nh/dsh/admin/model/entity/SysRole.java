package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName("sys_role")
public class SysRole {

    /**
     * pkId
     */
    @TableId(value = "pk_id", type = IdType.AUTO)
    private Integer pkId;

    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标识  0：正常   1：已删除
     */
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;


    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
