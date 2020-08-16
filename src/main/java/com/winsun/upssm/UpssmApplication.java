package com.winsun.upssm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.winsun.upssm.mapper")
@SpringBootApplication
public class UpssmApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpssmApplication.class, args);
    }

}
