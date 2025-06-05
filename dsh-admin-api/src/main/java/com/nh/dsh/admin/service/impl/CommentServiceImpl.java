package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.mapper.CommentMapper;
import com.nh.dsh.admin.mapper.UserMapper;
import com.nh.dsh.admin.model.entity.CommentEntity;
import com.nh.dsh.admin.model.entity.UserEntity;
import com.nh.dsh.admin.model.vo.CommentItemVO;
import com.nh.dsh.admin.service.CommentService;
import com.nh.dsh.admin.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-08-27 10:31
 **/

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl extends BaseServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    private final UserMapper userMapper;

    @Override
    public List<CommentItemVO> listCommentByForumId(Integer forumId) {
        // 一次性加载所有评论
        LambdaQueryWrapper<CommentEntity> commentQueryWrapper = new LambdaQueryWrapper<>();
        commentQueryWrapper.eq(CommentEntity::getForumId, forumId)
                .orderByAsc(CommentEntity::getCreateTime);

        List<CommentEntity> allComments = baseMapper.selectList(commentQueryWrapper);
        if (allComments.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取所有涉及到的用户ID
        Set<Integer> userIds = allComments.stream()
                .map(CommentEntity::getUserId)
                .collect(Collectors.toSet());

        // 批量查询用户信息
        List<UserEntity> users = userMapper.selectBatchIds(userIds);
        Map<Integer, UserEntity> userMap = users.stream()
                .collect(Collectors.toMap(UserEntity::getId, user -> user));

        // 构建 ID 到 CommentItemVO 的映射
        Map<Integer, CommentItemVO> commentMap = new HashMap<>();
        for (CommentEntity comment : allComments) {
            CommentItemVO vo = convertToCommentItemVO(comment, userMap, commentMap);
            commentMap.put(comment.getId(), vo);
        }

        // 构建顶级评论列表
        List<CommentItemVO> topLevelComments = new ArrayList<>();
        for (CommentEntity comment : allComments) {
            if (comment.getParentId() == null) {
                topLevelComments.add(commentMap.get(comment.getId()));
            } else {
                // 追溯到顶级评论
                CommentEntity parent = comment;
                while (parent.getParentId() != null) {
                    CommentEntity finalParent = parent;
                    parent = allComments.stream().filter(c -> c.getId().equals(finalParent.getParentId())).findFirst().orElse(null);
                }
                CommentItemVO topLevelCommentVO = commentMap.get(parent.getId());
                if (topLevelCommentVO != null) {
                    topLevelCommentVO.getReplyList().add(commentMap.get(comment.getId()));
                    topLevelCommentVO.setReplyNum(topLevelCommentVO.getReplyList().size());
                }
            }
        }

        return topLevelComments;
    }

    @Override
    public void removeComment(Integer commentId) {
        List<Integer> allCommentIds = new ArrayList<>();
        getAllCommentIdsRecursive(Collections.singletonList(commentId), allCommentIds);
        allCommentIds.add(commentId);
        baseMapper.deleteBatchIds(allCommentIds);
    }

    @Override
    public void commentEnabledChange(Integer commentId) {
        CommentEntity comment = baseMapper.selectById(commentId);
        if (comment == null) {
            log.error("评论不存在");
            throw new ServerException("评论不存在");
        }
        Integer enabled = comment.getEnabled();
        // 1 变 0,0 变 1
        comment.setEnabled(enabled ^ 1);
        baseMapper.updateById(comment);
    }

    private void getAllCommentIdsRecursive(List<Integer> currentIds, List<Integer> allIds) {
        LambdaQueryWrapper<CommentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CommentEntity::getParentId, currentIds);
        List<Integer> childIds = baseMapper.selectList(queryWrapper).stream().map(CommentEntity::getId).toList();
        if (!childIds.isEmpty()) {
            allIds.addAll(childIds);
            getAllCommentIdsRecursive(childIds, allIds);
        }
    }

    // 转换 CommentEntity 为 CommentItemVO
    private CommentItemVO convertToCommentItemVO(CommentEntity entity, Map<Integer, UserEntity> userMap, Map<Integer, CommentItemVO> commentMap) {
        CommentItemVO vo = new CommentItemVO();
        vo.setId(entity.getId());
        vo.setFileType(entity.getFileType());
        vo.setFiles(entity.getFiles());

        vo.setType(entity.getType());
        vo.setContent(entity.getContent());
        vo.setEnabled(entity.getEnabled());
        vo.setUserId(entity.getUserId());
        UserEntity user = userMap.get(entity.getUserId());
        if (user != null) {
            vo.setUsername(user.getWxName());
            vo.setAvatar(user.getAvatar());
        }
        vo.setCreateTime(DateUtil.formatTimeAgo(DateUtil.format(entity.getCreateTime())));
        vo.setReplyList(new ArrayList<>()); // 初始化回复列表
        return vo;
    }
}
