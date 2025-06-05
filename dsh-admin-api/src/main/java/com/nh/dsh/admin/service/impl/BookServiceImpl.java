package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.exception.ServerException;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.BookConvert;
import com.nh.dsh.admin.enums.ResourceTypeEnum;
import com.nh.dsh.admin.mapper.BookMapper;
import com.nh.dsh.admin.mapper.ConfigMapper;
import com.nh.dsh.admin.mapper.ForumMapper;
import com.nh.dsh.admin.model.dto.BookDTO;
import com.nh.dsh.admin.model.entity.*;
import com.nh.dsh.admin.model.query.BookQuery;
import com.nh.dsh.admin.model.vo.BookVO;
import com.nh.dsh.admin.security.cache.TokenStoreCache;
import com.nh.dsh.admin.security.user.ManagerDetail;
import com.nh.dsh.admin.service.BookService;
import com.nh.dsh.admin.service.CategoryService;
import com.nh.dsh.admin.service.PublisherService;
import com.nh.dsh.admin.utils.WxUtils;
import com.nh.dsh.admin.wechat.cache.AccessTokenCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 12:43
 **/

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl extends BaseServiceImpl<BookMapper, BookEntity> implements BookService {
    private final CategoryService categoryService;
    private final PublisherService publisherService;
    private final TokenStoreCache tokenStoreCache;
    private final AccessTokenCache tokenCache;
    private final ForumMapper forumMapper;
    private final RestTemplate restTemplate;
    private final ConfigMapper configMapper;

    @Override
    public void saveBook(BookDTO dto) {
        BookEntity book = BookConvert.INSTANCE.convert(dto);
        if (dto.getPublisherId() == null) {
            book.setPublisherId(0);
        }
        baseMapper.insert(book);
    }

    @Override
    public void updateBook(BookDTO dto) {
        BookEntity book = BookConvert.INSTANCE.convert(dto);
        baseMapper.updateById(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBookInBatch(List<Integer> ids) {
        if (ids.isEmpty()) {
            return;
        }
        LambdaUpdateWrapper<ForumEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(ForumEntity::getBookId, ids);
        forumMapper.delete(wrapper);
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public PageResult<BookVO> page(BookQuery query, String authorization) {
        ManagerDetail user = tokenStoreCache.getUser(authorization);
        Page<BookEntity> entityPage = getPage(query);
        LambdaQueryWrapper<BookEntity> wrapper = new LambdaQueryWrapper<BookEntity>()
                .like(StringUtils.isNotBlank(query.getBookName()), BookEntity::getBookName, "%" + query.getBookName() + "%")
                .like(StringUtils.isNotBlank(query.getWriter()), BookEntity::getWriter, "%" + query.getWriter() + "%")
                .like(StringUtils.isNotBlank(query.getIsbn()), BookEntity::getIsbn, "%" + query.getIsbn() + "%")
                .eq(query.getPublisherId() != null, BookEntity::getPublisherId, query.getPublisherId())
                .in(user.getPublisherIdList() != null && !user.getPublisherIdList().isEmpty(), BookEntity::getPublisherId, user.getPublisherIdList())
                .orderByDesc(BookEntity::getIsTop, BookEntity::getCreateTime);
        Page<BookEntity> pageResult = baseMapper.selectPage(entityPage, wrapper);
        List<BookVO> collectList = pageResult.getRecords().stream().map(item -> {
            BookVO vo = BookConvert.INSTANCE.convert(item);
            PublisherEntity publisher = publisherService.getById(item.getPublisherId());
            if (publisher != null) {
                vo.setPublishingHouse(publisher.getName());
            }
            CategoryEntity category = categoryService.getById(item.getClassificationId());
            if (category == null) {
                return vo;
            }
            vo.setClassification(category.getName());
            //书籍论坛一直跳转到微信公众号,获取该本书出版社的公众号配置信息
            List<ConfigEntity> configs = configMapper.selectList(new QueryWrapper<ConfigEntity>().eq("delete_flag", 0).eq("publisher_id", vo.getPublisherId()));
            if (!configs.isEmpty()) {
                //不为空的情况下，生成微信公众号二维码
                ConfigEntity entity = configs.get(0);
                if(StringUtils.isNotBlank(item.getForumLink())) {
                    // 链接不为空的情况下，生成微信公众号带参二维码
                    String wxLink = WxUtils.getWxLink(ResourceTypeEnum.BOOK.getValue(), item.getId(), entity.getAccount(), entity.getPassword(), entity.getId());
                    vo.setQrLink(wxLink);
                } else {
                    // 没有论坛的情况下，返回正常链接
                    vo.setQrLink("");
                }
            } else {
                vo.setQrLink(item.getForumLink());
            }
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(collectList, pageResult.getTotal());
    }

    @Override
    public List<BookVO> selectedList(String authorization) {
        ManagerDetail user = tokenStoreCache.getUser(authorization);
        LambdaQueryWrapper<BookEntity> query = new LambdaQueryWrapper<>();
        query.in(user.getPublisherIdList() != null && !user.getPublisherIdList().isEmpty(), BookEntity::getPublisherId, user.getPublisherIdList());
        return BookConvert.INSTANCE.convertList(list(query));
    }

    @Override
    public boolean stickyBook(int bookId) {
        try {
            BookEntity bookEntity = baseMapper.selectById(bookId);
            if (bookEntity == null) {
                throw new ServerException("这本书不存在");
            }
            //修改这本书的置顶信息
            Integer isTop = bookEntity.getIsTop();
            //将书本的置顶信息取反
            bookEntity.setIsTop(isTop == 0 ? 1 : 0);
            boolean success = saveOrUpdate(bookEntity);
            return success;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }
}
