package com.nh.dsh.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.client.common.cache.RequestContext;
import com.nh.dsh.client.common.exception.ErrorCode;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.common.result.PageResult;
import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.convert.ForumConvert;
import com.nh.dsh.client.enums.UserActionEnum;
import com.nh.dsh.client.mapper.*;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.entity.*;
import com.nh.dsh.client.model.query.ForumQuery;
import com.nh.dsh.client.model.vo.ForumDetailVO;
import com.nh.dsh.client.model.vo.ForumListItemVO;
import com.nh.dsh.client.service.CommentService;
import com.nh.dsh.client.service.ForumService;
import com.nh.dsh.client.service.UserActionService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.*;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 10:55
 **/

@Slf4j
@Service
@AllArgsConstructor
public class ForumServiceImpl extends BaseServiceImpl<ForumMapper, ForumEntity> implements ForumService {
    private final UserMapper userMapper;
    private final BookMapper bookMapper;
    private final CategoryMapper categoryMapper;
    private final UserActionService userActionService;
    private final PublisherMapper publisherMapper;
    private final BookResourceMapper bookResourceMapper;
    @Lazy
    private final CommentService commentService;

    @Override
    public PageResult<ForumListItemVO> forumList(ForumQuery query) {
        Page<ForumEntity> page = getPage(query);
        LambdaQueryWrapper<CommentEntity> commentQueryWrapper = new LambdaQueryWrapper<>();
        List<ForumListItemVO> resultList = baseMapper.selectByPage(page, query).stream().map(item -> {
            ForumListItemVO vo = ForumConvert.INSTANCE.convert(item);
            commentQueryWrapper.eq(CommentEntity::getForumId, item.getId());
            List<CommentEntity> allComments = commentService.getBaseMapper().selectList(commentQueryWrapper);
            BookEntity bookEntity = bookMapper.selectById(item.getBookId());
            UserEntity userEntity = userMapper.selectById(item.getUserId());
            CategoryEntity category = categoryMapper.selectById(bookEntity.getClassificationId());
            vo.setAuthor(bookEntity.getWriter());
            vo.setType(category.getName());
            // if (userEntity != null) {
            // //设置圈主
            //     vo.setAuthor(userEntity.getWxName());
            // }
            if (vo.getId() == 22) {
                vo.setFollowCount(432);
            } else if (vo.getId() == 23) {
                vo.setFollowCount(553);
            } else if(vo.getId() == 24) {
                vo.setFollowCount(325);
            } else if (vo.getId() == 25) {
                vo.setFollowCount(465);
            }else {
                vo.setFollowCount(userActionService.getActionCount(item.getId(), UserActionEnum.FOLLOW_FORUM));
            }
            vo.setCommentCount(allComments.size());
            vo.setBookDescription(bookEntity.getBookIntroduction());
            if (RequestContext.getUserId() == null) {
                vo.setFollow(false);
            } else {
                vo.setFollow(userActionService.checkUserAction(RequestContext.getUserId(), item.getId(), UserActionEnum.FOLLOW_FORUM));
            }
            return vo;
        }).sorted(Comparator.comparing(ForumListItemVO::getFollowCount).reversed()).toList();
        return new PageResult<>(resultList, page.getTotal());
    }

