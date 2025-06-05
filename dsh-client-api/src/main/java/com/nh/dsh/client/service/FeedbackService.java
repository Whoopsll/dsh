package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.model.dto.FeedbackDTO;
import com.nh.dsh.client.model.entity.FeedbackEntity;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-18 14:34
 **/

public interface FeedbackService extends IService<FeedbackEntity> {

    void feedback(FeedbackDTO dto);
}
