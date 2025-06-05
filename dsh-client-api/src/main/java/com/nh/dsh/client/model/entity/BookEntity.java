package com.nh.dsh.client.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description : 书本表
 * @create : 2024-06-17 12:29
 **/

@Data
@TableName("book")
@Schema(description = "书籍对象")
public class BookEntity {

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "书名")
    private String bookName;

    @Schema(description = "isbn")
    private String isbn;

    @Schema(description = "分类")
    private Integer classificationId;

    @Schema(description = "出版社")
    private Integer publisherId;

    /**
     * 逗号分割
     */
    @Schema(description = "译者")
    private String translator;

    /**
     * 逗号分割
     */
    @Schema(description = "作者")
    private String writer;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "页数")
    private Integer pageNumber;

    /**
     * 平装、精装等
     */
    @Schema(description = "装帧")
    private String bookStyle;

    @Schema(description = "发表时间")
    private Date publishDate;

    @Schema(description = "作者简介")
    private String authorIntroduction;

    @Schema(description = "书本简介")
    private String bookIntroduction;

    @Schema(description = "论坛链接")
    private String forumLink;

    @Schema(description = "书本封面")
    private String bookCover;
    @Schema(description = "微信公众号配置id")
    private Integer wxConfigId;

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
