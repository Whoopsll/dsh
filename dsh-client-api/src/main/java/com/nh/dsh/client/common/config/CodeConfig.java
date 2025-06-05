package com.nh.dsh.client.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-08-05 22:53
 **/

@Data
@Configuration
public class CodeConfig {

    @Value(value = "${code.secret}")
    private String secret;
}
