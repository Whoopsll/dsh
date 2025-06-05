package com.nh.dsh.client.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 11:40
 **/

@Data
public class ForumDetailVO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "论坛名称")
    private String name;

    @Schema(description = "书本id")
    private Integer bookId;

    @Schema(description = "书本名称")
    private String bookName;

    @Schema(description = "书本简介")
    private String bookIntroduction;

    @Schema(description = "作者")
    private String writer;

    @Schema(description = "分类")
    private String category;
    @Schema(description = "分类id")
    private int categoryId;

    @Schema(description = "作者简介")
    private String writerIntroduction;

    @Schema(description = "封面")
    private String cover;
    @Schema(description = "出版社")
    private String publisherName;
    @Schema(description = "出版时间")
    @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
    private Date publisherTime;
    @Schema(description = "电子书价格")
    private double price;
    @Schema(description = "ISBN")
    private String isbn;
    @Schema(description = "资源链接")
    private String resourceUrl;
    private long followCount;
    private long commentCount;
    private long likeCount;

    private boolean isFollow;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
