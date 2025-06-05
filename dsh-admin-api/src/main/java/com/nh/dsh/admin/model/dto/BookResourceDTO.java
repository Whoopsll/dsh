package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:50
 **/

@Data
@Schema(description = "书本资源编辑dto")
public class BookResourceDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "书本id")
    @NotNull(message = "书本id不能为空")
    private Integer bookId;

    @Schema(description = "用户id")
    private Integer userId;

    @Schema(description = "资源名")
    @NotBlank(message = "资源名不能为空")
    private String resourceName;

    @Schema(description = "链接")
    @NotBlank(message = "链接不能为空")
    private String link;

    private Integer enabled;
    private Integer type;
}
