package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.ForumDTO;
import com.nh.dsh.admin.model.entity.ForumEntity;
import com.nh.dsh.admin.model.vo.ForumVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 15:50
 **/

@Mapper
public interface ForumConvert {
    ForumConvert INSTANCE = Mappers.getMapper(ForumConvert.class);

    ForumEntity convert(ForumDTO dto);

    ForumVO convert(ForumEntity entity);

    List<ForumVO> convertList(List<ForumEntity> list);
}
