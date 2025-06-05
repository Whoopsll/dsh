package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.model.dto.PublisherDTO;
import com.nh.dsh.admin.model.entity.PublisherEntity;
import com.nh.dsh.admin.model.query.PublisherQuery;
import com.nh.dsh.admin.model.vo.PublisherVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-29 15:37
 **/

public interface PublisherService extends IService<PublisherEntity> {

    void save(PublisherDTO dto);

    void update(PublisherDTO dto);

    void deleteInBatch(List<Integer> ids);

    PageResult<PublisherVO> page(PublisherQuery query);
}
