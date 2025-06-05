package com.nh.dsh.client.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 11:36
 **/

@Data
public class ForumListItemVO {

    @Schema(description = "主键")
    private Integer id;
    @Schema(description = "论坛名称")
    private String name;
    @Schema(description = "封面")
    private String cover;
    @Schema(description = "作者")
    private String author;
    @Schema(description = "描述")
    private String bookDescription;
    @Schema(description = "分类")
    private String type;
    @Schema(description = "是否关注")
    private boolean isFollow;
    @Schema(description = "评论数")
    private int commentCount;

    private long followCount;
}
