package com.nh.dsh.client.convert;

import com.nh.dsh.client.model.entity.ForumEntity;
import com.nh.dsh.client.model.vo.ForumDetailVO;
import com.nh.dsh.client.model.vo.ForumListItemVO;
import org.mapstruct.Mapper;

/**
 * @author : Flobby
 * @program : dsh-client-api
 * @description :
 * @create : 2024-07-02 11:38
 **/

@Mapper
public interface ForumConvert {

    ForumConvert INSTANCE = org.mapstruct.factory.Mappers.getMapper(ForumConvert.class);

    ForumListItemVO convert(ForumEntity entity);

    ForumDetailVO convertDetail(ForumEntity entity);
}
