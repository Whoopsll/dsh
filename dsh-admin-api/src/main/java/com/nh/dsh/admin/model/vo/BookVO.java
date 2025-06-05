package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 12:51
 **/

@Data
@Schema(description = "book视图")
public class BookVO {

    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "书名")
    private String bookName;

    @Schema(description = "isbn")
    private String isbn;

    @Schema(description = "分类")
    private String classification;
    private Integer classificationId;

    @Schema(description = "出版社")
    private String publishingHouse;
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishDate;

    @Schema(description = "作者简介")
    private String authorIntroduction;

    @Schema(description = "书本简介")
    private String bookIntroduction;

    @Schema(description = "书本封面")
    private String bookCover;

    @Schema(description = "论坛链接")
    private String forumLink;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Schema(description = "微信公众号的ID")
    private int wxConfigId;
    private String qrLink;
    @Schema(description = "是否置顶")
    private Integer isTop;
}
