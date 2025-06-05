package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.DictConfigRequestDTO;
import com.nh.dsh.admin.model.entity.DictConfig;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DictConfigConvert {
    DictConfigConvert INSTANCE = Mappers.getMapper(DictConfigConvert.class);


    DictConfig convertToConfig(DictConfigRequestDTO dto);
}
