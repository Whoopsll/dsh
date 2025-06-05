package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.entity.SysManager;
import com.nh.dsh.admin.model.vo.SysManagerVO;
import com.nh.dsh.admin.security.user.ManagerDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysManagerConvert {
    SysManagerConvert INSTANCE = Mappers.getMapper(SysManagerConvert.class);

    SysManager convert(SysManagerVO vo);

    ManagerDetail convertDetail(SysManager entity);

    List<SysManagerVO> convertList(List<SysManager> list);
}
