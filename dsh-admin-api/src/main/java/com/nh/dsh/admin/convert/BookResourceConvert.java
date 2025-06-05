package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.BookResourceDTO;
import com.nh.dsh.admin.model.entity.BookResourceEntity;
import com.nh.dsh.admin.model.vo.AuditResourceVO;
import com.nh.dsh.admin.model.vo.BookResourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-18 09:54
 **/

@Mapper
public interface BookResourceConvert {

    BookResourceConvert INSTANCE = Mappers.getMapper(BookResourceConvert.class);

    BookResourceEntity convert(BookResourceDTO dto);

    AuditResourceVO convert(BookResourceEntity entity);

    List<BookResourceVO> convertList(List<BookResourceEntity> list);
}
