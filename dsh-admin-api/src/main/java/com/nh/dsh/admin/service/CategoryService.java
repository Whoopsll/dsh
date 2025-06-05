package com.nh.dsh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.model.dto.CategoryDTO;
import com.nh.dsh.admin.model.entity.CategoryEntity;
import com.nh.dsh.admin.model.query.CategoryQuery;
import com.nh.dsh.admin.model.vo.CategoryTreeNode;
import com.nh.dsh.admin.model.vo.CategoryVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:37
 **/

public interface CategoryService extends IService<CategoryEntity> {

    void save(CategoryDTO dto);

    void update(CategoryDTO dto);

    void deleteInBatch(List<Integer> ids);

    Result<List<CategoryTreeNode>> page(CategoryQuery query);

    void batchInsert();
}
