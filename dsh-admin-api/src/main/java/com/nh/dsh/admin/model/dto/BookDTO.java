package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description : dto
 * @create : 2024-06-17 12:45
 **/

@Data
@Schema(description = "书本编辑dto")
public class BookDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "书名")
    @NotBlank(message = "书名不能为空")
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
    @NotBlank(message = "作者不能为空")
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
    @NotBlank(message = "书本封面不能为空")
    private String bookCover;
    @Schema(description = "微信公众号的ID")
    private int wxConfigId;
}
