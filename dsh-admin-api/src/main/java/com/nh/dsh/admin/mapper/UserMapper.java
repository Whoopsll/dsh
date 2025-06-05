package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nh.dsh.admin.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 15:07
 **/

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
