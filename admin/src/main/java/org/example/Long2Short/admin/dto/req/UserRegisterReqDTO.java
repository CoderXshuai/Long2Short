package org.example.Long2Short.admin.dto.req;

import lombok.Data;

/**
 * @ClassName UserRegisterReqDTO
 * @Description 用户注册请求参数
 * @Author CoderXshuai
 * @CreateTime 2024/7/7 0:53
 * @Version v1.0
 */
@Data
public class UserRegisterReqDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
