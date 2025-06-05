package com.nh.dsh.admin.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-07-21 16:59
 **/

@Data
public class ResourceImportDTO {
    @ExcelProperty(value = "描述", index = 1)
    private String desc;
    @ExcelProperty(value = "对应链接", index = 0)
    private String url;
}
