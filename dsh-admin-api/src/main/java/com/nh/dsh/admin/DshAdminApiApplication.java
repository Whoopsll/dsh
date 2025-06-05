package com.nh.dsh.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync  // 开启异步支持
public class DshAdminApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DshAdminApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DshAdminApiApplication.class);
    }

}
