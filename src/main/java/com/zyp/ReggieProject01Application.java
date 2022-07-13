package com.zyp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan("com.zyp.filter")
@EnableTransactionManagement // 开启声明式事务支持
@EnableCaching
public class ReggieProject01Application {

    public static void main(String[] args) {
        SpringApplication.run(ReggieProject01Application.class, args);
        log.info("项目启动成功....");
    }

}