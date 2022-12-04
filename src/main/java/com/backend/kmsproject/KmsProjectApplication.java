package com.backend.kmsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class KmsProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmsProjectApplication.class, args);
    }

}
