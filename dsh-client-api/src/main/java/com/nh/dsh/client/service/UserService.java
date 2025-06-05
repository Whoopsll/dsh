package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.model.entity.UserEntity;
import com.nh.dsh.client.model.vo.UserInfoVO;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-07 16:21
 **/

public interface UserService extends IService<UserEntity> {

    UserInfoVO userInfo();
}
