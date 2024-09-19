package com.example.hbbcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.hbbcc"})
public class HbbccApplication {

    public static void main(String[] args) {
        SpringApplication.run(HbbccApplication.class, args);
    }

}
