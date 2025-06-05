package com.nh.dsh.admin.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-08-05 23:08
 **/

@Data
@Configuration
public class CodeConfig {

    @Value(value = "${code.secret}")
    private String secret;
}
