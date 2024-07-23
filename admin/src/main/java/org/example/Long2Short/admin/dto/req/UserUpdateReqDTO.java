package org.example.Long2Short.admin.dto.req;

import lombok.Data;

/**
 * @ClassName UserUpdateReqDTO
 * @Description 用户更新请求参数
 * @Author CoderXshuai
 * @CreateTime 2024/7/18 21:53
 * @Version v1.0
 */
@Data
public class UserUpdateReqDTO {

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
