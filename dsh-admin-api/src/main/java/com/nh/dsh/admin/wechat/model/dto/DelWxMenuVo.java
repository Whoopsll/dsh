package com.nh.dsh.admin.wechat.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DelWxMenuVo {
    private String appId;
    private List<String> keys;
}
