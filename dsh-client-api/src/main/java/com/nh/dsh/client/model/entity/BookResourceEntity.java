package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-06-18 09:45
 **/

@Data
@TableName("book_resource")
@Schema(description = "书籍资源对象")
public class BookResourceEntity {
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "书本id")
    private Integer bookId;
    @Schema(description = "第三方书本id")
    private String otherBookId;
    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "资源名")
    private String resourceName;

    @Schema(description = "链接")
    private String link;

    @Schema(description = "二维码")
    private String codeCover;

    private Integer validStatus;

    private int canAccess;
    /**
     * 异常原因
     */
    private String errorReason;
    /**
     * 审核结果：0-正常，1-异常
     */
    private int auditResult;
    @Schema(description = "资源类型,0:正常资源， 1:第三方资源，2:资源总纲地址")
    private int type;

    @Schema(description = "0:未删除  1:已删除")
    @TableLogic
    @JsonIgnore
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    private Integer deleteFlag;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
