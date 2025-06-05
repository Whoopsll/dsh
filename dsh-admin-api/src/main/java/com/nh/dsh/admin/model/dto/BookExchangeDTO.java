package com.nh.dsh.admin.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-07-25 10:18
 **/

@Data
public class BookExchangeDTO {
    @ExcelProperty(value = "防伪卡号", index = 0)
    private String cardNum;
    @ExcelProperty(value = "防伪码链接", index = 1)
    private String bookLink;
}
