package com.nh.dsh.client.service.impl;

import com.nh.dsh.client.common.cache.RequestContext;
import com.nh.dsh.client.mapper.FeedbackMapper;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.dto.FeedbackDTO;
import com.nh.dsh.client.model.entity.FeedbackEntity;
import com.nh.dsh.client.service.FeedbackService;
import org.springframework.stereotype.Service;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-18 14:34
 **/

@Service
public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackMapper, FeedbackEntity> implements FeedbackService {
    @Override
    public void feedback(FeedbackDTO dto) {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setUserId(RequestContext.getUserId());
        entity.setContent(dto.getFeedback());
        entity.setImg(dto.getImg());
        baseMapper.insert(entity);
    }
}
