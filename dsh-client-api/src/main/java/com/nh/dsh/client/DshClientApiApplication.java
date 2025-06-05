package com.nh.dsh.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Flobby
 */

@SpringBootApplication
public class DshClientApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DshClientApiApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
