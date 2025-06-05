package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.FeedbackConvert;
import com.nh.dsh.admin.mapper.FeedbackMapper;
import com.nh.dsh.admin.model.entity.FeedbackEntity;
import com.nh.dsh.admin.model.entity.UserEntity;
import com.nh.dsh.admin.model.query.FeedbackQuery;
import com.nh.dsh.admin.model.vo.FeedbackVO;
import com.nh.dsh.admin.service.FeedbackService;
import com.nh.dsh.admin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 09:50
 **/

@Service
@AllArgsConstructor
public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackMapper, FeedbackEntity> implements FeedbackService {
    private final UserService userService;

    @Override
    public PageResult<FeedbackVO> page(FeedbackQuery feedbackQuery) {
        Page<FeedbackEntity> page = getPage(feedbackQuery);
        LambdaQueryWrapper<FeedbackEntity> wrapper = new LambdaQueryWrapper<FeedbackEntity>().orderByDesc(FeedbackEntity::getCreateTime);
        Page<FeedbackEntity> pageResult = baseMapper.selectPage(page, wrapper);
        List<FeedbackVO> resultList = pageResult.getRecords().stream().map(item -> {
            FeedbackVO vo = FeedbackConvert.INSTANCE.convert(item);
            UserEntity user = userService.getById(item.getUserId());
            vo.setUsername(user.getWxName());
            return vo;
        }).toList();
        return new PageResult<>(resultList, pageResult.getTotal());
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }


}
