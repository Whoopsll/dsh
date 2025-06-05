package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.UserConvert;
import com.nh.dsh.admin.mapper.UserMapper;
import com.nh.dsh.admin.model.entity.UserEntity;
import com.nh.dsh.admin.model.query.UserQuery;
import com.nh.dsh.admin.model.vo.UserVO;
import com.nh.dsh.admin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 15:07
 **/

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public PageResult<UserVO> page(UserQuery query) {
        Page<UserEntity> page = getPage(query);
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(UserEntity::getCreateTime);
        baseMapper.selectPage(page, queryWrapper);
        return new PageResult<>(UserConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    @Override
    public List<Map<String, Object>> selectedList() {
        return list().stream().map(item -> {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("label", item.getWxName());
            itemMap.put("value", item.getId());
            return itemMap;
        }).toList();
    }

    @Override
    public Integer delete(List<Integer> ids) {
        return baseMapper.deleteBatchIds(ids);
    }
}
