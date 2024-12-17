package com.truemen.api.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.truemen.api.common", "com.truemen.api.post", "com.truemen.api.guide",
        "com.truemen.api.landmark" })
public class TruemenApplication {
    public static void main(String[] args) {
        SpringApplication.run(TruemenApplication.class, args);
    }
}