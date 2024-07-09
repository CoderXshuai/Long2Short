package org.example.Long2Short.admin;

/**
 * @ClassName UserTableShardingTest
 * @Description 用户表分片测试
 * @Author CoderXshuai
 * @CreateTime 2024/7/10 1:03
 * @Version v1.0
 */

public class UserTableShardingTest {
    public static final String SQL = """
            create table if not exists long2short.t_user_%d
            (
                id            bigint auto_increment comment 'ID'
                    primary key,
                username      varchar(255) null comment '用户名',
                password      varchar(512) null comment '密码',
                real_name     varchar(256) null comment '真实姓名',
                phone         varchar(128) null comment '手机号',
                mail          varchar(512) null comment '邮箱',
                deletion_time bigint       null comment '注销时间戳',
                create_time   datetime     null comment '创建时间',
                update_time   datetime     null comment '修改时间',
                del_flag      tinyint(1)   null comment '删除标识
            0：未删除
            1：已删除'
            )
                collate = utf8mb4_general_ci;
                    
            """;

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.printf((SQL) + "%n", i);
        }
    }
}
