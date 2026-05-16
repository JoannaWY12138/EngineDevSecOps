package com.devsecops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DevSecOps平台主启动类
 * 支持代码合并、构建、发布和分支管理功能
 */
@SpringBootApplication
public class DevSecOpsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DevSecOpsApplication.class, args);
    }
}