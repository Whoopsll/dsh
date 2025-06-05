package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.dto.NoticeDTO;
import com.nh.dsh.admin.model.entity.NoticeEntity;
import com.nh.dsh.admin.model.query.NoticeQuery;
import com.nh.dsh.admin.model.vo.ForumNoticeVO;
import com.nh.dsh.admin.model.vo.NoticeVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:17
 **/

public interface NoticeService extends IService<NoticeEntity> {

    void save(NoticeDTO dto);

    void update(NoticeDTO dto);

    void deleteInBatch(List<Integer> ids);

    PageResult<NoticeVO> page(NoticeQuery query);

    PageResult<ForumNoticeVO> forumPage(NoticeQuery query);

}
