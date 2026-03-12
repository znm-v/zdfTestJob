package com.sweetbite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * SweetBite 启动类
 */
@SpringBootApplication
@MapperScan("com.sweetbite.mapper")
@EnableCaching
@EnableTransactionManagement
public class SweetBiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(SweetBiteApplication.class, args);
        System.out.println("=================================");
        System.out.println("SweetBite 后端服务启动成功！");
        System.out.println("接口文档地址: http://localhost:8080/doc.html");
        System.out.println("=================================");
    }
}
