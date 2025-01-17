package org.example.Long2Short.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.example.Long2Short.admin.common.convention.result.Result;
import org.example.Long2Short.admin.common.convention.result.Results;
import org.example.Long2Short.admin.dto.req.UserRegisterReqDTO;
import org.example.Long2Short.admin.dto.req.UserUpdateReqDTO;
import org.example.Long2Short.admin.dto.resp.UserActualRespDTO;
import org.example.Long2Short.admin.dto.resp.UserRespDTO;
import org.example.Long2Short.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    /**
     * 根据用户名查询用户无脱敏信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/api/long2short/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username) {
        UserActualRespDTO userActualRespDTO = BeanUtil.toBean(userService.getUserByUsername(username), UserActualRespDTO.class);
        return Results.success(userActualRespDTO);
    }

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在 true: 存在, false: 不存在
     */
    @GetMapping("/api/long2short/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username) {
        return Results.success(userService.hasUsername(username));
    }

    @PostMapping("/api/long2short/v1/user/")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success();
    }

    @PutMapping("/api/long2short/v1/user/")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam) {
        userService.update(requestParam);
        return Results.success();
    }
}
