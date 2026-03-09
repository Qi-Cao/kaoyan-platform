package com.kaoyan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kaoyan.mapper")
public class KaoyanPlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(KaoyanPlatformApplication.class, args);
    }
}
