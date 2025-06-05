package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.BookDTO;
import com.nh.dsh.admin.model.entity.BookEntity;
import com.nh.dsh.admin.model.vo.BookVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-17 12:48
 **/

@Mapper
public interface BookConvert {
    BookConvert INSTANCE = Mappers.getMapper(BookConvert.class);

    BookEntity convert(BookDTO dto);

    BookVO convert(BookEntity book);
    List<BookVO> convertList(List<BookEntity> list);
}
