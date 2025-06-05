package com.nh.dsh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nh.dsh.admin.model.entity.Dict;
import com.nh.dsh.admin.model.query.DictQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 字典分页列表
     *
     * @param page
     * @param query
     * @return
     */
    List<Dict> getPage(Page<Dict> page, @Param("query") DictQuery query);
}
