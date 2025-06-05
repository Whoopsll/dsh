package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author Flobby
 */

@Data
@TableName("book_exchange")
@Schema(description = "书籍兑换记录实体")
public class BookExchangeEntity{
    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer bookId;
    private String verifyCode;
    private String userFlag;
    private Integer status;
    private String bookLink;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exchangeTime;

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