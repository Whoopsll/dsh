package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.dto.NoticeDTO;
import com.nh.dsh.admin.model.entity.NoticeEntity;
import com.nh.dsh.admin.model.vo.ForumNoticeVO;
import com.nh.dsh.admin.model.vo.NoticeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-20 17:18
 **/

@Mapper
public interface NoticeConvert {
    NoticeConvert INSTANCE = Mappers.getMapper(NoticeConvert.class);

    NoticeEntity convert(NoticeDTO dto);

    NoticeVO convert(NoticeEntity entity);

    ForumNoticeVO convertForum(NoticeEntity entity);
}
