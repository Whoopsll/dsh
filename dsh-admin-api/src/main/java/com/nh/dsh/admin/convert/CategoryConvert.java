package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.CategoryDTO;
import com.nh.dsh.admin.model.entity.CategoryEntity;
import com.nh.dsh.admin.model.vo.CategoryTreeNode;
import com.nh.dsh.admin.model.vo.CategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 14:42
 **/

@Mapper
public interface CategoryConvert {
    CategoryConvert INSTANCE = Mappers.getMapper(CategoryConvert.class);

    CategoryEntity convert(CategoryDTO dto);

    List<CategoryVO> convertList(List<CategoryEntity> list);

    List<CategoryVO> convertToList(List<CategoryTreeNode> list);
}
