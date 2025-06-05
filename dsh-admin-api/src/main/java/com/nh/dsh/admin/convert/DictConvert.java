package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.DictRequestDTO;
import com.nh.dsh.admin.model.entity.Dict;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DictConvert {
    DictConvert INSTANCE = Mappers.getMapper(DictConvert.class);


    Dict convertToDict(DictRequestDTO dto);
}
