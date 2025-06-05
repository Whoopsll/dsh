package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.model.entity.ManagerPublisherEntity;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-30 13:29
 **/

public interface ManagerPublisherService extends IService<ManagerPublisherEntity> {

    void save(Integer managerId, List<Integer> publisherIdList);

    void update(Integer managerId, List<Integer> publisherIdList);

    void delete(Integer managerId);

    List<Integer> getPublisherIdList(Integer managerId);

    List<String> getPublisherList(Integer managerId);


}
