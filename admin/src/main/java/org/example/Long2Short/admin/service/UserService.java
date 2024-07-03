package org.example.Long2Short.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.Long2Short.admin.dao.entity.UserDO;
import org.example.Long2Short.admin.dto.resp.UserRespDTO;

/**
 * @ClassName UserService
 * @Description 用户接口层
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 1:49
 * @Version v1.0
 */

public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在, true: 存在, false: 不存在
     */
    Boolean hasUsername(String username);
}
