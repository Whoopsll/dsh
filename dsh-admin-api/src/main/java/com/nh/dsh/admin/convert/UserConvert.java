package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.entity.UserEntity;
import com.nh.dsh.admin.model.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-07-17 16:38
 **/

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserVO convert(UserEntity user);

    List<UserVO> convertList(List<UserEntity> user);

}
