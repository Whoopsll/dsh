package com.nh.dsh.admin.convert;

import com.nh.dsh.admin.model.entity.SysRole;
import com.nh.dsh.admin.model.vo.SysRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysRoleConvert {
    SysRoleConvert INSTANCE = Mappers.getMapper(SysRoleConvert.class);

    SysRoleVO convert(SysRole entity);

    SysRole convert(SysRoleVO vo);

    List<SysRoleVO> convertList(List<SysRole> list);

}
