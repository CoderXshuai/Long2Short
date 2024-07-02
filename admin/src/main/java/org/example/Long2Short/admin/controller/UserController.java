package org.example.Long2Short.admin.controller;

import lombok.RequiredArgsConstructor;
import org.example.Long2Short.admin.common.convention.result.Result;
import org.example.Long2Short.admin.common.convention.result.Results;
import org.example.Long2Short.admin.dto.resp.UserRespDTO;
import org.example.Long2Short.admin.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description 用户管理控制层
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 0:36
 * @Version v1.0
 */

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */

    @GetMapping("/api/long2short/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username) {
        UserRespDTO userRespDTO = userService.getUserByUsername(username);
        return Results.success(userRespDTO);
    }
}
