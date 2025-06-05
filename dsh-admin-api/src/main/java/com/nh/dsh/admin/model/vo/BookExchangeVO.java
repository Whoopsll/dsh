package com.nh.dsh.admin.model.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-08-23 17:23
 **/

@Data
public class BookExchangeVO {
    @ExcelIgnore
    private Integer id;
    @ExcelIgnore
    private Integer bookId;
    @ExcelProperty(value = "校验码", index = 0)
    private String verifyCode;
    @ExcelIgnore
    private String userFlag;
    @ExcelIgnore
    private Integer status;
    @ExcelIgnore
    private String bookLink;
    @ExcelProperty(value = "二维码链接", index = 1)
    private String qrCodeLink;
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exchangeTime;
    @ExcelIgnore
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
