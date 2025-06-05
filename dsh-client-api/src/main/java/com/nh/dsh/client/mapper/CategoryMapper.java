package com.nh.dsh.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nh.dsh.client.model.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-06-17 14:36
 **/

@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
}
