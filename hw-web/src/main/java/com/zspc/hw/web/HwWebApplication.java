package com.zspc.hw.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients(value = {"com.zspc.hw.manage.remote.feign"})
@EnableTransactionManagement
@MapperScan("com.zspc.hw.mapper")
@SpringBootApplication
public class HwWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwWebApplication.class, args);
    }

}
