package com.nh.dsh.client.model.dto;

import lombok.Data;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-08-05 22:56
 **/

@Data
public class CodeDTO {
    private Integer resourceId;
    private String codeSecret;
    private String openId;
}
