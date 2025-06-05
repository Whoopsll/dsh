package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.entity.FeedbackEntity;
import com.nh.dsh.admin.model.vo.FeedbackVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author : Flobby
 * @program : dsh-admin-api
 * @description :
 * @create : 2024-06-19 09:56
 **/

@Mapper
public interface FeedbackConvert {
    FeedbackConvert INSTANCE = Mappers.getMapper(FeedbackConvert.class);

    FeedbackVO convert(FeedbackEntity entity);
}
