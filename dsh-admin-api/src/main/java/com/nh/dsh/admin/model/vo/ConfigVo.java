package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ConfigVo {
    @Schema(description = "主键")
    private int id;
    @Schema(description = "配置名称")
    private String name;
    @Schema(description = "账号")
    private String account;
    @Schema(description = "密码")
   private String password;
    @Schema(description = "公众号logo")
   private String logo;
    @Schema(description = "出版社id")
    private int publisherId;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @Schema(description = "资源获取方式")
    private int resourceGetType;
}
