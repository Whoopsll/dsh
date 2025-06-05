package com.nh.dsh.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.protocol.ResultBuilder;
import com.nh.dsh.admin.common.model.BaseServiceImpl;
import com.nh.dsh.admin.common.result.PageResult;
import com.nh.dsh.admin.common.result.Result;
import com.nh.dsh.admin.convert.CategoryConvert;
import com.nh.dsh.admin.mapper.CategoryMapper;
import com.nh.dsh.admin.model.dto.CategoryDTO;
import com.nh.dsh.admin.model.entity.CategoryEntity;
import com.nh.dsh.admin.model.query.CategoryQuery;
import com.nh.dsh.admin.model.vo.Category;
import com.nh.dsh.admin.model.vo.CategoryTreeNode;
import com.nh.dsh.admin.model.vo.CategoryVO;
import com.nh.dsh.admin.service.CategoryService;
import com.nh.dsh.admin.utils.JsonParser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:37
 **/

@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Override
    public void save(CategoryDTO dto) {
        CategoryEntity category = CategoryConvert.INSTANCE.convert(dto);
        baseMapper.insert(category);
    }

    @Override
    public void update(CategoryDTO dto) {
        CategoryEntity category = CategoryConvert.INSTANCE.convert(dto);
        baseMapper.updateById(category);
    }

    @Override
    public void deleteInBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public Result<List<CategoryTreeNode>> page(CategoryQuery query) {
        List<CategoryEntity> records = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("delete_flag", 0).orderByDesc("create_time"));
        // Page<CategoryEntity> entityPage = getPage(query);
        // LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<CategoryEntity>().orderByDesc(CategoryEntity::getCreateTime);
        // Page<CategoryEntity> pageResult = baseMapper.selectPage(entityPage, wrapper);
        // List<CategoryEntity> records = pageResult.getRecords();
        List<CategoryTreeNode> result = records.stream().filter(item -> item.getParentId() == 0).map(item -> converCategoryTreeNode(item, records)).collect(Collectors.toList());
        return Result.ok(result);
    }

    @Override
    public void batchInsert() {
        String filePath = "src/main/resources/bookTree.json";
        List<Category> node = null;
        try {
            node = JsonParser.parseJsonFile(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Category category : node) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setDescription("中图法分类:" +category.getCode());
            categoryEntity.setName(category.getName());
            categoryEntity.setParentId(0);
            baseMapper.insert(categoryEntity);
            List<Category> children = category.getChildren();
            if (!children.isEmpty()) {
                for (Category child : children) {
                    CategoryEntity categoryEntity1 = new CategoryEntity();
                    categoryEntity1.setDescription("中图法分类:" +child.getCode());
                    categoryEntity1.setName(child.getName());
                    categoryEntity1.setParentId(categoryEntity.getId());
                    baseMapper.insert(categoryEntity1);
                }
            }
        }
    }

    /**
     * 将分类扁平化处理为树形结构
     * @param entity
     * @param entityList
     * @return
     */
    private CategoryTreeNode converCategoryTreeNode(CategoryEntity entity, List<CategoryEntity> entityList) {
        CategoryTreeNode node = new CategoryTreeNode();
        BeanUtils.copyProperties(entity, node);
        node.setId(entity.getId());
        List<CategoryTreeNode> children = entityList.stream().filter(subMenu -> subMenu.getParentId() == (entity.getId()))
                .map(subMenu -> converCategoryTreeNode(subMenu, entityList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
