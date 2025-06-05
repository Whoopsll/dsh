package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:38
 **/

@Data
@Schema(description = "出版社视图")
public class PublisherVO {
    @Schema(description = "主键")
    private Integer id;

    @Schema(description = "出版社")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
