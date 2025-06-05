package com.nh.dsh.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:50
 **/

@Data
@Schema(description = "书本资源vo")
public class BookResourceVO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "书本id")
    private Integer bookId;

    @Schema(description = "资源名")
    private String resourceName;

    @Schema(description = "链接")
    private String link;

    private Integer enabled;

    private String secret;
    private String qrLink;
}
