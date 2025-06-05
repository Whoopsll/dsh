package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.dto.ForumDTO;
import com.nh.dsh.admin.model.entity.ForumEntity;
import com.nh.dsh.admin.model.query.ForumQuery;
import com.nh.dsh.admin.model.vo.ForumVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 15:52
 **/

public interface ForumService extends IService<ForumEntity> {

    void save(ForumDTO dto);

    void update(ForumDTO dto);

    void deleteInBatch(List<Integer> ids);

    PageResult<ForumVO> page(ForumQuery query, String authorization);

    /**
     * 置顶论坛
     * @param forumId
     * @return
     */
    boolean stickyForum(int forumId);
}
