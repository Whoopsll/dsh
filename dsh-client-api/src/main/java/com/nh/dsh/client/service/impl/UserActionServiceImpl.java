package com.nh.dsh.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.enums.UserActionEnum;
import com.nh.dsh.client.mapper.UserActionMapper;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.entity.UserActionEntity;
import com.nh.dsh.client.service.UserActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 14:30
 **/

@Slf4j
@Service
public class UserActionServiceImpl extends BaseServiceImpl<UserActionMapper, UserActionEntity> implements UserActionService {

    @Override
    public void doUserAction(Integer userId, Integer sourceId, UserActionEnum actionEnum) {
        LambdaQueryWrapper<UserActionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserActionEntity::getUserId, userId).eq(UserActionEntity::getSourceId, sourceId).eq(UserActionEntity::getActionType, actionEnum.getType());
        List<UserActionEntity> result = baseMapper.selectList(queryWrapper);
        if (result.size() > 1) {
            throw new ServerException("行为多次重复，出现异常");
        }
        if (result.size() == 1) {
            baseMapper.delete(queryWrapper);
            return;
        }
        UserActionEntity userActionEntity = new UserActionEntity();
        userActionEntity.setUserId(userId);
        userActionEntity.setSourceId(sourceId);
        userActionEntity.setActionType(actionEnum.getType());
        baseMapper.insert(userActionEntity);
    }

    @Override
    public List<Integer> getActionList(Integer userId, UserActionEnum actionEnum) {
        LambdaQueryWrapper<UserActionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserActionEntity::getUserId, userId).eq(UserActionEntity::getActionType, actionEnum.getType());
        return baseMapper.selectList(queryWrapper).stream()
                .map(UserActionEntity::getSourceId)
                .toList();
    }

    @Override
    public long getActionCount(Integer sourceId, UserActionEnum actionEnum) {
        LambdaQueryWrapper<UserActionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserActionEntity::getSourceId, sourceId).eq(UserActionEntity::getActionType, actionEnum.getType());
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public boolean checkUserAction(Integer userId, Integer sourceId, UserActionEnum actionEnum) {
        LambdaQueryWrapper<UserActionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserActionEntity::getUserId, userId).eq(UserActionEntity::getSourceId, sourceId).eq(UserActionEntity::getActionType, actionEnum.getType());
        return baseMapper.selectList(queryWrapper).size() == 1;
    }
}
