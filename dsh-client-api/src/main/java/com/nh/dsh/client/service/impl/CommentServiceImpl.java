package com.nh.dsh.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.client.common.cache.RequestContext;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.common.result.PageResult;
import com.nh.dsh.client.convert.CommentConvert;
import com.nh.dsh.client.enums.UserActionEnum;
import com.nh.dsh.client.mapper.CommentMapper;
import com.nh.dsh.client.mapper.ForumMapper;
import com.nh.dsh.client.mapper.UserMapper;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.dto.CommentActionDTO;
import com.nh.dsh.client.model.dto.CommentDTO;
import com.nh.dsh.client.model.entity.CommentEntity;
import com.nh.dsh.client.model.entity.ForumEntity;
import com.nh.dsh.client.model.entity.UserEntity;
import com.nh.dsh.client.model.query.Query;
import com.nh.dsh.client.model.vo.CommentItemVO;
import com.nh.dsh.client.service.CommentService;
import com.nh.dsh.client.service.UserActionService;
import com.nh.dsh.client.utils.DateUtil;
import com.nh.dsh.client.utils.PageUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 10:33
 **/

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl extends BaseServiceImpl<CommentMapper, CommentEntity> implements CommentService {
    private final UserActionService userActionService;
    private final UserMapper userMapper;
    private final ForumMapper forumMapper;

    @Override
    public void addComment(CommentDTO comment) {
        log.info("添加评论: {}", comment);
        CommentEntity entity = CommentConvert.INSTANCE.convert(comment);
        entity.setUserId(RequestContext.getUserId());
        baseMapper.insert(entity);
    }

    @Override
    public List<CommentItemVO> queryForumCommentList(Integer forumId) {
        Integer userId = RequestContext.getUserId();

        // 一次性加载所有评论
        LambdaQueryWrapper<CommentEntity> commentQueryWrapper = new LambdaQueryWrapper<>();
        commentQueryWrapper.eq(CommentEntity::getForumId, forumId)
                .orderByDesc(CommentEntity::getCreateTime);

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
            CommentItemVO vo = convertToCommentItemVO(comment, userId, userMap, commentMap);
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
                }
            }
        }

        return topLevelComments;
    }

    @Override
    public CommentItemVO getCommentDetail(Integer commentId) {
        Integer userId = RequestContext.getUserId();

        // 查询指定评论
        CommentEntity targetComment = baseMapper.selectById(commentId);
        if (targetComment == null) {
            throw new ServerException("Comment not found");
        }

        // 获取目标评论的顶级评论
        CommentEntity topLevelComment = targetComment;
        while (topLevelComment.getParentId() != null) {
            topLevelComment = baseMapper.selectById(topLevelComment.getParentId());
            if (topLevelComment == null) {
                throw new ServerException("Parent comment not found");
            }
        }

        // 递归查询所有子评论
        List<CommentEntity> allComments = new ArrayList<>();
        allComments.add(topLevelComment);
        getAllChildComments(topLevelComment.getId(), allComments);

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
            CommentItemVO vo = convertToCommentItemVO(comment, userId, userMap);
            commentMap.put(comment.getId(), vo);
        }

        // 构建评论树
        for (CommentEntity comment : allComments) {
            if (comment.getParentId() != null) {
                CommentEntity parent = comment;
                while (parent.getParentId() != null) {
                    CommentEntity finalParent = parent;
                    parent = allComments.stream().filter(c -> c.getId().equals(finalParent.getParentId())).findFirst().orElse(null);
                }
                CommentItemVO topLevelCommentVO = commentMap.get(parent.getId());
                if (topLevelCommentVO != null) {
                    topLevelCommentVO.getReplyList().add(commentMap.get(comment.getId()));
                }
            }
        }

        return commentMap.get(topLevelComment.getId());
    }

    // 递归查询所有子评论
    private void getAllChildComments(Integer parentId, List<CommentEntity> allComments) {
        LambdaQueryWrapper<CommentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommentEntity::getParentId, parentId);
        List<CommentEntity> childComments = baseMapper.selectList(queryWrapper);

        for (CommentEntity child : childComments) {
            allComments.add(child);
            getAllChildComments(child.getId(), allComments);
        }
    }

    // 转换 CommentEntity 为 CommentItemVO
    private CommentItemVO convertToCommentItemVO(CommentEntity entity, Integer userId, Map<Integer, UserEntity> userMap) {
        CommentItemVO vo = new CommentItemVO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setFileType(entity.getFileType());
        vo.setFiles(entity.getFiles());

        if (entity.getParentId() != null) {
            CommentEntity parentEntity = baseMapper.selectById(entity.getParentId());
            if (parentEntity != null) {
                UserEntity parentUser = userMap.get(parentEntity.getUserId());
                vo.setParentUsername(parentUser != null ? parentUser.getWxName() : null);
            }
        }

        ForumEntity forum = forumMapper.selectById(entity.getForumId());
        vo.setForumName(forum.getName());
        vo.setType(entity.getType());
        vo.setContent(entity.getContent());
        vo.setUserId(entity.getUserId());
        vo.setForumId(entity.getForumId());
        UserEntity user = userMap.get(entity.getUserId());
        if (user != null) {
            vo.setUsername(user.getWxName());
            vo.setAvatar(user.getAvatar());
        }
        vo.setCreateTime(DateUtil.formatTimeAgo(DateUtil.format(entity.getCreateTime())));
        vo.setLikeNum(userActionService.getActionCount(entity.getId(), UserActionEnum.LIKE_COMMENT));
        vo.setLike(userActionService.checkUserAction(userId, entity.getId(), UserActionEnum.LIKE_COMMENT));
        vo.setStar(userActionService.checkUserAction(userId, entity.getId(), UserActionEnum.STAR_COMMENT));
        vo.setReplyList(new ArrayList<>()); // 初始化回复列表
        return vo;
    }


    // 转换 CommentEntity 为 CommentItemVO
    private CommentItemVO convertToCommentItemVO(CommentEntity entity, Integer userId, Map<Integer, UserEntity> userMap, Map<Integer, CommentItemVO> commentMap) {
        CommentItemVO vo = new CommentItemVO();
        vo.setId(entity.getId());
        vo.setParentId(entity.getParentId());
        vo.setFileType(entity.getFileType());
        vo.setFiles(entity.getFiles());
        if (entity.getParentId() != null) {
            CommentItemVO parentVO = commentMap.get(entity.getParentId());
            if (parentVO != null) {
                vo.setParentUsername(parentVO.getUsername());
            }
        }

        ForumEntity forum = forumMapper.selectById(entity.getForumId());
        vo.setForumName(forum.getName());
        vo.setType(entity.getType());
        vo.setContent(entity.getContent());
        vo.setUserId(entity.getUserId());
        vo.setForumId(entity.getForumId());
        UserEntity user = userMap.get(entity.getUserId());
        if (user != null) {
            vo.setUsername(user.getWxName());
            vo.setAvatar(user.getAvatar());
        }
        vo.setCreateTime(DateUtil.formatTimeAgo(DateUtil.format(entity.getCreateTime())));
        vo.setLikeNum(userActionService.getActionCount(entity.getId(), UserActionEnum.LIKE_COMMENT));
        vo.setLike(userActionService.checkUserAction(userId, entity.getId(), UserActionEnum.LIKE_COMMENT));
        vo.setStar(userActionService.checkUserAction(userId, entity.getId(), UserActionEnum.STAR_COMMENT));
        vo.setReplyList(new ArrayList<>()); // 初始化回复列表
        return vo;
    }

    @Override
    public void doCommentAction(CommentActionDTO dto) {
        if (dto.getActionType().equals(UserActionEnum.FOLLOW_FORUM.getType())) {
            throw new ServerException("行为类型错误");
        }
        userActionService.doUserAction(RequestContext.getUserId(), dto.getCommentId(), UserActionEnum.getByType(dto.getActionType()));
    }

    @Override
    public void modifyComment(CommentDTO dto) {
        CommentEntity commentEntity = baseMapper.selectById(dto.getId());
        commentEntity.setContent(dto.getContent());
        baseMapper.updateById(commentEntity);
    }

    @Override
    public PageResult<CommentItemVO> queryStarCommentList(Query query) {
        Page<CommentEntity> page = new Page<>(query.getPage(), query.getLimit());
        List<Integer> actionIdList = userActionService.getActionList(RequestContext.getUserId(), UserActionEnum.STAR_COMMENT);
        if (actionIdList.isEmpty()) {
            return new PageResult<>(new ArrayList<>(), 0);
        }
        LambdaQueryWrapper<CommentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CommentEntity::getId, actionIdList);
        baseMapper.selectPage(page, wrapper);
        return new PageResult<>(convertToCommentList(page.getRecords()), page.getTotal());
    }

    @Override
    public PageResult<CommentItemVO> queryMyCommentList(Query query) {
        Page<CommentEntity> page = new Page<>(query.getPage(), query.getLimit());
        LambdaQueryWrapper<CommentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentEntity::getUserId, RequestContext.getUserId());
        baseMapper.selectPage(page, wrapper);
        return new PageResult<>(convertToCommentList(page.getRecords()), page.getTotal());
    }

    @Override
    public PageResult<CommentItemVO> queryReviewCommentList(Query query) {
        LambdaQueryWrapper<CommentEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CommentEntity::getUserId, RequestContext.getUserId());
        wrapper.isNotNull(CommentEntity::getParentId);
        // 获取我评论的所有id
        Set<Integer> idList = baseMapper.selectList(wrapper).stream().map(comment -> {
            Integer id = comment.getId();
            // 获取我评论的id的最顶层id
            return getTopCommentId(id);
        }).collect(Collectors.toSet());
        log.info("idList: {}", idList);
        if (idList.isEmpty()) {
            return new PageResult<>(new ArrayList<>(), 0);
        }
        // 将顶层id分页
        Page<CommentItemVO> pageResult = PageUtil.getPages(query.getPage(), query.getLimit(), convertToCommentList(baseMapper.selectBatchIds(idList)));
        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal());
    }

    private Integer getTopCommentId(Integer commentId) {
        CommentEntity comment = baseMapper.selectById(commentId);
        if (comment == null) {
            throw new ServerException("评论不存在");
        }
        Integer parentId = comment.getParentId();
        while (parentId != null && parentId > 0) {
            comment = baseMapper.selectById(parentId);
            if (comment == null || comment.getParentId() == null) {
                break;
            }
            parentId = comment.getParentId();
        }
        return parentId;
    }

    private List<CommentItemVO> convertToCommentList(List<CommentEntity> entityList) {
        return entityList.stream().map(entity -> {
            CommentItemVO vo = new CommentItemVO();
            vo.setId(entity.getId());
            vo.setFileType(entity.getFileType());
            vo.setFiles(entity.getFiles());
            ForumEntity forum = forumMapper.selectById(entity.getForumId());
            vo.setForumName(forum.getName());
            vo.setType(entity.getType());
            vo.setContent(entity.getContent());
            vo.setUserId(entity.getUserId());
            vo.setForumId(entity.getForumId());
            UserEntity user = userMapper.selectById(entity.getUserId());
            if (user != null) {
                vo.setUsername(user.getWxName());
                vo.setAvatar(user.getAvatar());
            }
            vo.setCreateTime(DateUtil.format(entity.getCreateTime()));
            return vo;
        }).toList();
    }

}
