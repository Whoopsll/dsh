package com.nh.dsh.admin.model.vo;

import lombok.Data;

@Data
public class AuditVo {
    private int interval; //0:立刻开始，1:每周
    private String cron; // 定时起的cron表达式
    private String time; //时间，周期为每周时，需传入time
    private String email; //异常接收邮箱地址
}
