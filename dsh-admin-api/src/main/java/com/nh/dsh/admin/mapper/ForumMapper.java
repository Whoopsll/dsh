package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.model.entity.ForumEntity;
import com.nh.dsh.admin.model.query.ForumQuery;
import com.nh.dsh.admin.model.vo.ForumVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 15:52
 **/

@Mapper
public interface ForumMapper extends BaseMapper<ForumEntity> {

    List<ForumVO> getPage(Page<ForumEntity> page, @Param("query")ForumQuery query, @Param("publisherList") List<Integer> publisherList);
}
