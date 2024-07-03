package org.example.Long2Short.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.Long2Short.admin.common.convention.exception.ClientException;
import org.example.Long2Short.admin.common.enums.UserErrorCodeEnum;
import org.example.Long2Short.admin.dao.entity.UserDO;
import org.example.Long2Short.admin.dao.mapper.UserMapper;
import org.example.Long2Short.admin.dto.resp.UserRespDTO;
import org.example.Long2Short.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


/**
 * @ClassName UserServiceImpl
 * @Description 用户接口实现层
 * @Author CoderXshuai
 * @CreateTime 2024/6/30 1:53
 * @Version v1.0
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean hasUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        return userDO != null;
    }
}