    @Override
    public ForumDetailVO forumDetail(Integer id) {
        ForumEntity entity = baseMapper.selectById(id);
        ForumDetailVO forumDetailVO = ForumConvert.INSTANCE.convertDetail(entity);
        UserEntity userEntity = userMapper.selectById(entity.getUserId());
        if (userEntity == null) {
            //不是系统用户
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        forumDetailVO.setUsername(userEntity.getWxName());
        BookEntity bookEntity = null;
        if (StringUtils.isNotBlank(entity.getOtherBookId())) {
            //如果不为空，获取第三方书籍
            bookEntity = bookMapper.selectOne(new QueryWrapper<BookEntity>().eq("other_id", entity.getOtherBookId()));
        } else {
            bookEntity = bookMapper.selectById(entity.getBookId());
        }
        forumDetailVO.setBookName(bookEntity.getBookName());
        forumDetailVO.setBookIntroduction(bookEntity.getBookIntroduction());
        forumDetailVO.setWriter(bookEntity.getWriter());
        CategoryEntity category = categoryMapper.selectById(bookEntity.getClassificationId());
        forumDetailVO.setCategory(category.getName());
        forumDetailVO.setCategoryId(category.getId());
        forumDetailVO.setWriterIntroduction(bookEntity.getAuthorIntroduction());
        forumDetailVO.setPublisherTime(bookEntity.getPublishDate());
        forumDetailVO.setPrice(bookEntity.getPrice());
        forumDetailVO.setIsbn(bookEntity.getIsbn());
        if(bookEntity.getPublisherId() != null || bookEntity.getPublisherId() != 0) {
            PublisherEntity publisherEntity = publisherMapper.selectById(bookEntity.getPublisherId());
            if (publisherEntity != null) {
                forumDetailVO.setPublisherName(publisherEntity.getName());
            }
        }
        if (forumDetailVO.getId() == 22) {
            forumDetailVO.setFollowCount(432 + userActionService.getActionCount(entity.getId(), UserActionEnum.FOLLOW_FORUM));
        } else if (forumDetailVO.getId() == 23) {
            forumDetailVO.setFollowCount(553 + userActionService.getActionCount(entity.getId(), UserActionEnum.FOLLOW_FORUM));
        }else if (forumDetailVO.getId() == 24) {
            forumDetailVO.setFollowCount(325 + userActionService.getActionCount(entity.getId(), UserActionEnum.FOLLOW_FORUM));
        } else if (forumDetailVO.getId() == 25) {
            forumDetailVO.setFollowCount(465 + userActionService.getActionCount(entity.getId(), UserActionEnum.FOLLOW_FORUM));
        } else {
            forumDetailVO.setFollowCount(userActionService.getActionCount(entity.getId(), UserActionEnum.FOLLOW_FORUM));
        }
        forumDetailVO.setFollow(userActionService.checkUserAction(RequestContext.getUserId(), entity.getId(), UserActionEnum.FOLLOW_FORUM));
        //获取总资源链接
        List<BookResourceEntity> bookResourceEntities = bookResourceMapper.selectList(new QueryWrapper<BookResourceEntity>().eq("type", 2).eq("book_id", bookEntity.getId()));
        if (!bookResourceEntities.isEmpty()) {
            String link = bookResourceEntities.get(0).getLink();
            forumDetailVO.setResourceUrl(link);
        } else {
            forumDetailVO.setResourceUrl("");
        }
        return forumDetailVO;
    }

    @Override
    public void followedForum(Integer forumId) {
        userActionService.doUserAction(RequestContext.getUserId(), forumId, UserActionEnum.FOLLOW_FORUM);
    }

    @Override
    public PageResult<ForumListItemVO> forumListFollowed(ForumQuery query) {
        List<Integer> sourceIdList = userActionService.getActionList(RequestContext.getUserId(), UserActionEnum.FOLLOW_FORUM);
        if (sourceIdList.isEmpty()) {
            return new PageResult<>(List.of(), 0L);
        }
        LambdaQueryWrapper<CommentEntity> commentQueryWrapper = new LambdaQueryWrapper<>();

        Page<ForumEntity> page = getPage(query);
        LambdaQueryWrapper<ForumEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ForumEntity::getId, sourceIdList).orderByDesc(ForumEntity::getCreateTime);
        Page<ForumEntity> pageResult = baseMapper.selectPage(page, queryWrapper);
        List<ForumListItemVO> resultList = pageResult.getRecords().stream().map(item -> {
            commentQueryWrapper.eq(CommentEntity::getForumId, item.getId())
                    .orderByAsc(CommentEntity::getCreateTime);
            List<CommentEntity> allComments = commentService.getBaseMapper().selectList(commentQueryWrapper);
            ForumListItemVO vo = ForumConvert.INSTANCE.convert(item);
            BookEntity bookEntity = bookMapper.selectById(item.getBookId());
            UserEntity userEntity = userMapper.selectById(item.getUserId());
            CategoryEntity category = categoryMapper.selectById(bookEntity.getClassificationId());
            vo.setAuthor(bookEntity.getAuthorIntroduction());
            vo.setType(category.getName());
            vo.setAuthor(userEntity.getWxName());
            vo.setCommentCount(allComments.size());
            vo.setBookDescription(bookEntity.getBookIntroduction());
            vo.setFollow(userActionService.checkUserAction(RequestContext.getUserId(), vo.getId(),UserActionEnum.FOLLOW_FORUM));
            vo.setFollowCount(userActionService.getActionCount(item.getId(), UserActionEnum.FOLLOW_FORUM));
            return vo;
        }).sorted(Comparator.comparing(ForumListItemVO::getFollowCount)).toList();
        return new PageResult<>(resultList, pageResult.getTotal());
    }

    @Override
    public Result getLikedBook(int typeId, int bookId) {
        List<BookEntity> bookEntities = bookMapper.selectList(new QueryWrapper<BookEntity>().ne("id", bookId).eq("classification_id", typeId).eq("delete_flag",0));
        List<BookEntity> result = new ArrayList<>(4);
        List<Integer> bookIds = new ArrayList<>();
        if(!bookEntities.isEmpty()) {
            //如果小于等于4,随机取4个数据
            if (bookEntities.size() <= 4) {
                result = bookEntities;
            } else {
                //如果大于等于4，随机取4个
                Random random = new Random();
                //创建一个新的列表，并打乱顺序
                List<BookEntity> shuffledList = new ArrayList<>(bookEntities);
                Collections.shuffle(shuffledList, random);
                result.addAll(shuffledList.subList(0, 4));
            }
            result.forEach(item -> {
                bookIds.add(item.getId());
            });
            List<ForumEntity> forums = new ArrayList<>();
            if (bookIds.size() > 1) {
                forums = getBaseMapper().getLikedForums(bookIds);
            } else if(bookIds.size() >0){
                forums = getBaseMapper().selectList(new QueryWrapper<ForumEntity>().eq("book_id", bookIds.get(0)).eq("delete_flag", 0));
            }
            List<ForumListItemVO> list = forums.stream().map(forum -> {
                ForumListItemVO itemVO = new ForumListItemVO();
                itemVO.setCover(forum.getCover());
                itemVO.setId(forum.getId());
                itemVO.setName(forum.getName());
                return itemVO;
            }).toList();
            return Result.ok(list);
        }
        return Result.ok(Arrays.asList());
    }
}
