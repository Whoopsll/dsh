package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.mapper.ManagerPublisherMapper;
import com.nh.dsh.admin.model.entity.ManagerPublisherEntity;
import com.nh.dsh.admin.model.entity.PublisherEntity;
import com.nh.dsh.admin.service.ManagerPublisherService;
import com.nh.dsh.admin.service.PublisherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-30 13:29
 **/
@Service
@AllArgsConstructor
public class ManagerPublisherServiceImpl extends BaseServiceImpl<ManagerPublisherMapper, ManagerPublisherEntity> implements ManagerPublisherService {
    private final PublisherService publisherService;

    @Override
    public void save(Integer managerId, List<Integer> publisherIdList) {
        List<ManagerPublisherEntity> list = publisherIdList.stream().map(publisherId -> {
            ManagerPublisherEntity entity = new ManagerPublisherEntity();
            entity.setManagerId(managerId);
            entity.setPublisherId(publisherId);
            return entity;
        }).toList();
        this.saveBatch(list);
    }

    @Override
    public void update(Integer managerId, List<Integer> publisherIdList) {
        this.delete(managerId);
        if (publisherIdList != null && !publisherIdList.isEmpty()) {
            this.save(managerId, publisherIdList);
        }
    }


    @Override
    public void delete(Integer managerId) {
        LambdaQueryWrapper<ManagerPublisherEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ManagerPublisherEntity::getManagerId, managerId);
        this.remove(wrapper);
    }

    @Override
    public List<Integer> getPublisherIdList(Integer managerId) {
        LambdaQueryWrapper<ManagerPublisherEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ManagerPublisherEntity::getManagerId, managerId);
        return list(wrapper).stream().map(ManagerPublisherEntity::getPublisherId).toList();
    }

    @Override
    public List<String> getPublisherList(Integer managerId) {
        LambdaQueryWrapper<PublisherEntity> wrapper = new LambdaQueryWrapper<>();
        List<Integer> publisherIdList = this.getPublisherIdList(managerId);
        wrapper.select(PublisherEntity::getName).in(!publisherIdList.isEmpty(), PublisherEntity::getId, publisherIdList);
        return publisherService.list(wrapper).stream().map(PublisherEntity::getName).toList();
    }
}
