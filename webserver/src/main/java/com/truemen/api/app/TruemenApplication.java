package com.truemen.api.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
<<<<<<< HEAD
@ComponentScan(basePackages = { "com.truemen.api.common", "com.truemen.api.post", "com.truemen.api.guide",
        "com.truemen.api.landmark" })
=======
@ComponentScan(basePackages = {"com.truemen.api.common", "com.truemen.api.post", "com.truemen.api.guide", "com.truemen.api.landmark", "com.truemen.api.user"})
@MapperScan("com.truemen.api.guide.repository")
>>>>>>> 417c2aee82355870caf58377966e92c8e83db3d2
public class TruemenApplication {
    public static void main(String[] args) {
        SpringApplication.run(TruemenApplication.class, args);
    }
}