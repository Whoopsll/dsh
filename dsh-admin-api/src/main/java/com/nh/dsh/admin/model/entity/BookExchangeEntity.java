package com.nh.dsh.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-30 15:36
 **/

@Data
@TableName("book_exchange")
@Schema(description = "书籍兑换记录实体")
public class BookExchangeEntity extends BaseEntity{
    private Integer bookId;
    private String verifyCode;
    private String userFlag;
    private Integer status;
    private String bookLink;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exchangeTime;
}
