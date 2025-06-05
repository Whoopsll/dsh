package com.nh.dsh.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nh.dsh.client.model.entity.BookEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-06-17 12:38
 **/

@Mapper
public interface BookMapper extends BaseMapper<BookEntity> {
}
