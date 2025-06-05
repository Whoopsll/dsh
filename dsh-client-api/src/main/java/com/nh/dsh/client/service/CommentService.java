package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.common.result.PageResult;
import com.nh.dsh.client.enums.UserActionEnum;
import com.nh.dsh.client.model.dto.CommentActionDTO;
import com.nh.dsh.client.model.dto.CommentDTO;
import com.nh.dsh.client.model.entity.CommentEntity;
import com.nh.dsh.client.model.query.Query;
import com.nh.dsh.client.model.vo.CommentItemVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 10:33
 **/

public interface CommentService extends IService<CommentEntity> {

    void addComment(CommentDTO comment);

    List<CommentItemVO> queryForumCommentList(Integer forumId);

    CommentItemVO getCommentDetail(Integer commentId);

    void doCommentAction(CommentActionDTO dto);

    void modifyComment(CommentDTO dto);

    PageResult<CommentItemVO> queryStarCommentList(Query query);

    PageResult<CommentItemVO> queryMyCommentList(Query query);

    PageResult<CommentItemVO> queryReviewCommentList(Query query);
}
