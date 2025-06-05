package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.model.entity.CommentEntity;
import com.nh.dsh.admin.model.vo.CommentItemVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-08-27 10:30
 **/

public interface CommentService extends IService<CommentEntity> {

    List<CommentItemVO> listCommentByForumId(Integer forumId);

    void removeComment(Integer commentId);

    void commentEnabledChange(Integer commentId);
}
