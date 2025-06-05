package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.convert.NoticeConvert;
import com.nh.dsh.admin.mapper.NoticeMapper;
import com.nh.dsh.admin.model.dto.NoticeDTO;
import com.nh.dsh.admin.model.entity.ForumEntity;
import com.nh.dsh.admin.model.entity.NoticeEntity;
import com.nh.dsh.admin.model.entity.SysManager;
import com.nh.dsh.admin.model.query.NoticeQuery;
import com.nh.dsh.admin.model.vo.ForumNoticeVO;
import com.nh.dsh.admin.model.vo.NoticeVO;
import com.nh.dsh.admin.service.ForumService;
import com.nh.dsh.admin.service.NoticeService;
import com.nh.dsh.admin.service.SysManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:17
 **/

@Slf4j
@Service
@AllArgsConstructor
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {
    private final SysManagerService managerService;
    private final ForumService forumService;

    @Override
    public void save(NoticeDTO dto) {
        NoticeEntity noticeEntity = NoticeConvert.INSTANCE.convert(dto);
        baseMapper.insert(noticeEntity);
    }

    @Override
    public void update(NoticeDTO dto) {
        NoticeEntity noticeEntity = NoticeConvert.INSTANCE.convert(dto);
        baseMapper.updateById(noticeEntity);
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public PageResult<NoticeVO> page(NoticeQuery query) {
        Page<NoticeEntity> page = getPage(query);
        LambdaQueryWrapper<NoticeEntity> wrapper = new LambdaQueryWrapper<NoticeEntity>()
                .eq(StringUtils.isNotBlank(query.getKeyword()), NoticeEntity::getTitle, "%"+query.getKeyword()+"%")
                .orderByDesc(NoticeEntity::getCreateTime);
        Page<NoticeEntity> pageResult = baseMapper.selectPage(page, wrapper);
        List<NoticeVO> resultList = pageResult.getRecords().stream().map(item -> {
            NoticeVO vo = NoticeConvert.INSTANCE.convert(item);
            SysManager manager = managerService.getById(item.getAdminId());
            vo.setAdminName(manager.getUsername());
            ForumEntity forum = forumService.getById(item.getForumId());
            vo.setForumName(forum.getName());
            return vo;
        }).toList();
        return new PageResult<>(resultList, pageResult.getTotal());
    }

    @Override
    public PageResult<ForumNoticeVO> forumPage(NoticeQuery query) {
        Page<NoticeEntity> page = getPage(query);
        LambdaQueryWrapper<NoticeEntity> wrapper = new LambdaQueryWrapper<NoticeEntity>()
                .eq(NoticeEntity::getForumId, query.getForumId())
                .orderByDesc(NoticeEntity::getIsTop)
                .orderByDesc(NoticeEntity::getCreateTime);
        Page<NoticeEntity> pageResult = baseMapper.selectPage(page, wrapper);
        List<ForumNoticeVO> resultList = pageResult.getRecords().stream().map(item -> {
            ForumNoticeVO vo = NoticeConvert.INSTANCE.convertForum(item);
            SysManager manager = managerService.getById(item.getAdminId());
            vo.setAdminName(manager.getUsername());
            return vo;
        }).toList();
        return new PageResult<>(resultList, pageResult.getTotal());
    }
}
