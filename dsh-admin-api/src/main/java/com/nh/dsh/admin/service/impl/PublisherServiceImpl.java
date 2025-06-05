package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.PublisherConvert;
import com.nh.dsh.admin.mapper.PublisherMapper;
import com.nh.dsh.admin.model.dto.PublisherDTO;
import com.nh.dsh.admin.model.entity.PublisherEntity;
import com.nh.dsh.admin.model.query.PublisherQuery;
import com.nh.dsh.admin.model.vo.PublisherVO;
import com.nh.dsh.admin.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-29 15:37
 **/

@Slf4j
@Service
public class PublisherServiceImpl extends BaseServiceImpl<PublisherMapper, PublisherEntity> implements PublisherService {
    @Override
    public void save(PublisherDTO dto) {
        PublisherEntity publisher = PublisherConvert.INSTANCE.convert(dto);
        baseMapper.insert(publisher);
    }

    @Override
    public void update(PublisherDTO dto) {
        PublisherEntity publisher = PublisherConvert.INSTANCE.convert(dto);
        baseMapper.updateById(publisher);
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public PageResult<PublisherVO> page(PublisherQuery query) {
        Page<PublisherEntity> entityPage = getPage(query);
        LambdaQueryWrapper<PublisherEntity> wrapper = new LambdaQueryWrapper<PublisherEntity>().orderByDesc(PublisherEntity::getCreateTime);
        Page<PublisherEntity> pageResult = baseMapper.selectPage(entityPage, wrapper);
        return new PageResult<>(PublisherConvert.INSTANCE.convertList(pageResult.getRecords()), pageResult.getTotal());
    }
}
