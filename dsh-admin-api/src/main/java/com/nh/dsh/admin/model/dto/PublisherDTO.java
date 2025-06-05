package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:38
 **/

@Data
@Schema(description = "出版社编辑dto")
public class PublisherDTO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "出版社")
    @NotBlank(message = "出版社不能为空")
    private String name;

    @Schema(description = "描述")
    @NotBlank(message = "描述不能为空")
    private String description;
}
