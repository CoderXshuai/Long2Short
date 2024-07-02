package org.example.Long2Short.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @ClassName UserController
 * @Description 添加描述
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 0:36
 * @Version v1.0
 */
@SpringBootApplication
@MapperScan("org.example.Long2Short.admin.dao.mapper")
public class Long2ShortAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(Long2ShortAdminApplication.class, args);
    }
}
