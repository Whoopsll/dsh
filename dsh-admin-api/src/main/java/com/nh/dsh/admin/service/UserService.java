package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.entity.UserEntity;
import com.nh.dsh.admin.model.query.UserQuery;
import com.nh.dsh.admin.model.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 15:07
 **/

public interface UserService extends IService<UserEntity> {

    PageResult<UserVO> page(UserQuery query);

    List<Map<String, Object>> selectedList();

    Integer delete(List<Integer> ids);
}
