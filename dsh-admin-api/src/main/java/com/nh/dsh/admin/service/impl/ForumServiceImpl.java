package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.ForumConvert;
import com.nh.dsh.admin.mapper.BookMapper;
import com.nh.dsh.admin.mapper.ForumMapper;
import com.nh.dsh.admin.model.dto.ForumDTO;
import com.nh.dsh.admin.model.entity.BookEntity;
import com.nh.dsh.admin.model.entity.ForumEntity;
import com.nh.dsh.admin.model.query.ForumQuery;
import com.nh.dsh.admin.model.vo.ForumVO;
import com.nh.dsh.admin.security.cache.TokenStoreCache;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.service.ForumService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 15:53
 **/

@Slf4j
@Service
@AllArgsConstructor
public class ForumServiceImpl extends BaseServiceImpl<ForumMapper, ForumEntity> implements ForumService {
    private final TokenStoreCache tokenStoreCache;
    private final BookMapper bookMapper;

    @Override
    public void save(ForumDTO dto) {
        ForumEntity entity = ForumConvert.INSTANCE.convert(dto);
        // 判断是否存在这个书籍的论坛
        LambdaQueryWrapper<ForumEntity> query = new LambdaQueryWrapper<>();
        query.eq(ForumEntity::getBookId, entity.getBookId());
        Long count = baseMapper.selectCount(query);
        if (count > 0) {
            log.error("该书籍已经存在论坛");
            throw new ServerException("该书籍已经存在论坛");
        }
        baseMapper.insert(entity);
        // 更新书籍论坛链接到书籍表
        LambdaUpdateWrapper<BookEntity> update = new LambdaUpdateWrapper<>();
        update.eq(BookEntity::getId, entity.getBookId());
        String forumLink = String.format("http://demo.dianhuiyun.com.cn/dsh-client-h5/#/pages/forum/forum?id=%d&type=1", entity.getId());
        update.set(BookEntity::getForumLink, forumLink);
        bookMapper.update(update);
    }

    @Override
    public void update(ForumDTO dto) {
        ForumEntity entity = ForumConvert.INSTANCE.convert(dto);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public PageResult<ForumVO> page(ForumQuery query, String authorization) {
        ManagerDetail user = tokenStoreCache.getUser(authorization);
        Page<ForumEntity> page = getPage(query);
        List<ForumVO> resultList = baseMapper.getPage(page, query, user.getPublisherIdList());
        return new PageResult<>(resultList, page.getTotal());
    }

    @Override
    public boolean stickyForum(int forumId) {
        try {
            ForumEntity forumEntity = baseMapper.selectById(forumId);
            if (forumEntity == null) {
                throw new ServerException("论坛不存在");
            }
            //修改这本书的置顶信息
            Integer isTop = forumEntity.getIsTop();
            //将书本的置顶信息取反
            forumEntity.setIsTop(isTop == 0 ? 1 : 0);
            boolean success = saveOrUpdate(forumEntity);
            return success;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
