package com.nh.dsh.client.convert;

import com.nh.dsh.client.model.dto.CommentDTO;
import com.nh.dsh.client.model.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 15:01
 **/

@Mapper
public interface CommentConvert {
    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);

    CommentEntity convert(CommentDTO dto);
}
