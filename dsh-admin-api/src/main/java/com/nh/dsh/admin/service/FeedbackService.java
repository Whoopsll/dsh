package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.entity.FeedbackEntity;
import com.nh.dsh.admin.model.query.FeedbackQuery;
import com.nh.dsh.admin.model.vo.FeedbackVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 09:50
 **/

public interface FeedbackService extends IService<FeedbackEntity> {

    PageResult<FeedbackVO> page(FeedbackQuery feedbackQuery);

    void deleteInBatch(List<Integer> ids);
}
