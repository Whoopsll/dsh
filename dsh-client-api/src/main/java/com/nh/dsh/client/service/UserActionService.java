package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.enums.UserActionEnum;
import com.nh.dsh.client.model.entity.UserActionEntity;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 14:29
 **/

public interface UserActionService extends IService<UserActionEntity> {

    void doUserAction(Integer userId, Integer sourceId, UserActionEnum actionEnum);

    List<Integer> getActionList(Integer userId, UserActionEnum actionEnum);

    long getActionCount(Integer sourceId, UserActionEnum actionEnum);

    boolean checkUserAction(Integer userId, Integer sourceId, UserActionEnum actionEnum);
}
