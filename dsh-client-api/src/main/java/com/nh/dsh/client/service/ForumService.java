package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.common.result.PageResult;
import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.model.entity.ForumEntity;
import com.nh.dsh.client.model.query.ForumQuery;
import com.nh.dsh.client.model.vo.ForumDetailVO;
import com.nh.dsh.client.model.vo.ForumListItemVO;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 10:54
 **/

public interface ForumService extends IService<ForumEntity> {

    PageResult<ForumListItemVO> forumList(ForumQuery query);

    ForumDetailVO forumDetail(Integer id);

    void followedForum(Integer forumId);

    PageResult<ForumListItemVO> forumListFollowed(ForumQuery query);

    /**
     * 根据类型获取相同类型的书籍
     * @param typeId
     * @param bookId
     * @return
     */
    Result getLikedBook(int typeId, int bookId);
}
