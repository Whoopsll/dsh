package com.nh.dsh.client.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nh.dsh.client.common.config.CodeConfig;
import com.nh.dsh.client.common.exception.ErrorCode;
import com.nh.dsh.client.common.exception.ServerException;
import com.nh.dsh.client.mapper.BookResourceMapper;
import com.nh.dsh.client.mapper.UserMapper;
import com.nh.dsh.client.model.BaseServiceImpl;
import com.nh.dsh.client.model.dto.CodeDTO;
import com.nh.dsh.client.model.entity.BookExchangeEntity;
import com.nh.dsh.client.model.entity.BookResourceEntity;
import com.nh.dsh.client.model.entity.UserEntity;
import com.nh.dsh.client.service.BookResourceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-08-05 22:55
 **/

@Slf4j
@Service
@AllArgsConstructor
public class BookResourceServiceImpl extends BaseServiceImpl<BookResourceMapper, BookResourceEntity> implements BookResourceService {
    private final CodeConfig codeConfig;
    private final UserMapper userMapper;

    @Override
    public String checkBookResource(CodeDTO dto) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getWxId, dto.getOpenId()));
        if (count != 1) {
            log.error("不是本系统的用户,openId:{}", dto.getOpenId());
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        if (!dto.getCodeSecret().equals(codeConfig.getSecret())) {
            this.invalidCode(dto.getResourceId());
            throw new ServerException("codeSecret error");
        }
        BookResourceEntity bookResourceEntity = baseMapper.selectById(dto.getResourceId());
            if(bookResourceEntity.getCanAccess() == 0 || bookResourceEntity.getAuditResult() == 1 || bookResourceEntity.getValidStatus() == 1) {
            throw new ServerException("该资源不可访问");
        }
        return bookResourceEntity.getLink();
    }


    private void invalidCode(Integer resourceId) {
        BookResourceEntity bookResourceEntity = baseMapper.selectById(resourceId);
        bookResourceEntity.setValidStatus(1);
        baseMapper.updateById(bookResourceEntity);
    }
}
