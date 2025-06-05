package com.nh.dsh.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 15:09
 **/

@Data
@Schema(description = "用户编辑dto")
public class UserDTO {
    @Schema(description = "主键")
    private Integer id;
    @Schema(description = "微信昵称")
    @NotBlank(message = "微信昵称不能为空")
    private String wxName;
}
