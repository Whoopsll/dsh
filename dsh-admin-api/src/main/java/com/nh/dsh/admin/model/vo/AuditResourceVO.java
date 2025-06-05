package com.nh.dsh.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-07-24 17:36
 **/

@Data
public class AuditResourceVO {
    private Integer id;
    private Integer bookId;
    private String bookName;
    private String bookCover;
    private String resourceName;
    private String link;
    private Integer isChanged;
    private Integer result;
    private String reason;
    private Integer isCanAccess;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private int type;
}
