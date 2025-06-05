package com.nh.dsh.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.client.common.result.Result;
import com.nh.dsh.client.model.dto.CodeDTO;
import com.nh.dsh.client.model.entity.BookExchangeEntity;
import com.nh.dsh.client.model.entity.BookResourceEntity;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-08-05 22:55
 **/

public interface BookResourceService extends IService<BookResourceEntity> {

    String checkBookResource(CodeDTO dto);


}
