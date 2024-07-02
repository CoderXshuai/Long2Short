package org.example.Long2Short.admin.dto.resp;

import lombok.Data;

/**
 * @ClassName UserRespDTO
 * @Description 用户返回参数响应
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 1:56
 * @Version v1.0
 */

@Data
public class UserRespDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

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
