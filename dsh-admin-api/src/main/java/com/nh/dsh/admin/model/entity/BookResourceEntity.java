package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:45
 **/

@Data
@TableName("book_resource")
@Schema(description = "书籍资源对象")
public class BookResourceEntity extends BaseEntity {
    @Schema(description = "书本id")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private Integer bookId;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "资源名")
    private String resourceName;

    @Schema(description = "链接")
    private String link;

    @Schema(description = "二维码")
    private String codeCover;

    /**
     * 0：已启用
     * 1: 未启用
     */
    private Integer enabled;
    /**
     * 0：正常
     * 1: 非法
     */
    private Integer validStatus;
    /**
     * 0: 不阻断访问
     * 1: 阻断访问
     */
    private int canAccess = 1;
    /**
     * 异常原因
     */
    private String errorReason;
    /**
     * 审核结果：0-正常，1-异常
     */
    private int auditResult;
    @Schema(description = "资源类型")
    private int type;
    @Schema(description = "第三方书籍id")
    private String otherBookId;
}
