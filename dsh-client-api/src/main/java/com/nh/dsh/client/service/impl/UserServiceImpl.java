package com.nh.dsh.client.service.impl;

import com.nh.dsh.client.common.cache.RequestContext;
import com.nh.dsh.client.mapper.UserMapper;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.entity.UserEntity;
import com.nh.dsh.client.model.vo.UserInfoVO;
import com.nh.dsh.client.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-07 16:23
 **/

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public UserInfoVO userInfo() {
        Integer userId = RequestContext.getUserId();
        UserEntity user = baseMapper.selectById(userId);
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setNickname(user.getWxName());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setWxId(user.getWxId());
        return userInfoVO;
    }
}
