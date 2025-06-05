package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.PublisherDTO;
import com.nh.dsh.admin.model.entity.PublisherEntity;
import com.nh.dsh.admin.model.vo.PublisherVO;
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
public interface PublisherConvert {
    PublisherConvert INSTANCE = Mappers.getMapper(PublisherConvert.class);

    PublisherEntity convert(PublisherDTO dto);

    List<PublisherVO> convertList(List<PublisherEntity> list);
}
